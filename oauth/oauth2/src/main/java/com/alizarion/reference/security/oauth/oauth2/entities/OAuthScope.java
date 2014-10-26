package com.alizarion.reference.security.oauth.oauth2.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class OAuthScope implements Serializable {

    private static final long serialVersionUID = 8474629680209445237L;

    @Id
    @TableGenerator(name = "security_oauth_scope_SEQ",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            table = "sequence")
    @GeneratedValue(generator = "security_oauth_scope_SEQ",
            strategy = GenerationType.TABLE)
    private Long id;


    @Embedded
    private ScopeKey scope;

    public OAuthScope() {
    }



    public OAuthScope(final ScopeKey scope) {
        this.scope = scope;
    }


    public ScopeKey getScope() {
        return scope;
    }


    public Long getId() {
        return id;
    }


    @Override
    public String toString() {
        return "ThirdPartyRole{" +
                "role=" + scope +
                '}';
    }
}
