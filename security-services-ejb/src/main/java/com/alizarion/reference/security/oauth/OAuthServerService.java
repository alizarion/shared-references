package com.alizarion.reference.security.oauth;

import com.alizarion.reference.security.doa.OAuthJpaDao;
import com.alizarion.reference.security.entities.Credential;
import com.alizarion.reference.security.entities.oauth.OAuthAccessToken;
import com.alizarion.reference.security.entities.oauth.server.OAuthClientApplication;
import com.alizarion.reference.security.entities.oauth.server.OAuthServerAuthorization;
import com.alizarion.reference.security.exception.oauth.*;
import com.alizarion.reference.security.resources.OAuthServerMBean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
@Stateless
public class OAuthServerService implements Serializable {

    private static final long serialVersionUID = 2039876823561689686L;

    @PersistenceContext
    EntityManager em;

    private OAuthJpaDao authDao;

    @EJB
    private OAuthServerMBean serverMBean;

    @PostConstruct
    public void setUp(){
        this.authDao = new OAuthJpaDao(this.em);
    }

    public OAuthServerAuthorization getTemporaryOAuthCodeResponse(
            final Credential user,
            final String clientId,
            final Set<String> scopes)
            throws InvalidScopeException, ClientIdNotFoundException {
        OAuthClientApplication application =
                this.authDao.
                        findOAuthClientApplicationByClientId(clientId);
        OAuthServerAuthorization authorization =
                (OAuthServerAuthorization)
                        application.
                                addAuthorization(user, scopes);
        this.authDao.merge(application);
        this.em.refresh(authorization);
        return authorization;

    }

    public OAuthServerAuthorization getPermanentOAuthCodeResponse(
            final Credential user,
            final String clientId,
            final Set<String> scopes)
            throws InvalidScopeException, ClientIdNotFoundException {
        OAuthClientApplication application =
                this.authDao.
                        findOAuthClientApplicationByClientId(clientId);
        OAuthServerAuthorization authorization =
                (OAuthServerAuthorization)
                        application.
                                addAuthorization(user, scopes);
        authorization.generateRefreshToken(
                serverMBean.
                        getRefreshTokenDurationSecond());
        this.authDao.merge(application);
        this.em.refresh(authorization);
        return authorization;
    }

    public OAuthClientApplication  authenticateClientRequest
            (final String clientId,
             final String secret) throws BadCredentialException {
        try {
            final OAuthClientApplication application =
                    this.authDao.
                            findOAuthClientApplicationByClientId(clientId);
            if (application.getApplicationKey().
                    getClientSecret().equals(secret)){
                return application;
            } else {
                throw new BadCredentialException(clientId);

            }
        } catch (ClientIdNotFoundException e) {
            throw new BadCredentialException(clientId);
        }
    }

    private OAuthServerAuthorization validateCode(final String code,
                                                  final String clientId)
            throws InvalidAuthCodeException,
            ClientIdNotFoundException,
            BadCredentialException {
        OAuthServerAuthorization authorization =
                this.authDao.findAliveServerAuthByCode(code);
        if (!authorization.
                getAuthApplication().
                getApplicationKey().
                getClientId().
                equals(clientId)){
            throw new BadCredentialException(clientId);
        }
        return  this.authDao.findAliveServerAuthByCode(code);
    }

    private OAuthServerAuthorization validateRefreshToken(final String refreshToken,
                                                          final String clientId)
            throws ClientIdNotFoundException,
            BadCredentialException,
            InvalidRefreshTokenException {

        OAuthServerAuthorization authorization =
                this.authDao.findAliveServerAuthByToken(refreshToken);
        if (authorization.
                getAuthApplication().
                getApplicationKey().
                getClientId().
                equals(clientId)){
            return authorization;
        }  else {
            throw new BadCredentialException(clientId);
        }
    }

    public OAuthAccessToken getNewAccessTokenByOAuthCode(final String code,
                                                         final String clientId)
            throws ClientIdNotFoundException,
            BadCredentialException,
            InvalidAuthCodeException {
        OAuthServerAuthorization authorization =  validateCode(code,clientId);
        OAuthAccessToken accessToken = authorization.
                getNewAccessToken(serverMBean.
                        getAccessTokenDurationSecond());
        authorization.getAuthCode().revoke();
        this.em.merge(authorization);
        return accessToken;
    }



    public OAuthAccessToken getNewAccessTokenByOAuthRefreshToken(
            final String refreshToken,
            final String clientId)
            throws ClientIdNotFoundException,
            InvalidRefreshTokenException,
            BadCredentialException {

        OAuthServerAuthorization authorization =
                validateRefreshToken(refreshToken, clientId);
        OAuthAccessToken accessToken = authorization.
                getNewAccessToken(serverMBean.
                        getAccessTokenDurationSecond());
        authorization.getRefreshToken().addTime(
                this.serverMBean.
                        getRefreshTokenDurationSecond());
        this.em.merge(authorization);
        return accessToken;
    }


}
