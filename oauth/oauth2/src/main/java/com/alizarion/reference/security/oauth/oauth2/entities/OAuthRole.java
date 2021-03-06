package com.alizarion.reference.security.oauth.oauth2.entities;

import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
public interface OAuthRole extends Serializable {

    public String getRoleId();

    public boolean equals(Object o);

    public int hashCode();

}
