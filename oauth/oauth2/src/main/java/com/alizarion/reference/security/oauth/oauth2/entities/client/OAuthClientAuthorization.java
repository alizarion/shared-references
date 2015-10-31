package com.alizarion.reference.security.oauth.oauth2.entities.client;

import com.alizarion.reference.security.oauth.oauth2.entities.*;
import com.alizarion.reference.security.oauth.oauth2.OAuthHelper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_oauth_client_authorization")
@NamedQueries({@NamedQuery(name = OAuthClientAuthorization.FIND_BY_STATE,
        query = "select ca from OAuthClientAuthorization " +
                "ca where ca.state.value = :state"),
        @NamedQuery(name = OAuthClientAuthorization.FIND_REFRESH_TOKEN,
                query = "select ca from OAuthClientAuthorization ca where " +
                        "ca.credential.id = :credentialId and " +
                        "(ca.refreshToken.expireDate is null" +
                        " or ca.refreshToken.expireDate > :today)")})
public class OAuthClientAuthorization extends OAuthAuthorization<OAuthScopeClient> {

    private static final long serialVersionUID = -5782746473189901608L;

    public final static String FIND_BY_STATE =
            "OAuthClientAuthorization.FIND_BY_STATE";

    public final static String FIND_REFRESH_TOKEN =
            "OAuthClientAuthorization.FIND_REFRESH_TOKEN";


    @ManyToMany
    @JoinTable(name = "security_oauth_client_authorization_oauth_scope_client",
            joinColumns =
            @JoinColumn(name ="oauth_server_authorization_id",
                    referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "security_oauth_scope_client",
                    referencedColumnName = "id"))
    private Set<OAuthScopeClient> scopes = new HashSet<>();

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "application_id")
    protected OAuthServerApplication authApplication;


    public OAuthClientAuthorization(final OAuthServerApplication authApplication,
                                    OAuthCredential credential) {
        this.authApplication = authApplication;
        this.state = new Token(300,
                OAuthHelper.
                        getRandomAlphaNumericString(150));
        this.credential = credential;
    }

    public OAuthClientAuthorization(final OAuthServerApplication authApplication,
                                    final OAuthCredential credential,
                                    final Set<OAuthScopeClient> scopes) {
        this.authApplication = authApplication;
        this.state = new Token(300,
                OAuthHelper.
                        getRandomAlphaNumericString(150));
        this.credential = credential;
        this.scopes.addAll(scopes);
    }

    public OAuthClientAuthorization() {
    }

    @Override
    public OAuthApplication getAuthApplication() {
        return this.authApplication;
    }

    public Set<OAuthScopeClient> getScopes() {
        return scopes;
    }

    @Override
    public List<OAuthScopeClient> getScopesAsList() {
        return new ArrayList<>(this.scopes);
    }

    @Override
    public Set<String> getScopeKeys() {
        Set<String> scopeKeys = new HashSet<>();
        for (OAuthScopeClient scopeClient :  this.scopes){
            scopeKeys.add(scopeClient.getScope().getKey()) ;
        }
        return scopeKeys;
    }

    @Override
    public void revoke() {
        this.refreshToken.revoke();
        this.state.revoke();
    }

    public OAuthAccessToken addAccessToken(final long duration,
                                           final String tokenValue){
        OAuthAccessToken accessToken =
                new OAuthAccessToken(
                        new Token(duration,tokenValue),this);
        this.accessTokens.add(accessToken);
        return accessToken;
    }

    public Token getState() {
        return state;
    }

    public void setState(Token state) {
        this.state = state;
    }
}
