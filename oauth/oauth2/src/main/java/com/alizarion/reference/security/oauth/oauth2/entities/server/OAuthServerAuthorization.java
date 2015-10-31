package com.alizarion.reference.security.oauth.oauth2.entities.server;

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
                query = "select sAuth from OAuthServerAuthorization sAuth " +
                        " where cast(sAuth.credential.id as string) = :credentialId " +
                        "and  sAuth.authApplication.applicationKey.clientId = :clientId " +
                        "and ((sAuth.refreshToken is not null" +
                        " and (sAuth.refreshToken.expireDate is null or " +
                        "sAuth.refreshToken.expireDate > :today))  or " +
                        "((select count(*) from OAuthAccessToken as at where sAuth.id = " +
                        "at.authorization.id and at.bearer.expireDate > :today)>0))")})
public class OAuthServerAuthorization extends OAuthAuthorization<OAuthScopeServer> {

    private static final long serialVersionUID = 6324216375307560864L;

    public static final String FIND_BY_AUTH_CODE =
            "OAuthServerAuthorization.FIND_BY_VALID_AUTH_CODE";

    public static final String FIND_BY_REFRESH_TOKEN =
            "OAuthServerAuthorization.FIND_BY_REFRESH_TOKEN";

    public static final String FIND_ALIVE_CREDENTIAL_CLIENT_AUTH =
            "OAuthServerAuthorization.FIND_ALIVE_CREDENTIAL_CLIENT_AUTH";

    @Transient
    private boolean promptRequired ;

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
        this.promptRequired =  true;
        this.credential =  credential;
        this.authApplication = authApplication;
        this.scopes.addAll(scopes);

    }

    public boolean isPromptRequired() {
        return promptRequired;
    }

    public void setPromptRequired(boolean promptRequired) {
        this.promptRequired = promptRequired;
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
        if (!this.scopes.equals(scopes) || !scopes.containsAll(this.scopes)) {
            this.promptRequired = true;
            this.scopes = scopes;
        }
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
        this.promptRequired = true;
        return this.refreshToken;
    }

    public void revoke(){
        this.promptRequired =  true;
        if (this.refreshToken != null){
            this.refreshToken.revoke();
        }
        if(this.authCode!=null){
            this.authCode.revoke();
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
