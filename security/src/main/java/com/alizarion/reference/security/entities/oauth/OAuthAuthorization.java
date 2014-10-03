package com.alizarion.reference.security.entities.oauth;

import com.alizarion.reference.security.entities.Credential;
import com.alizarion.reference.security.entities.Token;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class OAuthAuthorization implements Serializable {

    private static final long serialVersionUID = 1615612298298934691L;


    @Id
    @TableGenerator(
            name = "security_oauth_client_authorization_SEQ",
            table = "sequence",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT")
    @GeneratedValue(
            generator ="security_oauth_client_authorization_SEQ",
            strategy = GenerationType.TABLE)
    private Long id;

    @ManyToOne(optional = false)
    protected OAuthApplication authApplication;

    @ManyToOne(optional = false)
    protected Credential credential;

    @OneToMany(cascade = {CascadeType.REMOVE,
            CascadeType.PERSIST,
            CascadeType.MERGE},
            fetch = FetchType.EAGER)
    protected Set<OAuthAccessToken> accessTokens = new HashSet<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "creationDate",
                    column = @Column(name = "refresh_token_creation_date",
                            nullable = true)),
            @AttributeOverride(name = "value",
                    column = @Column(name = "refresh_token_value",
                            nullable = true)),
            @AttributeOverride(name = "expireDate",
                    column = @Column(name = "refresh_token_expire_date",
                            nullable = true))
    })
    protected Token refreshToken;

    public OAuthAuthorization(){

    }

    public OAuthAuthorization(final OAuthApplication authApplication) {

        this.authApplication = authApplication;
    }

    public Token getRefreshToken() {
        return refreshToken;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public OAuthApplication getAuthApplication() {
        return authApplication;
    }

    public Long getId() {
        return id;
    }


    public void setRefreshToken(Token refreshToken) {
        this.refreshToken = refreshToken;
    }

    public abstract void revoke();

    protected void revokeAccess(){
        for (OAuthAccessToken token : this.accessTokens){
            if (token.getBearer().isValidToken()){
                token.getBearer().revoke();
            }
        }
    }


    public Set<OAuthAccessToken> getAccessTokens() {
        return accessTokens;
    }


    @Override
    public String toString() {
        return "OAuthAuthorization{" +
                "authApplication=" + authApplication +
                ", accessTokens=" + accessTokens +
                ", refreshToken=" + refreshToken +
                '}';
    }
}
