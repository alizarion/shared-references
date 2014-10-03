package com.alizarion.reference.security.entities.oauth.client;

import com.alizarion.reference.security.entities.RoleKey;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_oauth_scope")
public class OAuthScope implements Serializable {

    private static final long serialVersionUID = 8474629680209445237L;

    @EmbeddedId
    private RoleKey role;

    public OAuthScope() {
    }



    public OAuthScope(final RoleKey role) {
        this.role = role;
    }


    public RoleKey getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "ThirdPartyRole{" +
                "role=" + role +
                '}';
    }
}
