package com.alizarion.reference.security.oauth.oauth2.exception;

/**
 * @author selim@openlinux.fr.
 */
public class BadCredentialException extends OAuthException {

    private static final long serialVersionUID = -4050402314042629707L;

    private final static String MSG = "bad credentials ";

    public BadCredentialException(String msg) {
        super(MSG +msg);
    }

    public BadCredentialException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BadCredentialException(Throwable cause) {
        super(cause);
    }
}
