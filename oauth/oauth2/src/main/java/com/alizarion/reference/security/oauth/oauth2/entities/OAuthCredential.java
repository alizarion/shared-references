package com.alizarion.reference.security.oauth.oauth2.entities;

import java.io.Serializable;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */

public interface OAuthCredential<T,T2> extends Serializable {


    public final static String FIND_BY_USERNAME_NAMED_QUERY = "OAuthCredential.FIND_BY_USERNAME_NAMED_QUERY";

    public T2 getId();



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



}
