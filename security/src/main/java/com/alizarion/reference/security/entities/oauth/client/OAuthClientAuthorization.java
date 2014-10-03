package com.alizarion.reference.security.entities.oauth.client;

import com.alizarion.reference.security.entities.Credential;
import com.alizarion.reference.security.entities.Token;
import com.alizarion.reference.security.entities.oauth.OAuthAccessToken;
import com.alizarion.reference.security.entities.oauth.OAuthApplication;
import com.alizarion.reference.security.entities.oauth.OAuthAuthorization;
import com.alizarion.reference.security.tools.SecurityHelper;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_oauth_server_authorization")
@NamedQueries({@NamedQuery(name = OAuthClientAuthorization.FIND_BY_STATE,
        query = "select ca from OAuthClientAuthorization " +
                "ca where ca.state.value = :state"),
        @NamedQuery(name = OAuthClientAuthorization.FIND_REFRESH_TOKEN,
                query = "select ca from OAuthClientAuthorization ca where " +
                        "ca.credential.id = :credentialId and " +
                        "(ca.refreshToken.expireDate is null" +
                        " or ca.refreshToken.expireDate > :today)")})
public class OAuthClientAuthorization extends OAuthAuthorization {

    private static final long serialVersionUID = -5782746473189901608L;

    public final static String FIND_BY_STATE =
            "OAuthClientAuthorization.FIND_BY_STATE";

    public final static String FIND_REFRESH_TOKEN =
            "OAuthClientAuthorization.FIND_REFRESH_TOKEN";


    @ManyToMany
    private Set<OAuthScope> scopes = new HashSet<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "creationDate",
                    column = @Column(name = "state_creation_date")),
            @AttributeOverride(name = "value",
                    column = @Column(name = "state_value")),
            @AttributeOverride(name = "expireDate",
                    column = @Column(name = "state_expire_date"))
    })
    private Token state;


    public OAuthClientAuthorization(final OAuthApplication authApplication,
                                    Credential credential) {
        super(authApplication);
        this.state = new Token(300,
                SecurityHelper.
                        getRandomAlphaNumericString(150));
        this.credential = credential;
    }

    public OAuthClientAuthorization(final OAuthApplication authApplication,
                                    final Credential credential,
                                    final Set<OAuthScope> scopes) {
        super(authApplication);
        this.state = new Token(300,
                SecurityHelper.
                        getRandomAlphaNumericString(150));
        this.credential = credential;
        this.scopes.addAll(scopes);
    }

    public OAuthClientAuthorization() {
    }

    public Set<OAuthScope> getScopes() {
        return scopes;
    }


    @Override
    public void revoke() {
        this.refreshToken.revoke();
        this.state.revoke();
        revokeAccess();
    }

    public OAuthAccessToken addAccessToken(final long duration,
                                           final String tokenValue){
        this.revokeAccess();
        OAuthAccessToken accessToken =
                new OAuthAccessToken(
                        new Token(duration,tokenValue),this);
        this.accessTokens.add(accessToken);
        return accessToken;
    }

}
