package com.alizarion.reference.security.oauth.oauth2.entities.client;

import com.alizarion.reference.security.oauth.oauth2.entities.*;
import com.alizarion.reference.security.oauth.oauth2.exception.InvalidScopeException;

import javax.persistence.*;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * describe the server application<br/>
 * service that will be requested for an authorization.
 * @author selim@openlinux.fr.
 * @see com.alizarion.reference.security.oauth.oauth2.entities.OAuthApplication
 * @see com.alizarion.reference.security.oauth.oauth2.entities.OAuthAuthorization
 */
@Entity
@Table(name = "security_oauth_server_application")
public class OAuthServerApplication extends OAuthApplication<OAuthScopeClient> {

    private static final long serialVersionUID = 4124715375581000690L;


    @OneToMany(cascade =
            {CascadeType.MERGE,CascadeType.PERSIST},
            mappedBy = "application")
    private Set<OAuthScopeClient> clientAuthorizedScopes = new HashSet<>();

    /**
     * Granted roles in client app
     * on Oauth authorization success.
     */
    @ManyToMany(cascade =
            {CascadeType.MERGE,
                    CascadeType.PERSIST})
    private Set<OAuthRole> grantedRoles =
            new HashSet<>();


    @OneToMany(cascade =
            {CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REMOVE})
    private Set<OAuthClientAuthorization> authorizations =
            new HashSet<>();


    @Column(name = "api_authorization_url",
            nullable = false,
            unique = true)
    private URL apiAuthzUrl;

    @Column(name = "api_token_url",
            nullable = false,
            unique = true)
    private URL apiTokenUrl;

    /**
     * list of all authorization linked with this client.
     */
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REMOVE})
    @JoinTable(name = "security_oauth_client_app_server_cert",
            joinColumns = @JoinColumn(name = "app_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "cert_id",referencedColumnName = "id"))
    private Set<OAuthCachedJWKCertificate> certs = new HashSet<>();


    /**
     *
     * @param name  of the application.
     * @param homePage home page url of the application.
     * @param callback oauth callback url.
     * @param apiAuthzUrl api url to send authorization request
     * @param apiTokenUrl api url to get access token
     * @param applicationKey clientId and clientSecret
     */
    public OAuthServerApplication(
            final String name,
            final URL homePage,
            final URI callback,
            final URL apiAuthzUrl,
            final URL apiTokenUrl,
            final OAuthApplicationKey applicationKey) {
        super(name, homePage, callback);
        this.apiAuthzUrl = apiAuthzUrl;
        this.apiTokenUrl = apiTokenUrl;
        this.applicationKey = applicationKey;

    }

    public OAuthServerApplication() {
    }

    public URL getApiAuthzUrl() {
        return apiAuthzUrl;
    }

    public void setApiAuthzUrl(URL apiAuthzUrl) {
        this.apiAuthzUrl = apiAuthzUrl;
    }

    public URL getApiTokenUrl() {
        return apiTokenUrl;
    }

    public void setApiTokenUrl(URL apiTokenUrl) {
        this.apiTokenUrl = apiTokenUrl;
    }

    public void setAuthorizations(
            final Set<OAuthClientAuthorization> authorizations) {
        this.authorizations = authorizations;
    }

    public Set<OAuthScopeClient> getAllowedScopes() {
        return clientAuthorizedScopes;
    }

    public Set<OAuthScopeClient> getClientAuthorizedScopes() {
        return clientAuthorizedScopes;
    }

    public void setClientAuthorizedScopes(
            final Set<OAuthScopeClient> clientAuthorizedScopes) {
        this.clientAuthorizedScopes = clientAuthorizedScopes;
    }

    public Set<OAuthCachedJWKCertificate> getCerts() {
        return certs;
    }

    /**
     * Method to get a valid cached sign certificate.
     * @return set of valid certificates.
     */
    public  Set<OAuthCachedJWKCertificate> getValidCachedCerts(){
        Set<OAuthCachedJWKCertificate> result =  new HashSet<>();
        for (OAuthCachedJWKCertificate cert  :  this.certs){
            if (!cert.hasExpire()){
                result.add(cert);
            }
        }
        return result;
    }

    /**
     * Method to add authorized scope
     * @param scopeClient scope to add
     * @return the added OAuthScopeClient
     */
    public OAuthScopeClient addClientAuthorizedScope(
            final OAuthScopeClient scopeClient){
        scopeClient.setApplication(this);
        this.clientAuthorizedScopes.add(scopeClient);
        return scopeClient;
    }

    public Set<OAuthScopeClient> getDefaultClientScopes(){
        Set<OAuthScopeClient> scopeClients = new HashSet<>();
        for (OAuthScopeClient scope :  this.clientAuthorizedScopes){
            if (scope.getAskByDefault()){
                scopeClients.add(scope);
            }
        }
        return scopeClients;
    }

    public String getDefaultScopesCommaSeparated() {
        StringBuilder stringBuffer = new StringBuilder();
        Iterator<OAuthScopeClient> clientScopeIterator = getDefaultClientScopes().iterator();
        while (clientScopeIterator.hasNext()) {
            if (clientScopeIterator.hasNext()) {
                stringBuffer.append(
                        clientScopeIterator.
                                next().getScope().
                                getKey()).append(',');
            } else {
                stringBuffer.append(
                        clientScopeIterator.
                                next().getScope().
                                getKey());

            }
        }
        return stringBuffer.toString();

    }

    public Set<OAuthRole> getGrantedRoles() {
        return grantedRoles;
    }



    public void setGrantedRoles(Set<OAuthRole> defaultGrantedRoles) {
        this.grantedRoles.addAll(defaultGrantedRoles);
    }


    public void addGrantedRole(final OAuthRole role){
        this.grantedRoles.add(role);
    }

    @Override
    public OAuthAuthorization addAuthorization(
            final OAuthCredential credential,
            final Set<OAuthScopeClient> requestedRoles)
            throws InvalidScopeException {
        if (this.clientAuthorizedScopes.containsAll(requestedRoles)){
            OAuthClientAuthorization authorization =
                    new OAuthClientAuthorization(this,credential,
                            requestedRoles );
            this.authorizations.add(authorization);
            return authorization;
        } else {
            requestedRoles.removeAll(this.clientAuthorizedScopes);
            Iterator<OAuthScopeClient> unAuthorized =
                    requestedRoles.iterator();
            String unAuthorizedString= "";
            while (unAuthorized.hasNext()){
                if (unAuthorized.hasNext()){
                    unAuthorizedString  =
                            unAuthorizedString +
                                    unAuthorized.next().
                                            getScope().getKey() +  ", ";
                }   else {
                    unAuthorizedString  = unAuthorizedString +
                            unAuthorized.next().getScope().getKey();
                }
            }

            throw new InvalidScopeException(unAuthorizedString);
        }

    }

    public Set<OAuthClientAuthorization> getAuthorizations() {
        return authorizations;
    }

}
