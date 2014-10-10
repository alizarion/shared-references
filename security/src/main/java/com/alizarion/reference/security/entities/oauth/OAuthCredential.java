package com.alizarion.reference.security.entities.oauth;

import java.io.Serializable;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
public interface OAuthCredential<T> extends Serializable {

    public final static String FIND_BY_USERNAME_NAMED_QUERY = "OAuthCredential.FIND_BY_USERNAME_NAMED_QUERY";

    public String getId();

    public  OAuthCredential init(final Set<String> scopes);

    public String getUsername();

    public Set<String> getAvailableScopes();

    public boolean setScopes(final Set<String> scopes);

    public Set<T> getOAuthRoles();

    public Boolean isCorrectPassword(String password);

    public boolean addScope(final String scope);



}
