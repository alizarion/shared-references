package com.alizarion.reference.security.entities.oauth.server;

import com.alizarion.reference.security.entities.Credential;
import com.alizarion.reference.security.entities.CredentialRole;
import com.alizarion.reference.security.entities.oauth.OAuthApplication;
import com.alizarion.reference.security.entities.oauth.OAuthApplicationKey;
import com.alizarion.reference.security.entities.oauth.OAuthAuthorization;
import com.alizarion.reference.security.exception.oauth.InvalidScopeException;
import com.alizarion.reference.security.tools.SecurityHelper;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_oauth_client_application")
public class OAuthClientApplication extends OAuthApplication {

    private static final long serialVersionUID = 3181235030223846729L;

    @OneToMany(cascade = {CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REMOVE})
    private Set<OAuthServerAuthorization> authorizations = new HashSet<>();


    public OAuthClientApplication() {
    }


    /**
     * Default constructor to define declared client application.
     * @param name of the client application.
     * @param homePage home page of the client web site.
     * @param callback callback url.
     */
    public OAuthClientApplication(final String name,
                                  final URL homePage,
                                  final URL callback) {
        super(name, homePage, callback);
    }



    @Override
    public OAuthAuthorization addAuthorization(
            final Credential credential,
            final Set<String> scopes)
            throws InvalidScopeException {
        OAuthServerAuthorization authorization =
                new OAuthServerAuthorization(this,
                        extractCredentialRole(scopes,
                                credential));
        this.authorizations.add(authorization);
        return authorization;
    }

    @SuppressWarnings(value = "all")
    private Set<CredentialRole> extractCredentialRole(
            final Set<String> scopes,
            Credential credential)
            throws InvalidScopeException {
        Set<CredentialRole> roles = new HashSet<>();
        if (this.defaultRoles.getKeys().containsAll(scopes)){
            for (String scope :  scopes){
                for (CredentialRole credentialRole :
                        credential.getCredentialRoles()){
                    if (credentialRole.getRole().
                            getRoleKey().
                            getKey().
                            equals(scope)){
                        roles.add(credentialRole);
                    }
                }
            }
        }  else {
            scopes.removeAll(this.defaultRoles.getKeys());
            StringBuffer stringBuffer = new StringBuffer();
            Iterator<String> keyIterator = scopes.iterator();
            while (keyIterator.hasNext()){

                if (keyIterator.hasNext()) {
                    stringBuffer.
                            append(keyIterator.
                                    next()).append(',');
                } else {
                    stringBuffer.
                            append(keyIterator.
                                    next());
                }
            }
            throw new InvalidScopeException(
                    stringBuffer.toString());
        }
        return roles;
    }

    public Set<OAuthServerAuthorization> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(Set<OAuthServerAuthorization> authorizations) {
        this.authorizations = authorizations;
    }

    public void generateApplicationKey(){
        this.applicationKey = new OAuthApplicationKey(
                UUID.randomUUID().toString(),
                SecurityHelper.
                        getRandomAlphaNumericString(300));
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
