package com.alizarion.reference.security.oauth.oauth2.exception;

/**
 * Invalid scope parameter in initial Authorization
 * @author selim@openlinux.fr.
 */
public class InvalidScopeException extends OAuthException {

    private static final long serialVersionUID = -8520542982691087267L;

    public static final String MSG = "Invalid scope ";

    public InvalidScopeException(String msg) {
        super(MSG + msg);
    }

    public InvalidScopeException(String msg, Throwable cause) {
        super(MSG + msg, cause);
    }

    public InvalidScopeException(Throwable cause) {
        super(cause);
    }
}
