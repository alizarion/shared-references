package com.alizarion.reference.security.oauth.entities.server;

import com.alizarion.reference.security.oauth.entities.*;
import com.alizarion.reference.security.oauth.toolkit.OAuthHelper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_oauth_server_authorization")
@NamedQueries({
        @NamedQuery(
                name = OAuthServerAuthorization.FIND_BY_AUTH_CODE,
                query = "select sAuth from OAuthServerAuthorization " +
                        "sAuth where sAuth.authCode.value = :code "),
        @NamedQuery(
                name = OAuthServerAuthorization.FIND_BY_REFRESH_TOKEN,
                query = "select sAuth from OAuthServerAuthorization sAuth" +
                        " where sAuth.refreshToken.value = :refreshToken"),
        @NamedQuery(
                name = OAuthServerAuthorization.FIND_ALIVE_CREDENTIAL_CLIENT_AUTH,
                query = "select sAuth from OAuthServerAuthorization sAuth" +
                        " where sAuth.credential.id = :credentialId " +
                        "and sAuth.authApplication.applicationKey.clientId = :clientId ")})
public class OAuthServerAuthorization extends OAuthAuthorization<OAuthScopeServer> {

    private static final long serialVersionUID = 6324216375307560864L;

    public static final String FIND_BY_AUTH_CODE =
            "OAuthServerAuthorization.FIND_BY_VALID_AUTH_CODE";

    public static final String FIND_BY_REFRESH_TOKEN =
            "OAuthServerAuthorization.FIND_BY_REFRESH_TOKEN";

    public static final String FIND_ALIVE_CREDENTIAL_CLIENT_AUTH =
            "OAuthServerAuthorization.FIND_ALIVE_CREDENTIAL_CLIENT_AUTH";

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "security_oauth_server_authorization_scopes",
            joinColumns =
            @JoinColumn(name = "server_authorization_id",
                    referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "server_scope_id",
                    referencedColumnName = "id"))
    private Set<OAuthScopeServer> scopes = new HashSet<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "creationDate",
                    column = @Column(name = "code_creation_date"
                            ,nullable = true)),
            @AttributeOverride(name = "value",
                    column = @Column(name = "code_value",
                            nullable = true)),
            @AttributeOverride(name = "expireDate",
                    column = @Column(name = "code_expire_date",
                            nullable = true))
    })
    private Token authCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "application_id")
    protected OAuthClientApplication authApplication;

    public OAuthServerAuthorization(){

    }

    @Override
    public OAuthApplication getAuthApplication() {
        return this.authApplication;
    }

    public OAuthServerAuthorization(final OAuthClientApplication authApplication,
                                    final OAuthCredential credential,
                                    final Set<OAuthScopeServer> scopes) {

        this.credential =  credential;
        this.authApplication = authApplication;
        this.scopes.addAll(scopes);

    }

    public void generateCode(){
        this.authCode = new Token(300,
                OAuthHelper.
                        getRandomAlphaNumericString(150));
    }

    @Override
    public Set<OAuthScopeServer> getScopes() {
        return this.scopes;
    }

    public void setScopes(Set<OAuthScopeServer> scopes) {
        this.scopes = scopes;
    }

    @Override
    public List<OAuthScopeServer> getScopesAsList() {
        return new ArrayList<>(this.scopes);
    }


    @Override
    public Set<String> getScopeKeys()  {
        Set<String> scopeKeys = new HashSet<>();
        for (OAuthScopeServer scopeServer :  this.scopes){
            scopeKeys.add(scopeServer.getScope().getKey());
        }
        return scopeKeys;
    }

    public OAuthAccessToken addAccessToken(
            final long duration){
        this.revokeAccess();
        OAuthAccessToken accessToken =
                new OAuthAccessToken(
                        new Token(
                                duration,
                                OAuthHelper.
                                        getRandomAlphaNumericString(300)),this);
        this.accessTokens.add(accessToken);
        return accessToken;
    }

    public Token generateRefreshToken(final Long duration){
        this.refreshToken = new Token(
                duration,
                OAuthHelper.getRandomAlphaNumericString(300));

        return this.refreshToken;
    }

    public void revoke(){
        this.refreshToken.revoke();
        this.authCode.revoke();
        this.revokeAccess();

    }

    public boolean isAlive() {
        if (this.refreshToken != null) {
            return this.refreshToken.isAlive();
        } else {
            for (OAuthAccessToken accessToken : this.accessTokens) {
                if (accessToken.getBearer().isAlive()) {
                    return true;
                }
            }
            return false;
        }
    }



    public Token getAuthCode() {
        return authCode;
    }

    @Override
    public String toString() {
        return "OAuthClientAuthorization{" +
                ", authCode=" + authCode +
                '}';
    }
}