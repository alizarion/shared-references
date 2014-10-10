package com.alizarion.reference.security.services.oauth;

import com.alizarion.reference.security.doa.OAuthJpaDao;
import com.alizarion.reference.security.entities.oauth.OAuthAccessToken;
import com.alizarion.reference.security.entities.oauth.OAuthCredential;
import com.alizarion.reference.security.entities.oauth.OAuthDuration;
import com.alizarion.reference.security.entities.oauth.server.OAuthClientApplication;
import com.alizarion.reference.security.entities.oauth.server.OAuthScopeServer;
import com.alizarion.reference.security.entities.oauth.server.OAuthServerAuthorization;
import com.alizarion.reference.security.exception.TokenExpiredException;
import com.alizarion.reference.security.exception.oauth.*;
import com.alizarion.reference.security.services.resources.OAuthServerMBean;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 * Class providing OAuth 2.0 server services.
 * @author selim@openlinux.fr.
 * @see com.alizarion.reference.security.entities.oauth.server.OAuthClientApplication
 * @see com.alizarion.reference.security.entities.oauth.server.OAuthServerAuthorization
 * @see com.alizarion.reference.security.entities.oauth.OAuthAccessToken
 * @see com.alizarion.reference.security.doa.OAuthJpaDao
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
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


    /**
     * Method to get a new requested OAuth authorization biz object
     * @param clientId  client_id
     * @param redirectURI client_secret
     * @param requestedScopes list of scopes
     * @param responseType  required response type
     * @param oAuthCredential  logged user
     * @return OAuth authorization biz object
     * @throws InvalidScopeException if one of the requested scope are unavailable
     * @throws ClientIdNotFoundException if bad clientid has been requested
     */
    public OAuthServerAuthorization getAuthorization(
            final String clientId,
            final String redirectURI,
            final Set<String> requestedScopes,
            final String responseType,
            final String duration,
            final OAuthCredential oAuthCredential)
            throws InvalidScopeException,
            ClientIdNotFoundException{
        OAuthClientApplication application =
                this.authDao.
                        findOAuthClientApplicationByClientIdAndRedirectURI(
                                clientId,
                                redirectURI);

        this.em.detach(application);
        Set<OAuthScopeServer>  scopes = new HashSet<>();
        for (String scope : requestedScopes ){
            scopes.add(application
                    .getServerScopeByKey(scope));
        }
        OAuthServerAuthorization authorization = (OAuthServerAuthorization)
                application
                        .addAuthorization(oAuthCredential, scopes);
        if (responseType
                .equals("code")){
            authorization.generateCode();
        }
        if (OAuthDuration.P.equals(duration)){
            authorization.generateRefreshToken(
                    this.serverMBean.
                            getRefreshTokenDurationSecond());
        }
        return authorization;

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
            InvalidRefreshTokenException, TokenExpiredException {

        OAuthServerAuthorization authorization =
                this.authDao.findAliveServerAuthByToken(refreshToken);
        if (!authorization.getRefreshToken().isValidToken()){
            throw new TokenExpiredException(refreshToken);
        }
        if (authorization.
                getAuthApplication().
                getApplicationKey().
                getClientId().
                equals(clientId)){
            return authorization;
        }  else {
            throw new ClientIdNotFoundException(clientId);
        }
    }


    /**
     * Method to persist the authorization that have been accepted by the user
     * @param authorization accepted authorization
     * @return merged authorization
     */
    public OAuthServerAuthorization acceptAuthorization(
            final OAuthServerAuthorization authorization) {
        if (authorization.getAuthCode() == null) {
            authorization.addAccessToken(this
                    .serverMBean
                    .getAccessTokenDurationSecond());
        }
        return this.em.merge(authorization);
    }


    /**
     * Method to truncate auth code with new access token.
     * @param code received code.
     * @param clientId requested clientId
     * @return  OAuthAccessToken
     * @throws ClientIdNotFoundException
     * @throws BadCredentialException
     * @throws InvalidAuthCodeException
     */
    public OAuthAccessToken getNewAccessTokenByOAuthCode(final String code,
                                                         final String clientId)
            throws ClientIdNotFoundException,
            BadCredentialException,
            InvalidAuthCodeException {
        OAuthServerAuthorization authorization =  validateCode(code,clientId);
        OAuthAccessToken accessToken = authorization.
                addAccessToken(serverMBean.
                        getAccessTokenDurationSecond());
        authorization.getAuthCode().revoke();
        this.em.merge(authorization);
        return accessToken;
    }


    /**
     * Method get new access token using refresh token.
     * @param refreshToken received refresh token
     * @param clientId received clientID
     * @return OAuthAccessToken as access token
     * @throws ClientIdNotFoundException if the clientId is unknown
     * @throws InvalidRefreshTokenException refresh token does
     * not exist or has expired.
     */
    public OAuthAccessToken getNewAccessTokenByOAuthRefreshToken(
            final String refreshToken,
            final String clientId)
            throws ClientIdNotFoundException,
            InvalidRefreshTokenException,
            TokenExpiredException {

        OAuthServerAuthorization authorization =
                validateRefreshToken(refreshToken, clientId);
        OAuthAccessToken accessToken = authorization.
                addAccessToken(serverMBean.
                        getAccessTokenDurationSecond());
        authorization.getRefreshToken().addTime(
                this.serverMBean.
                        getRefreshTokenDurationSecond());
        this.em.merge(authorization);
        return accessToken;
    }

    public OAuthClientApplication  authenticateClientRequest
            (final String clientId,
             final String secret)
            throws BadCredentialException,
            ClientIdNotFoundException {
        final OAuthClientApplication application =
                this.authDao.
                        findOAuthClientApplicationByClientId(clientId);
        if (application.getApplicationKey().
                getClientSecret().equals(secret)){
            return application;
        } else {
            throw new BadCredentialException(clientId);

        }
    }

    /**
     * Method to get new access token using username and password
     * @param username to use
     * @param password  to use
     * @param requestedScopes requested scopes
     * @param application OAuthClientApplication as the declared client.
     * @return  OAuthAccessToken
     * @throws BadCredentialException if bad username or password.
     * @throws URISyntaxException if the bad redirect_uri
     * have been persisted
     * @throws ClientIdNotFoundException if OAuthClientApplication
     * id reference does not exist
     * @throws InvalidScopeException if one of the requested scope does not exist
     */
    public OAuthAccessToken getAccessTokenByPassword(
            final String username,
            final String password,
            final Set<String> requestedScopes,
            final OAuthClientApplication application)
            throws BadCredentialException,
            URISyntaxException,
            ClientIdNotFoundException,
            InvalidScopeException {

        OAuthCredential credential = this.authDao
                .findOAuthCredentialByUsername(username);
        if (!credential.isCorrectPassword(password)){
            throw new BadCredentialException(username);
        }

        OAuthServerAuthorization authorization =  getAuthorization(
                application.getApplicationKey().getClientId()
                , application.getRedirectURI().toString(),
                requestedScopes,
                "token",
                OAuthDuration.P.toString(),
                credential);
        acceptAuthorization(authorization);
        return authorization.addAccessToken(this.serverMBean
                .getAccessTokenDurationSecond());
    }
}
