package com.alizarion.reference.security.oauth.oauth2.entities;



import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NamedQueries(
        @NamedQuery(name = OAuthAuthorization.FIND_BEST_ACCESS_TOKEN,
                query = "select at from OAuthAccessToken  " +
                        "at where at.bearer.expireDate > " +
                        ":today  and at.authorization.id = :authID " +
                        "order by at.bearer.expireDate desc "))
public abstract class OAuthAuthorization<T extends OAuthScope> implements Serializable {

    private static final long serialVersionUID = 1615612298298934691L;

    public static final String FIND_BEST_ACCESS_TOKEN = "OAuthAuthorization.FIND_BEST_ACCESS_TOKEN";

    @Id
    @TableGenerator(
            name = "security_oauth_authorization_SEQ",
            table = "sequence",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT")
    @GeneratedValue(
            generator ="security_oauth_authorization_SEQ",
            strategy = GenerationType.TABLE)
    private Long id;



    @ManyToOne(optional = false)
    @JoinColumn(name = "credential_id")
    protected OAuthCredential credential;

    @OneToMany(cascade = {CascadeType.REMOVE,
            CascadeType.PERSIST,
            CascadeType.MERGE},
            mappedBy = "authorization",
            fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA)
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

    public boolean isPermanent(){
        if (this.refreshToken != null){
            if (this.refreshToken.isAlive()){
                return true;
            }
        }
        return false;
    }


    public Token getRefreshToken() {
        return refreshToken;
    }

    public OAuthCredential getCredential() {
        return credential;
    }

    public void setCredential(OAuthCredential credential) {
        this.credential = credential;
    }

    public abstract OAuthApplication getAuthApplication();

    public Long getId() {
        return id;
    }

    public abstract Set<T> getScopes();

    public abstract List<T> getScopesAsList();

    public abstract Set<String> getScopeKeys();

    public void setRefreshToken(Token refreshToken) {
        this.refreshToken = refreshToken;
    }

    public abstract void revoke();


    public Set<OAuthAccessToken> getAccessTokens() {
        return accessTokens;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OAuthAuthorization)) return false;

        OAuthAuthorization that = (OAuthAuthorization) o;

        return !(id != null ? !id.equals(that.id) : that.id != null)
                && !(refreshToken != null ?
                !refreshToken.equals(that.refreshToken) :
                that.refreshToken != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (refreshToken != null ? refreshToken.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OAuthAuthorization{" +
                "id=" + id +
                ", credential=" + credential +
                ", accessTokens=" + accessTokens.size() +
                ", refreshToken=" + refreshToken +
                '}';
    }
}
