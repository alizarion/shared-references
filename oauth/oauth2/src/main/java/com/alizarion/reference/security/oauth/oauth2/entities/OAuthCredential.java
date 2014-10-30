package com.alizarion.reference.security.oauth.oauth2.entities;

import com.alizarion.reference.exception.NotImplementedException;

import javax.mail.internet.InternetAddress;
import java.io.Serializable;
import java.net.URL;
import java.util.Locale;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */

public interface OAuthCredential<T,E> extends Serializable {


    public final static String FIND_BY_USERNAME_NAMED_QUERY = "OAuthCredential.FIND_BY_USERNAME_NAMED_QUERY";

    public E getId();


    public String getIdToString();

    public  OAuthCredential init(final Set<String> scopes);

    /**
     * Method to get username /!\ the jpa implementation
     * of this interface MUST have
     * a field named username, to be resolved in jpql queries
     * @return
     */
    public String getUsername();

    public Set<String> getAvailableScopes();

    public boolean setScopes(final Set<String> scopes);

    public Set<T> getOAuthRoles();

    public Boolean isCorrectPassword(String password);

    public boolean addScope(final String scope);

    public InternetAddress getEmail();

    public Boolean isEmailVerified();

    public Locale getLocale();


    /* optional fields */
    public String getName()throws NotImplementedException;

    public String getGender()throws NotImplementedException;

    public String getFamilyName()throws NotImplementedException;

    public String getGivenName()throws NotImplementedException;

    public String getNickname() throws NotImplementedException;

    public URL getPicture() throws NotImplementedException;

    public URL getWebSite() throws NotImplementedException;

    public URL getProfile() throws NotImplementedException;


}
