package com.alizarion.reference.security.entities.oauth.server;

import com.alizarion.reference.security.entities.Credential;
import com.alizarion.reference.security.entities.CredentialRole;
import com.alizarion.reference.security.entities.Token;
import com.alizarion.reference.security.exception.OAuthException;
import com.alizarion.reference.security.tools.SecurityHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_oauth_authorization")
public class OAuthAuthorization implements Serializable {

    private static final long serialVersionUID = 1615612298298934691L;

    @ManyToOne(optional = false)
    private OAuthApplication authApplication;

    @ManyToMany
    private Set<CredentialRole> scopes = new HashSet<>();

    @OneToMany(cascade = {CascadeType.REMOVE,
            CascadeType.PERSIST,
            CascadeType.MERGE},
            fetch = FetchType.EAGER)
    private Set<OAuthAccessToken> accessTokens = new HashSet<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "creationDate",
                    column = @Column(name = "refresh_token_creation_date")),
            @AttributeOverride(name = "value",
                    column = @Column(name = "refresh_token_value")),
            @AttributeOverride(name = "expireDate",
                    column = @Column(name = "refresh_token_expire_date"))
    })
    private Token refreshToken;


    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "creationDate",
                    column = @Column(name = "code_creation_date")),
            @AttributeOverride(name = "value",
                    column = @Column(name = "code_value")),
            @AttributeOverride(name = "expireDate",
                    column = @Column(name = "code_expire_date"))
    })
    private Token authCode;

    public OAuthAuthorization(){

    }

    public OAuthAuthorization(final long refreshTokenDuration,
                              final OAuthApplication authApplication,
                              final Set<CredentialRole> roles) {
        this.authCode = new Token(300,
                SecurityHelper.
                        getRandomAlphaNumericString(150));
        this.scopes = roles;
        this.authApplication = authApplication;
        this.refreshToken = new Token(refreshTokenDuration,
                SecurityHelper.getRandomAlphaNumericString(300));
    }

    public Set<CredentialRole> getScopes() {
        return scopes;
    }


    public Token getRefreshToken() {
        return refreshToken;
    }

    public Credential getCredential() throws OAuthException {
        if (!this.scopes.isEmpty()){
            return new ArrayList<>(this.scopes).get(0).getCredential();
        }
        throw new OAuthException("no scope/credentialRole " +
                "associated with this OAuthAuthorization :"+this);
    }

    public Token getAuthCode() {
        return authCode;
    }

    public OAuthApplication getAuthApplication() {
        return authApplication;
    }

    public OAuthAccessToken getNewAccessToken(final long duration){
        this.revokeAccess();
        OAuthAccessToken accessToken =  new OAuthAccessToken(duration,this);
        this.accessTokens.add(accessToken);
        return accessToken;
    }

    public void revoke(){
        this.refreshToken.revoke();
        this.authCode.revoke();
        this.revokeAccess();

    }

    protected void revokeAccess(){
        for (OAuthAccessToken token : this.accessTokens){
            if (token.getToken().isValidToken()){
                token.getToken().revoke();
            }
        }
    }

    @PrePersist
    public void generateCode(){
        this.authCode = new Token(300,
                SecurityHelper.
                        getRandomAlphaNumericString(150));
    }

    public Set<OAuthAccessToken> getAccessTokens() {
        return accessTokens;
    }

    @Override
    public String toString() {
        return "OAuthAuthorization{" +
                "authApplication=" + authApplication +
                ", scopes=" + scopes +
                ", refreshToken=" + refreshToken +
                ", authCode=" + authCode +
                '}';
    }
}
