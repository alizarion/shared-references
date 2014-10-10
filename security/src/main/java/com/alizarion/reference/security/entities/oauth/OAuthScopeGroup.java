package com.alizarion.reference.security.entities.oauth;

import com.alizarion.reference.security.entities.RoleGroupKey;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class OAuthScopeGroup implements Serializable {

    private static final long serialVersionUID = 688317503522425540L;

    @Id
    @TableGenerator(
            name = "security_oauth_scope_group_SEQ",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT"
    )
    @GeneratedValue(
            generator = "security_oauth_scope_group_SEQ",
            strategy = GenerationType.TABLE
    )
    private Long id;

    @Embedded
    private RoleGroupKey group;


    public OAuthScopeGroup() {

    }

    public OAuthScopeGroup(final RoleGroupKey group) {
        this.group = group;
    }

    public RoleGroupKey getGroup() {
        return group;
    }

    public abstract Set<String> getGroupKeys();

    public Long getId() {
        return id;
    }
}
