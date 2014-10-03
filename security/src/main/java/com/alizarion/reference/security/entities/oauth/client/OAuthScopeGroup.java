package com.alizarion.reference.security.entities.oauth.client;

import com.alizarion.reference.security.entities.RoleGroupKey;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_oauth_scope_group")
public class OAuthScopeGroup implements Serializable {

    private static final long serialVersionUID = 688317503522425540L;

    @EmbeddedId
    private RoleGroupKey group;

    @ManyToMany
    private Set<OAuthScope> scopes;

    public OAuthScopeGroup() {

        }

    public OAuthScopeGroup(final RoleGroupKey group,
                           final Set<OAuthScope> scopes) {
        this.group = group;
        this.scopes = scopes;
    }

    public RoleGroupKey getGroup() {
        return group;
    }

    public Set<OAuthScope> getScopes() {
        return scopes;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OAuthScopeGroup)) return false;

        OAuthScopeGroup that = (OAuthScopeGroup) o;

        return !(group != null ? !group.equals(that.group) :
                that.group != null) &&
                !(scopes != null ?
                        !scopes.equals(that.scopes) :
                        that.scopes != null);

    }


    public Set<String> getKeys(){
        Set<String> keys = new HashSet<>();
        for (OAuthScope scope  :  this.scopes){
            keys.add(scope.getRole().getKey());
        }
        return keys;
    }

    @Override
    public int hashCode() {
        int result = group != null ?
                group.hashCode() : 0;
        result = 31 * result + (scopes != null ?
                scopes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OAuthScopeGroup{" +
                "group=" + group +
                ", scope=" + scopes +
                '}';
    }
}
