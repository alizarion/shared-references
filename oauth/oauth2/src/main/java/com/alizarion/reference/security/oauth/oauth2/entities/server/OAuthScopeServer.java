package com.alizarion.reference.security.oauth.oauth2.entities.server;

import com.alizarion.reference.security.oauth.oauth2.entities.OAuthRole;
import com.alizarion.reference.security.oauth.oauth2.entities.OAuthScope;
import com.alizarion.reference.security.oauth.oauth2.entities.ScopeKey;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Class that link oauth scope with server roles,
 * server scope must be linked to a real user in app role,
 * scope cannot be attribute to a user without the good role.
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_oauth_scope_server")
public class OAuthScopeServer extends OAuthScope {

    private static final long serialVersionUID = 6577148257872055707L;

    /**
     *
     */
    @ManyToMany(mappedBy = "allowedServerScopes")
    private Set<OAuthClientApplication> application = new HashSet<>();

    @ManyToMany(mappedBy = "scopes")
    private Set<OAuthScopeServerGroup>  scopeServerGroups = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "oauth_role_id", nullable = false)
    private OAuthRole role;

    public Set<OAuthClientApplication> getApplication() {
        return application;
    }

    public void setApplication(Set<OAuthClientApplication> application) {
        this.application = application;
    }

    public OAuthScopeServer(ScopeKey scope, OAuthRole role) {
        super(scope);
        this.role = role;
    }

    public Set<OAuthScopeServerGroup> getScopeServerGroups() {
        return scopeServerGroups;
    }

    protected OAuthScopeServer() {
    }

    public OAuthRole getRole() {
        return role;
    }
}
