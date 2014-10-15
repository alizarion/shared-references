package com.alizarion.reference.security.oauth.dao;

import com.alizarion.reference.dao.jpa.JpaDao;
import com.alizarion.reference.security.oauth.entities.OAuthAccessToken;
import com.alizarion.reference.security.oauth.entities.OAuthApplication;
import com.alizarion.reference.security.oauth.entities.OAuthCredential;
import com.alizarion.reference.security.oauth.entities.client.OAuthClientAuthorization;
import com.alizarion.reference.security.oauth.entities.client.OAuthServerApplication;
import com.alizarion.reference.security.oauth.entities.server.OAuthClientApplication;
import com.alizarion.reference.security.oauth.entities.server.OAuthServerAuthorization;
import com.alizarion.reference.security.oauth.exception.*;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * @author selim@openlinux.fr.
 */
public class OAuthJpaDao extends JpaDao<Long,OAuthApplication> {


    public OAuthJpaDao(EntityManager entityManager) {
        super(entityManager);
    }


    @SuppressWarnings("unchecked")
    private OAuthApplication findOAuthApplicationByClientId(final String clientId)
            throws ClientIdNotFoundException {
        List<OAuthApplication> result = this.em.
                createNamedQuery(OAuthApplication.
                        FIND_BY_ClIENT_ID).
                setParameter("clientId", clientId).getResultList();
        if (result.isEmpty()) {
            throw new ClientIdNotFoundException(clientId);
        } else {
            return result.get(0);
        }
    }

    @SuppressWarnings("unchecked")
    private OAuthApplication findOAuthApplicationByClientIdAndRedirectURI(
            final String clientId,
            final String redirectURI)
            throws ClientIdNotFoundException {
        List<OAuthApplication> result = this.em.
                createNamedQuery(OAuthApplication.
                        FIND_BY_CLIENT_ID_REDIRECT_URI).
                setParameter("clientId", clientId)
                .setParameter("redirectURI",redirectURI).getResultList();
        if (result.isEmpty()) {
            throw new ClientIdNotFoundException(clientId);
        } else {
            return result.get(0);
        }
    }


    public OAuthServerApplication
    findOAuthServerApplicationByClientId(
            final String clientId)
            throws ClientIdNotFoundException {
        try {
            return (OAuthServerApplication)
                    findOAuthApplicationByClientId(clientId);
        } catch (ClassCastException e) {
            throw new ClientIdNotFoundException(clientId, e);

        }
    }

    public OAuthClientApplication
    findOAuthClientApplicationByClientId(
            final String clientId)
            throws ClientIdNotFoundException {
        try {
            return (OAuthClientApplication)
                    findOAuthApplicationByClientId(clientId);
        } catch (ClassCastException e) {
            throw new ClientIdNotFoundException(clientId, e);

        }
    }

    public OAuthClientApplication
    findOAuthClientApplicationByClientIdAndRedirectURI(
            final String clientId,final String redirectURI)
            throws ClientIdNotFoundException {
        try {
            return (OAuthClientApplication)
                    findOAuthApplicationByClientIdAndRedirectURI(clientId,redirectURI);
        } catch (ClassCastException e) {
            throw new ClientIdNotFoundException(clientId, e);

        }
    }


    /**
     * Method to get server oauth authorisation by code
     *
     * @param code sent to the client.
     * @return OAuthServerAuthorization
     * @throws com.alizarion.reference.security.oauth.exception.InvalidAuthCodeException the code does not exist.
     */
    @SuppressWarnings("unchecked")
    public OAuthServerAuthorization findOAuthServerAuthorizationByCode(
            final String code) throws InvalidAuthCodeException {
        List<OAuthServerAuthorization> result =
                this.em.createNamedQuery(OAuthServerAuthorization.
                        FIND_BY_AUTH_CODE).
                        setParameter("code", code).
                        getResultList();
        if (result.isEmpty()) {
            throw new InvalidAuthCodeException(code);
        } else {
            return result.get(0);
        }
    }

    /**
     * Method server authorization using an alive auth code.
     *
     * @param code sent to the client.
     * @return OAuthServerAuthorization as server authorization.
     * @throws InvalidAuthCodeException if code does not exist or it has expired.
     */
    public OAuthServerAuthorization findAliveServerAuthByCode(final String code)
            throws InvalidAuthCodeException {
        OAuthServerAuthorization result = findOAuthServerAuthorizationByCode(code);
        if (result.getAuthCode().getExpireDate().before(new Date())) {
            throw new InvalidAuthCodeException(code);
        } else {
            return result;
        }
    }

