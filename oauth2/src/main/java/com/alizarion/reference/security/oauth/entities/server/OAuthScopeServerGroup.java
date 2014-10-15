package com.alizarion.reference.security.oauth.entities.server;


import com.alizarion.reference.security.oauth.entities.OAuthScopeGroup;
import com.alizarion.reference.security.oauth.entities.ScopeGroupKey;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_oauth_scope_server_group")
public class OAuthScopeServerGroup extends OAuthScopeGroup {

    private static final long serialVersionUID = -7220480335580407512L;

    @ManyToMany
    @JoinTable(name = "security_oauth_scope_server_group_join_server_scope",
            joinColumns =
            @JoinColumn(name = "group_id",
                    referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "server_scope_id",
                    referencedColumnName = "id"))
    private Set<OAuthScopeServer> scopes = new HashSet<>();

    public OAuthScopeServerGroup(
            final Set<OAuthScopeServer> scopes) {
        this.scopes = scopes;
    }

    public OAuthScopeServerGroup(final ScopeGroupKey group,
                                 final Set<OAuthScopeServer> scopes) {
        super(group);

        this.scopes.addAll(scopes);
    }

    protected OAuthScopeServerGroup() {
    }

    @Override
    public Set<String> getGroupKeys() {
        Set<String> scopeKeys = new HashSet<>();
        for (OAuthScopeServer scope: scopes){
            scopeKeys.add(scope.getScope().getKey());
        }
        return scopeKeys;
    }


}
