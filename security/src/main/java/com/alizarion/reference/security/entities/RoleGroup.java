package com.alizarion.reference.security.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * group to grant many role.
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_role_group")
public class RoleGroup implements Serializable {

    @EmbeddedId
    private RoleGroupKey group;

    @ManyToMany
    private Set<Role> roles = new HashSet<>();


    public RoleGroup() {
    }

    public RoleGroup(final RoleGroupKey group,
                     final Set<Role> roles) {
        this.group = group;
        this.roles = roles;
    }

    public RoleGroupKey getGroup() {
        return group;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RoleGroup)) return false;

        RoleGroup roleGroup = (RoleGroup) o;

        return !(group != null ?
                !group.equals(roleGroup.group) :
                roleGroup.group != null) && !(roles != null ?
                !roles.equals(roleGroup.roles) :
                roleGroup.roles != null);

    }

    public Set<RoleKey> getKeys(){
        Set<RoleKey> keys = new HashSet<>();
        for (Role role  :  this.roles){
            keys.add(role.getRoleKey());
        }
        return keys;
    }

    @Override
    public int hashCode() {
        int result = group != null ? group.hashCode() : 0;
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        return result;
    }
}
