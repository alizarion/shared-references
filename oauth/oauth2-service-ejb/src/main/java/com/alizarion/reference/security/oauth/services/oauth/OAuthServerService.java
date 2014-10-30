package com.alizarion.reference.security.oauth.services.oauth;


import com.alizarion.reference.security.oauth.oauth2.dao.OAuthJpaDao;
import com.alizarion.reference.security.oauth.oauth2.entities.*;
import com.alizarion.reference.security.oauth.oauth2.entities.server.OAuthClientApplication;
import com.alizarion.reference.security.oauth.oauth2.entities.server.OAuthScopeServer;
import com.alizarion.reference.security.oauth.oauth2.entities.server.OAuthServerAuthorization;
import com.alizarion.reference.security.oauth.oauth2.entities.server.OAuthSignatureKeyPair;
import com.alizarion.reference.security.oauth.oauth2.exception.*;
import com.alizarion.reference.security.oauth.services.resources.OAuthServerMBean;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Class providing OAuth 2.0 server services.
 * @author selim@openlinux.fr.
 * @see OAuthClientApplication
 * @see OAuthServerAuthorization
 * @see OAuthAccessToken
 * @see OAuthJpaDao
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class OAuthServerService implements Serializable {

    private static final long serialVersionUID = 2039876823561689686L;

    private final static Logger LOG =  Logger.getLogger(OAuthServerService.class);

    @PersistenceContext
    EntityManager em;

    private OAuthJpaDao authDao;

    @EJB
    private OAuthServerMBean serverMBean;

    @EJB
    private CipherSecurityInitializer securityInitializer;

    @Resource
    private SessionContext context;

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
     * @param credentialUsername  logged user
     * @return OAuth authorization biz object
     * @throws InvalidScopeException if one of the requested scope are unavailable
     * @throws ClientIdNotFoundException if bad clientid has been requested
     */
    public OAuthServerAuthorization getAuthorization(
            final String clientId,
            final String redirectURI,
            final Set<String> requestedScopes,
            final OAuthResponseType responseType,
            final String duration,
            final String credentialUsername)
            throws InvalidScopeException,
            ClientIdNotFoundException,
            BadCredentialException {
        //check if  exist
        OAuthCredential oAuthCredential = this.authDao
                .findOAuthCredentialByUsername(credentialUsername) ;
        OAuthServerAuthorization authorization =
                this.authDao
                        .findAliveServerAuthForCredential(
                                oAuthCredential.getIdToString()
                                ,clientId);
        OAuthClientApplication application =
                this.authDao.
                        findOAuthClientApplicationByClientIdAndRedirectURI(
                                clientId,
                                redirectURI);
        this.em.clear();

        Set<OAuthScopeServer>  scopes = application
                .getServerScopesByKeys(requestedScopes);
        if (authorization != null){
            authorization.setScopes(scopes);
            authorization.generateCode();

        }  else {
            authorization = (OAuthServerAuthorization)
                    application
                            .addAuthorization(oAuthCredential, scopes);
        }

        if (responseType
                .equals(OAuthResponseType.C)){
            authorization.generateCode();
        } else if(responseType.equals(OAuthResponseType.T)){
            authorization.addAccessToken(this
                    .serverMBean
                    .getAccessTokenDurationSecond());
        }

        if (OAuthDuration.P.equals(duration) &&
                !authorization.isPermanent()){
            authorization.generateRefreshToken(
                    this.serverMBean.
                            getRefreshTokenDurationSecond());
        } else if (OAuthDuration.T.equals(duration) &&
                authorization.isPermanent()){
            authorization.getRefreshToken().revoke();
        }

        if (authorization.isPromptRequired()){
            authorization.revokeAccess();
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
        return authorization;
    }

    private OAuthServerAuthorization validateRefreshToken(final String refreshToken,
                                                          final String clientId)
            throws ClientIdNotFoundException,
            InvalidRefreshTokenException, TokenExpiredException {

        OAuthServerAuthorization authorization =
                this.authDao.findAliveServerAuthByToken(refreshToken);
        if (!authorization.getRefreshToken().isAlive()){
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

    public OAuthClientApplication authenticateClientRequest
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

    public OAuthAccessToken findAliveAccessToken(final String bearerToken)
            throws InvalidAccessTokenException {
        OAuthAccessToken accessToken =
                this.authDao.findOAuthAccessByToken(bearerToken);

        if (accessToken.getBearer().isAlive()){
            return accessToken;
        }  else {
            throw new InvalidAccessTokenException(bearerToken);
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
                OAuthResponseType.T,
                OAuthDuration.P.toString(),
                credential.getUsername());
        acceptAuthorization(authorization);
        return authorization.addAccessToken(this.serverMBean
                .getAccessTokenDurationSecond());
    }

    public List<OAuthSignatureKeyPair> getOrderedAliveCerts() throws OAuthOpenIDSignatureException {
        List<OAuthSignatureKeyPair> alive = new ArrayList<>(this.authDao.findAliveCert());
        if (alive.isEmpty()){
            try {
                OAuthSignatureKeyPair signatureKeyPair =  new OAuthSignatureKeyPair(
                        this.securityInitializer.getAESKey(),
                        CipherSecurityInitializer.CRYPT_ALG,
                        CipherSecurityInitializer.SIGN_ALG,
                        this.serverMBean.getKeyPairDurationSecond());
                signatureKeyPair = em.merge(signatureKeyPair);
                this.context.getTimerService().createTimer(
                        signatureKeyPair.getHalfLife(),signatureKeyPair.getKid());
                alive.add(signatureKeyPair);
            } catch (GeneralSecurityException e) {
                throw new  OAuthOpenIDSignatureException("cannot create sign keys :", e);
            }
        }
        Collections.sort(alive);
        return alive;
    }


    /**
     * Timer that create a new oauth cert every half life time of the previous.
     * @param timer Timer
     * @throws OAuthOpenIDSignatureException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    @Timeout
    public void timeOutHandler(Timer timer) throws
            OAuthOpenIDSignatureException,
            IllegalBlockSizeException,
            InvalidKeyException,
            BadPaddingException,
            NoSuchAlgorithmException,
            NoSuchPaddingException {

        LOG.info("Cert HalfLife generate a new one " +
                timer.getInfo());
        OAuthSignatureKeyPair signatureKeyPair =  new OAuthSignatureKeyPair(
                this.securityInitializer.getAESKey(),
                CipherSecurityInitializer.CRYPT_ALG,
                CipherSecurityInitializer.SIGN_ALG,
                this.serverMBean.getKeyPairDurationSecond());
        signatureKeyPair = em.merge(signatureKeyPair);
        this.context.getTimerService().createTimer(
                signatureKeyPair.getHalfLife(),
                signatureKeyPair.getKid());
    }

    public void revokeToken(final String token) throws InvalidTokenException {
        try {
            OAuthAccessToken  accessToken =
                    findAliveAccessToken(token);
            accessToken.revoke();
            this.em.merge(accessToken);
        } catch (InvalidAccessTokenException e) {
            try {
                OAuthAuthorization authorization =
                        this.authDao.findAliveServerAuthByToken(token);
                authorization.revoke();
                this.em.merge(authorization);
            } catch (InvalidRefreshTokenException e1) {
                throw  new InvalidTokenException("invalid token");
            }
        }
    }
}
