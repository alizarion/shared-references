package com.alizarion.reference.security.entities.oauth.server;

import com.alizarion.reference.security.entities.Token;
import com.alizarion.reference.security.tools.SecurityHelper;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */

@Entity
@Table(name = "security_oauth_access_token")
public class OAuthAccessToken implements Serializable {


    private static final long serialVersionUID = -7896930421192239848L;

    public final static String FIND_CREDENTIAL = "OAuthAccessToken.FIND_CREDENTIAL";

    @EmbeddedId
    private Token token;

    @ManyToOne(optional = false)
    private OAuthAuthorization authorization;

    public OAuthAccessToken() {

    }


    public OAuthAccessToken(final long duration,
                            final OAuthAuthorization authorization) {
        this.authorization = authorization;
        this.token =  new Token(duration,
                SecurityHelper.getRandomAlphaNumericString(300));
    }


    public Token getToken() {
        return token;
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
                !(token != null ? !token.equals(that.token) :
                        that.token != null);

    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (authorization != null ?
                authorization.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OAuthAccessToken{" +
                "token=" + token +
                ", authorization=" + authorization +
                '}';
    }
}
