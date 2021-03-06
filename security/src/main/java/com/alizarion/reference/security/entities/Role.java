package com.alizarion.reference.security.entities;


import com.alizarion.reference.security.oauth.oauth2.entities.OAuthRole;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * In application users Roles
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_roles")
@NamedQueries({
        @NamedQuery(name = Role.FIND_BY_KEY,query = "select role from" +
                " Role role where role.roleKey.key = :roleKey ")
})
public class Role implements OAuthRole {

    private static final long serialVersionUID = 3487799026854810752L;

    public static final String FIND_BY_KEY = "FIND_BY_KEY";

    @Id
    @TableGenerator(name="security_roles_SEQ",
            table="sequence",
            pkColumnName="SEQ_NAME",
            valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy= GenerationType.TABLE,
            generator="security_roles_SEQ")
    @Column
    private Long id;

    @OneToMany(mappedBy = "role")
    private Set<CredentialRole> credentialRoles = new HashSet<>();

    @Embedded
    private RoleKey roleKey;

    @ManyToMany
    private Set<RoleGroup> group = new HashSet<>();

    public Role() {
    }

    public Role( final RoleKey role) {
        this.roleKey = role;
    }

    public Role(
            final RoleKey role,
            final Set<RoleGroup> group) {
        this.roleKey = role;
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    public RoleKey getRoleKey() {
        return roleKey;
    }

    public Set<RoleGroup> getGroup() {
        return group;
    }


    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getRoleId() {
        return this.getRoleKey().getKey();
    }



    public Set<CredentialRole> getCredentialRoles() {
        return credentialRoles;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Role)) return false;

        Role role = (Role) o;

        return !(roleKey != null ?
                !roleKey.equals(role.roleKey) :
                role.roleKey != null);

    }

    @Override
    public int hashCode() {
        return roleKey != null ? roleKey.hashCode() : 0;
    }
}
