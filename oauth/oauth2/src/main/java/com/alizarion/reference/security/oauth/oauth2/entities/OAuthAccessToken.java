package com.alizarion.reference.security.oauth.oauth2.entities;


import javax.persistence.*;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_oauth_access_token")
@NamedQueries({@NamedQuery(name =
        OAuthAccessToken.FIND_ALIVE_BY_CREDENTIAL,
        query = "select ot from OAuthAccessToken ot where " +
                "ot.authorization.credential.id = :credentialId and" +
                " ot.bearer.expireDate > :today "),
        @NamedQuery(name = OAuthAccessToken.FIND_BY_TOKEN,
                query = "select ot from OAuthAccessToken ot" +
                        " where ot.bearer.value = :accessToken")})
public class OAuthAccessToken implements Serializable {


    private static final long serialVersionUID = -7896930421192239848L;


    public static final String FIND_ALIVE_BY_CREDENTIAL = "OAuthAccessToken.FIND_ALIVE_BY_CREDENTIAL";

    public static final String FIND_BY_TOKEN = "OAuthAccessToken.FIND_BY_TOKEN";

    @Id
    @TableGenerator(name = "security_oauth_access_token_SEQ",
            table = "sequence",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT")
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "security_oauth_access_token_SEQ")
    private Long id;

    @Embedded
    private Token bearer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "oauth_authorization_id")
    private OAuthAuthorization authorization;




    public OAuthAccessToken() {

    }


    public OAuthAccessToken(Token token,final OAuthAuthorization parent) {
        this.authorization = parent;
        this.bearer =  token;
    }

    public Long getId() {
        return id;
    }

    public Token getBearer() {
        return bearer;
    }

    public OAuthAuthorization getAuthorization() {
        return authorization;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OAuthAccessToken)) return false;

        OAuthAccessToken that = (OAuthAccessToken) o;

        return !(authorization != null ?
                !authorization.equals(that.authorization)
                : that.authorization != null) &&
                !(bearer != null ? !bearer.equals(that.bearer) :
                        that.bearer != null);

    }

    public void revoke(){
        this.bearer.revoke();
       // this.authorization.revoke();
    }

    @Override
    public int hashCode() {
        int result = bearer != null ? bearer.hashCode() : 0;
        result = 31 * result + (authorization != null ?
                authorization.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OAuthAccessToken{" +
                "token=" + bearer +
                ", authorization=" + authorization +
                '}';
    }
}