    /**
     * Method to get a server authorization by refresh token.
     *
     * @param token as String
     * @return oauth server authorization
     * @throws InvalidRefreshTokenException if no token.
     */
    @SuppressWarnings("unchecked")
    public OAuthServerAuthorization findOAuthServerAuthorizationByRefreshToken(
            final String token) throws InvalidRefreshTokenException {
        List<OAuthServerAuthorization> result =
                this.em.createNamedQuery(OAuthServerAuthorization.
                        FIND_BY_REFRESH_TOKEN).
                        setParameter("refreshToken", token).
                        getResultList();
        if (result.isEmpty()) {
            throw new InvalidRefreshTokenException(token);
        } else {
            return result.get(0);
        }
    }

    public OAuthServerAuthorization findAliveServerAuthByToken(final String token)
            throws InvalidRefreshTokenException {
        OAuthServerAuthorization authorization =
                findOAuthServerAuthorizationByRefreshToken(token);
        if (authorization.getRefreshToken().getExpireDate() != null) {
            if (authorization.getRefreshToken().
                    getExpireDate().before(new Date())) {
                throw new InvalidRefreshTokenException(token);
            }
        }
        return authorization;
    }

    @SuppressWarnings("unchecked")
    public List<OAuthServerAuthorization> findAuthByCredentialAndClientId(final Long credentialId,
                                                                          final String clientId)
    {
        return em.createNamedQuery(
                OAuthServerAuthorization.FIND_ALIVE_CREDENTIAL_CLIENT_AUTH)
                .setParameter("clientId",clientId)
                .setParameter("credentialId",credentialId)
                .getResultList();
    }


    public OAuthServerAuthorization findAliveServerAuthForCredential(final Long credentialId,
                                                                     final String clientId) {
        List<OAuthServerAuthorization> results = findAuthByCredentialAndClientId(credentialId, clientId);
        if (!results.isEmpty()) {
            for (OAuthServerAuthorization authorization : results) {
                if (authorization.isAlive()) {
                    return authorization;
                }
            }
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    public List<OAuthAccessToken> getAliveAccessToken(final Long credentialId) {
        return this.em.createNamedQuery(OAuthAccessToken.
                FIND_ALIVE_BY_CREDENTIAL).
                setParameter("credentialId", credentialId).
                setParameter("today", new Date(),
                        TemporalType.DATE).getResultList();
    }

    @SuppressWarnings("unchecked")
    public OAuthAccessToken findOAuthAccessByToken(final String token)
            throws InvalidAccessTokenException {
        List<OAuthAccessToken> result = this.em.createNamedQuery(OAuthAccessToken.
                FIND_BY_TOKEN).
                setParameter("accessToken", token).getResultList();

        if (result.isEmpty()) {
            throw new InvalidAccessTokenException(token + "not found");
        } else {
            return result.get(0);
        }
    }

    @SuppressWarnings("unchecked")
    public OAuthClientAuthorization findClientAuthByState(final String state)
            throws StateNotFoundException {
        List<OAuthClientAuthorization> result =
                this.em.createNamedQuery(OAuthClientAuthorization.
                        FIND_BY_STATE).
                        setParameter("state", state).
                        getResultList();

        if (result.isEmpty()) {
            throw new StateNotFoundException(state);
        } else {
            return result.get(0);
        }
    }

    @SuppressWarnings("unchecked")
    public OAuthClientAuthorization findAliveRefreshToken(final Long clientId)
            throws InvalidRefreshTokenException {
        List<OAuthClientAuthorization> result =
                this.em.createNamedQuery(
                        OAuthClientAuthorization.
                                FIND_REFRESH_TOKEN).
                        setParameter("clientId", clientId).
                        setParameter("today", new Date(), TemporalType.DATE).
                        getResultList();
        if (result.isEmpty()) {
            throw new InvalidRefreshTokenException(" found for credentialId " + clientId);
        } else {
            return result.get(0);
        }

    }

    @SuppressWarnings("unchecked")
    public OAuthCredential findOAuthCredentialByUsername(final String username)
            throws BadCredentialException {
        List<OAuthCredential> result = this.em.createNamedQuery(
                OAuthCredential.FIND_BY_USERNAME_NAMED_QUERY)
                .setParameter("username", username)
                .getResultList();

        if (result.isEmpty()){
            throw new BadCredentialException(username);
        }   else {
            return result.get(0);
        }
    }

}