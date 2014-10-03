package com.alizarion.reference.security.entities.oauth.server;

import com.alizarion.reference.security.entities.CredentialRole;
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
@Table(name = "security_oauth_client_authorization")
@NamedQueries({@NamedQuery(
        name = OAuthServerAuthorization.FIND_BY_AUTH_CODE,
        query = "select sAuth from OAuthServerAuthorization " +
                "sAuth where sAuth.authCode.value = :code "),
        @NamedQuery(
                name = OAuthServerAuthorization.FIND_BY_REFRESH_TOKEN,
                query = "select sAuth from OAuthServerAuthorization sAuth" +
                        " where sAuth.refreshToken.value = :refreshToken")})
public class OAuthServerAuthorization extends OAuthAuthorization {

    private static final long serialVersionUID = 6324216375307560864L;

    public static final String FIND_BY_AUTH_CODE =
            "OAuthServerAuthorization.FIND_BY_VALID_AUTH_CODE";

    public static final String FIND_BY_REFRESH_TOKEN =
            "OAuthServerAuthorization.FIND_BY_REFRESH_TOKEN";

    @ManyToMany
    private Set<CredentialRole> scopes = new HashSet<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "creationDate",
                    column = @Column(name = "code_creation_date"
                            ,nullable = false)),
            @AttributeOverride(name = "value",
                    column = @Column(name = "code_value",
                            nullable = false)),
            @AttributeOverride(name = "expireDate",
                    column = @Column(name = "code_expire_date",
                            nullable = false))
    })
    private Token authCode;

    public OAuthServerAuthorization(){

    }

    public OAuthServerAuthorization(final OAuthApplication authApplication,
                                    final Set<CredentialRole> roles) {
        this.authCode = new Token(300,
                SecurityHelper.
                        getRandomAlphaNumericString(150));
        this.scopes = roles;
        this.authApplication = authApplication;

    }


    public Set<CredentialRole> getScopes() {
        return scopes;
    }




    public OAuthAccessToken getNewAccessToken(
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
                "scopes=" + scopes +
                ", authCode=" + authCode +
                '}';
    }
}
