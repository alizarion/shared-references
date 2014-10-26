package com.alizarion.reference.security.oauth.oauth2.exception;

/**
 * @author selim@openlinux.fr.
 */
public class OAuthOpenIDException extends OAuthException {

    private static final long serialVersionUID = -1298605855383919739L;

    public OAuthOpenIDException(Throwable cause) {
        super(cause);
    }

    public OAuthOpenIDException(String msg) {
        super(msg);
    }

    public OAuthOpenIDException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
