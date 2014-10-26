package com.alizarion.reference.security.oauth.oauth2.entities.client;

import com.alizarion.reference.security.oauth.oauth2.entities.OAuthScopeGroup;
import com.alizarion.reference.security.oauth.oauth2.entities.ScopeGroupKey;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_oauth_scope_client_group")
public class OAuthScopeClientGroup extends OAuthScopeGroup {

    private static final long serialVersionUID = 1482813938233797076L;

    @ManyToMany
    @JoinTable(name = "security_oauth_scope_client_group_join_scopes",
            joinColumns = @JoinColumn(name = "scope_client_group_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "client_scope_id",
                    referencedColumnName = "id"))
    private Set<OAuthScopeClient> scopes = new HashSet<>();

    public OAuthScopeClientGroup(Set<OAuthScopeClient> scopes) {
        this.scopes = scopes;
    }

    public OAuthScopeClientGroup(ScopeGroupKey group, Set<OAuthScopeClient> scopes) {
        super(group);
        this.scopes = scopes;
    }

    public OAuthScopeClientGroup() {
    }

    public Set<OAuthScopeClient> getScopes() {
        return scopes;
    }

    @Override
    public Set<String> getGroupKeys() {
        Set<String> scopeKeys = new HashSet<>();
        for (OAuthScopeClient  scope: scopes){
            scopeKeys.add(scope.getScope().getKey());
        }
        return scopeKeys;
    }
}
