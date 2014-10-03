package com.alizarion.reference.security.entities.oauth.client;

import com.alizarion.reference.security.entities.Credential;
import com.alizarion.reference.security.entities.oauth.OAuthApplication;
import com.alizarion.reference.security.entities.oauth.OAuthApplicationKey;
import com.alizarion.reference.security.entities.oauth.OAuthAuthorization;
import com.alizarion.reference.security.exception.oauth.InvalidScopeException;

import javax.persistence.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * describe the server application<br/>
 * service that will be requested for an authorization.
 * @author selim@openlinux.fr.
 * @see com.alizarion.reference.security.entities.oauth.OAuthApplication
 * @see com.alizarion.reference.security.entities.oauth.OAuthAuthorization
 */
@Entity
@Table(name = "security_oauth_server_application")
public class OAuthServerApplication extends OAuthApplication{

    private static final long serialVersionUID = 4124715375581000690L;

    @OneToMany
    private Set<OAuthScope> allowedScopes =
            new HashSet<>();

    @OneToMany(cascade =
            {CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REMOVE})
    private Set<OAuthClientAuthorization> authorizations =
            new HashSet<>();



    /**
     * default scopes that will be
     * requested on demanding oauth authorization.
     */
    @OneToOne
    private OAuthScopeGroup requestedScopeByDefault;


    /**
     *
     * @param name  of the application.
     * @param homePage home page url of the application.
     * @param callback oauth callback url.
     */
    public OAuthServerApplication(final String name,
                                  final URL homePage,
                                  final URL callback,
                                  final OAuthApplicationKey applicationKey) {
        super(name, homePage, callback);
        this.applicationKey = applicationKey;
    }

    public OAuthServerApplication() {
    }

    @Override
    public OAuthAuthorization addAuthorization(
            final Credential credential,
            final Set<String> requestedRoles) throws InvalidScopeException {
        Set<OAuthScope>  requestedScopes = new HashSet<>();
        for (String role : requestedRoles){
            requestedScopes.add(getAssociatedScope(role));
        }
        OAuthClientAuthorization authorization =
                new OAuthClientAuthorization(this,
                        credential,
                        requestedScopes);
        this.authorizations.add(authorization);
        return authorization;
    }


    public Set<OAuthScope> getAllowedScopes() {
        return allowedScopes;
    }

    private OAuthScope getAssociatedScope(
            final String role)
            throws InvalidScopeException {
        for (OAuthScope scope: this.allowedScopes){
            if (scope.getRole().getKey().equals(role)){
                return scope;
            }
        }
        throw new InvalidScopeException(role);
    }

    public OAuthScopeGroup getRequestedScopeByDefault() {
        return requestedScopeByDefault;
    }

    public void setRequestedScopeByDefault(
            final OAuthScopeGroup requestedScopeByDefault) {
        this.requestedScopeByDefault = requestedScopeByDefault;
    }

    public void setAllowedScopes(
            final Set<OAuthScope> allowedScopes) {
        this.allowedScopes = allowedScopes;
    }

    public Set<OAuthClientAuthorization> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(Set<OAuthClientAuthorization> authorizations) {
        this.authorizations = authorizations;
    }
}
