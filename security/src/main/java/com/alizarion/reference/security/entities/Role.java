package com.alizarion.reference.security.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * In application users Roles
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_roles")
public class Role {

    @ManyToOne
    private CredentialRole credentialRole;

    @EmbeddedId
    private RoleKey roleKey;

    @ManyToMany
    private Set<RoleGroup> group = new HashSet<>();

    public Role() {
    }

    public Role(final CredentialRole credentialRole,
                final RoleKey role) {
        this.credentialRole = credentialRole;
        this.roleKey = role;
    }

    public Role(final CredentialRole credentialRole,
                final RoleKey role,
                final Set<RoleGroup> group) {
        this.credentialRole = credentialRole;
        this.roleKey = role;
        this.group = group;
    }

    public Role(RoleKey role) {
        this.roleKey = role;
    }

    public RoleKey getRoleKey() {
        return roleKey;
    }

    public CredentialRole getCredentialRole() {
        return credentialRole;
    }

    public Set<RoleGroup> getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Role)) return false;

        Role role1 = (Role) o;

        return !(credentialRole != null ?
                !credentialRole.equals(role1.credentialRole) :
                role1.credentialRole != null) &&
                !(group != null ? !group.equals(role1.group) :
                        role1.group != null) &&
                !(roleKey != null ? !roleKey.equals(role1.roleKey) :
                        role1.roleKey != null);

    }

    @Override
    public int hashCode() {
        int result = credentialRole != null ? credentialRole.hashCode() : 0;
        result = 31 * result + (roleKey != null ? roleKey.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        return result;
    }
}
