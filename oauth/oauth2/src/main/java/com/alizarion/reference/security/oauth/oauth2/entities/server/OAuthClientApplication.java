package com.alizarion.reference.security.oauth.oauth2.entities.server;

import com.alizarion.reference.security.oauth.oauth2.OAuthHelper;
import com.alizarion.reference.security.oauth.oauth2.entities.OAuthApplication;
import com.alizarion.reference.security.oauth.oauth2.entities.OAuthApplicationKey;
import com.alizarion.reference.security.oauth.oauth2.entities.OAuthAuthorization;
import com.alizarion.reference.security.oauth.oauth2.entities.OAuthCredential;
import com.alizarion.reference.security.oauth.oauth2.exception.InvalidScopeException;

import javax.persistence.*;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * Client app who has subscribe to the authorization server.
 * @author selim@openlinux.fr.
 * @see com.alizarion.reference.security.oauth.oauth2.entities.OAuthApplication
 */
@Entity
@Table(name = "security_oauth_client_application")
public class OAuthClientApplication extends OAuthApplication<OAuthScopeServer> {

    private static final long serialVersionUID = 3181235030223846729L;


    /**
     * Server allowed scopes to be requested.
     */
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "security_oauth_client_application_server_scope",
            joinColumns =
            @JoinColumn(name = "client_application_id",
                    referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "server_scope_id",
                    referencedColumnName = "id"))
    private Set<OAuthScopeServer> allowedServerScopes = new
            HashSet<>();

    /**
     * list of all authorization linked with this client.
     */
    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REMOVE})
    @JoinTable(name = "security_oauth_client_app_server_auth",
            joinColumns = @JoinColumn(name = "app_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "auth_id",referencedColumnName = "id"))
    private Set<OAuthServerAuthorization> authorizations = new HashSet<>();

    /**
     * grant param that allow client to do a password request token.
     * only for trusted client.
     */
    @Column(name = "trusted_client")
    private Boolean trustedClient;





    protected OAuthClientApplication() {
    }


    /**
     * Default constructor to define declared client application.
     * @param name of the client application.
     * @param homePage home page of the client web site.
     * @param callback callback url.
     */
    public OAuthClientApplication(final String name,
                                  final URL homePage,
                                  final URI callback) {
        super(name, homePage, callback);
        this.trustedClient =  false;
    }


    /**
     * Method to add an new authorization to the client.
     * @param credential  linked identity with this authorization.
     * @param scopes requested server scopes for this authorization.
     * @return result authorization.
     * @throws InvalidScopeException if one of the scopes are not listed in allowedServerScopes.
     */
    @Override
    public OAuthAuthorization addAuthorization(
            final OAuthCredential credential,
            final Set<OAuthScopeServer> scopes)
            throws InvalidScopeException {

        if (this.allowedServerScopes.containsAll(scopes)){
            for (OAuthScopeServer scopeServer :  scopes){
                if (!credential
                        .getOAuthRoles()
                        .contains(scopeServer.getRole())){
                    throw new InvalidScopeException("user does not have " +
                            scopeServer.getRole().getRoleId());
                }
            }

            OAuthServerAuthorization authorization =
                    new OAuthServerAuthorization(this,
                            credential,
                            scopes);
            this.authorizations.add(authorization);
            return authorization;

        }   else {
            scopes.removeAll(this.allowedServerScopes);
            Iterator<OAuthScopeServer> unAuthorized =
                    allowedServerScopes.iterator();
            String unAuthorizedString= "";
            while (unAuthorized.hasNext()){
                if (unAuthorized.hasNext()){
                    unAuthorizedString  = unAuthorizedString +
                            unAuthorized.next().getScope().getKey() +  ", ";
                }   else {
                    unAuthorizedString  = unAuthorizedString +
                            unAuthorized.next().getScope().getKey();
                }
            }

            throw new InvalidScopeException(unAuthorizedString);

        }

    }

    /**
     * Method to get server scope by key.
     * @param key scope key
     * @return server scope
     * @throws InvalidScopeException if the requested scope
     * key is not allowed by default for this client.
     */
    public OAuthScopeServer  getServerScopeByKey(final String key)
            throws InvalidScopeException {
        for (OAuthScopeServer scopeServer :this.allowedServerScopes ){
            if (scopeServer.getScope().getKey().equals(key)){
                return scopeServer;
            }
        }
        throw new  InvalidScopeException(key);
    }



    /**
     * Method to get list of server scopes by list of scope keys.
     * @param scopesKeys list of scope keys
     * @return set of valid server scopes
     * @throws InvalidScopeException  if the requested scope
     * key is not allowed by default for this client.
     */
    public Set<OAuthScopeServer> getServerScopesByKeys(
            final Set<String> scopesKeys) throws InvalidScopeException {
        Set<OAuthScopeServer> scopes= new HashSet<>();
        for (String scope: scopesKeys){
            scopes.add(getServerScopeByKey(scope));
        }
        return scopes;
    }

    public Set<OAuthServerAuthorization> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(Set<OAuthServerAuthorization> authorizations) {
        this.authorizations = authorizations;
    }

    public Boolean getTrustedClient() {
        return trustedClient;
    }

    public void generateApplicationKey(){
        this.applicationKey = new OAuthApplicationKey(
                UUID.randomUUID().toString(),
                OAuthHelper.
                        getRandomAlphaNumericString(300));
    }

    /**
     * Method to get authorized client scopes.
     * @return set of authorized server scopes.
     */
    public Set<OAuthScopeServer> getAllowedServerScopes() {
        return allowedServerScopes;
    }

    public void setAllowedServerScopes(
            final Set<OAuthScopeServer> allowedServerScopes) {
        this.allowedServerScopes.addAll(allowedServerScopes);
    }

    public void addAllowedServerScope(
            final OAuthScopeServer serverScope){
        this.allowedServerScopes.add(serverScope);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OAuthClientApplication)) return false;

        OAuthClientApplication that = (OAuthClientApplication) o;

        return !(authorizations != null ?
                !authorizations.equals(that.authorizations) :
                that.authorizations != null);

    }

    @Override
    public int hashCode() {
        return authorizations != null ?
                authorizations.hashCode() : 0;
    }

    @Override
    @SuppressWarnings(value = "all")
    public String toString() {
        Iterator<OAuthServerAuthorization> iterator =
                this.authorizations.iterator();
        StringBuffer stringBuffer =  new StringBuffer('{');
        while (iterator.hasNext()){
            if (iterator.hasNext()){
                stringBuffer.
                        append(iterator.
                                toString()).append(',');
            }   else {
                stringBuffer.
                        append(iterator.
                                toString()).append('}');

            }
        }

        return "OAuthClientApplication{" +
                "authorizations=" + stringBuffer +
                '}';
    }
}
