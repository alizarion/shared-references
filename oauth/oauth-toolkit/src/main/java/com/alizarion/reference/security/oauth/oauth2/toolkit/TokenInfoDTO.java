package com.alizarion.reference.security.oauth.oauth2.toolkit;

import com.alizarion.reference.security.oauth.oauth2.entities.OAuthAccessToken;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TokenInfoDTO implements Serializable {

    private static final long serialVersionUID = -1825702016687930812L;

    @XmlElement(name = "audience")
    private String audience;

    @XmlElement(name = "expire_in")
    private String expireIn;

    @XmlElement(name = "scope")
    private Set<String> scope;

    public TokenInfoDTO() {


      }

    @SuppressWarnings("unchecked")
    public TokenInfoDTO(OAuthAccessToken accessToken) {
        this.audience =  accessToken.
                getAuthorization().
                getAuthApplication().
                getApplicationKey().
                getClientId();

        this.expireIn = accessToken.getBearer().expireIn().toString();

        this.scope = accessToken.getAuthorization().getScopeKeys();

    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(String expireIn) {
        this.expireIn = expireIn;
    }

    public Set<String> getScope() {
        return scope;
    }

    public void setScope(Set<String> scope) {
        this.scope = scope;
    }
}
