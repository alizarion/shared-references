package com.alizarion.reference.security.entities.oauth.server;

import com.alizarion.reference.security.entities.Token;
import com.alizarion.reference.security.entities.oauth.OAuthAccessToken;
import com.alizarion.reference.security.entities.oauth.OAuthApplication;
import com.alizarion.reference.security.entities.oauth.OAuthAuthorization;
import com.alizarion.reference.security.entities.oauth.OAuthCredential;
import com.alizarion.reference.security.tools.SecurityHelper;

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
@NamedQueries({@NamedQuery(
        name = OAuthServerAuthorization.FIND_BY_AUTH_CODE,
        query = "select sAuth from OAuthServerAuthorization " +
                "sAuth where sAuth.authCode.value = :code "),
        @NamedQuery(
                name = OAuthServerAuthorization.FIND_BY_REFRESH_TOKEN,
                query = "select sAuth from OAuthServerAuthorization sAuth" +
                        " where sAuth.refreshToken.value = :refreshToken")})
public class OAuthServerAuthorization extends OAuthAuthorization<OAuthScopeServer> {

    private static final long serialVersionUID = 6324216375307560864L;

    public static final String FIND_BY_AUTH_CODE =
            "OAuthServerAuthorization.FIND_BY_VALID_AUTH_CODE";

    public static final String FIND_BY_REFRESH_TOKEN =
            "OAuthServerAuthorization.FIND_BY_REFRESH_TOKEN";

    @ManyToMany
    @JoinTable(name = "security_oauth_server_authorization_scopes",
            joinColumns =
            @JoinColumn(name = "server_authorization_id",
                    referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "server_scope_id",
                    referencedColumnName = "id")
    )
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

    public OAuthServerAuthorization(){

    }

    public OAuthServerAuthorization(final OAuthApplication authApplication,
                                    final OAuthCredential credential,
                                    final Set<OAuthScopeServer> scopes) {

        this.credential =  credential;
        this.authApplication = authApplication;
        this.scopes.addAll(scopes);

    }

    public void generateCode(){
        this.authCode = new Token(300,
                SecurityHelper.
                        getRandomAlphaNumericString(150));
    }

    @Override
    public Set<OAuthScopeServer> getScopes() {
        return this.scopes;
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
                                SecurityHelper.
                                        getRandomAlphaNumericString(300)),this);
        this.accessTokens.add(accessToken);
        return accessToken;
    }

    public Token generateRefreshToken(final Long duration){
        this.refreshToken = new Token(
                duration,
                SecurityHelper.getRandomAlphaNumericString(300));

        return this.refreshToken;
    }

    public void revoke(){
        this.refreshToken.revoke();
        this.authCode.revoke();
        this.revokeAccess();

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
