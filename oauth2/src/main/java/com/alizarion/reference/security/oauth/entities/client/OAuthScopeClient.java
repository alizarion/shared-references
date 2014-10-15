package com.alizarion.reference.security.oauth.entities.client;

import com.alizarion.reference.security.oauth.entities.OAuthScope;
import com.alizarion.reference.security.oauth.entities.ScopeKey;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_oauth_scope_client")
@AttributeOverride(name = "scope.key",
        column = @Column(name = "scope_key",
                nullable = false,
                unique = false,
                length = 25))
public class OAuthScopeClient extends OAuthScope {

    private static final long serialVersionUID = 4173744809959570878L;

    @ManyToOne
    @JoinColumn(name = "oauth_server_application_id")
    public OAuthServerApplication application;

    @Column(name = "is_asked_by_default")
    private Boolean askByDefault;

    @ManyToMany(mappedBy = "scopes")
    private Set<OAuthClientAuthorization> authorizations = new HashSet<>();

    @ManyToMany(mappedBy = "scopes")
    private Set<OAuthScopeClientGroup> clientScopeGroups = new HashSet<>();

    protected OAuthScopeClient() {
    }

    public OAuthScopeClient(ScopeKey scope) {
        super(scope);
        this.askByDefault = false;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OAuthScopeClient)) return false;

        OAuthScopeClient that = (OAuthScopeClient) o;

        return !(application != null ?
                !application.equals(that.application) :
                that.application != null);

    }

    public Set<OAuthScopeClientGroup> getClientScopeGroups() {
        return clientScopeGroups;
    }

    public OAuthServerApplication getApplication() {
        return application;
    }

    public void setApplication(OAuthServerApplication application) {
        this.application = application;
    }

    public Set<OAuthClientAuthorization> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(Set<OAuthClientAuthorization> authorizations) {
        this.authorizations = authorizations;
    }

    public Boolean getAskByDefault() {
        return askByDefault;
    }

    public void setAskByDefault(Boolean askByDefault) {
        this.askByDefault = askByDefault;
    }

    @Override
    public int hashCode() {
        return application != null ? application.hashCode() : 0;
    }
}
