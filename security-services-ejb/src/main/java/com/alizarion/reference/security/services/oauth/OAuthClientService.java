package com.alizarion.reference.security.services.oauth;

import com.alizarion.reference.security.doa.OAuthJpaDao;
import com.alizarion.reference.security.entities.Token;
import com.alizarion.reference.security.entities.oauth.OAuthAccessToken;
import com.alizarion.reference.security.entities.oauth.OAuthCredential;
import com.alizarion.reference.security.entities.oauth.client.OAuthClientAuthorization;
import com.alizarion.reference.security.entities.oauth.client.OAuthScopeClient;
import com.alizarion.reference.security.entities.oauth.client.OAuthServerApplication;
import com.alizarion.reference.security.exception.oauth.ClientIdNotFoundException;
import com.alizarion.reference.security.exception.oauth.InvalidRefreshTokenException;
import com.alizarion.reference.security.exception.oauth.InvalidScopeException;
import com.alizarion.reference.security.exception.oauth.StateNotFoundException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Providing service to generate client OAuth requests.
 * @author selim@openlinux.fr.
 */
@Stateless
@TransactionAttribute
public class OAuthClientService implements Serializable {

    @PersistenceContext
    EntityManager em;

    private OAuthJpaDao authDao;

    @PostConstruct
    public void setUp(){
        this.authDao = new OAuthJpaDao(this.em);
    }

    private static final long serialVersionUID = 8067817736543902873L;

    /**
     * Method to get oauth authorization.
     * @param user that will be linked to this request.
     * @param clientId OAuthServerApplication that will be requested.
     * @param scopes scopes to be requested to the app.
     * @return OAuthClientAuthorization that <br/>
     * contain all necessary information
     * to process tu request.
     * @throws InvalidScopeException if scope not linked to the app.
     */
    public OAuthClientAuthorization getOAuthCodeRequest(
            final OAuthCredential user,
            final String clientId,
            final Set<OAuthScopeClient> scopes)
            throws InvalidScopeException,
            ClientIdNotFoundException {
        OAuthServerApplication application =
                this.authDao.
                        findOAuthServerApplicationByClientId(clientId);
        OAuthClientAuthorization authorization =
                (OAuthClientAuthorization)
                        application.
                                addAuthorization(user,scopes);
        this.authDao.merge(application);
        this.em.refresh(authorization);
        return authorization;
    }

    public OAuthClientAuthorization getOAuthCodeRequest(
            final OAuthCredential user,
            final String clientId)
            throws InvalidScopeException, ClientIdNotFoundException {
        OAuthServerApplication application =
                this.authDao.
                        findOAuthServerApplicationByClientId(clientId);
        return getOAuthCodeRequest(user,clientId,
                application.getDefaultClientScopes());
    }



    /**
     * Method to persist new received access token.
     * @param token received token
     * @param state  associate state to get back the good authorization
     * @param duration alive duration for this token
     * @return OAuthAccessToken alive access token
     * @throws StateNotFoundException if not corresponding state found.
     */
    public OAuthAccessToken addAccessToken(final String token,
                                           final String state,
                                           final Long duration,
                                           final String refreshToken)
            throws StateNotFoundException{
        OAuthClientAuthorization authorization =
                this.authDao.findClientAuthByState(state);
        OAuthAccessToken accessToken =
                authorization.addAccessToken(duration,token);
        if (refreshToken != null){
            authorization.
                    setRefreshToken(new Token(refreshToken));
        }
        this.em.merge(authorization);
        return accessToken;
    }

    /**
     * Method to get any alive access token for user.
     * @param credentialId id of the associated user.
     * @return  oauth access token if any remain.
     */
    public OAuthAccessToken getAliveAccessToken(final Long credentialId){
        List<OAuthAccessToken> result =
                this.authDao.
                        getAliveAccessToken(credentialId);
        if (result.isEmpty()){
            return null;
        } else {
            return result.get(0);
        }
    }


    public OAuthClientAuthorization getAliveRefreshToken(final Long credentialId)
            throws InvalidRefreshTokenException {
        return this.authDao.findAliveRefreshToken(credentialId);
    }


}
