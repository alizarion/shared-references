package com.alizarion.reference.security.oauth.exception;

/**
 * @author selim@openlinux.fr.
 */
public class InvalidAccessTokenException extends OAuthException {

    private static final long serialVersionUID = 1194680950760060530L;

    private final static String MSG =  "invalid access token ";

    public InvalidAccessTokenException(String msg) {
        super(MSG + msg);
    }

    public InvalidAccessTokenException(String msg, Throwable cause) {
        super(MSG + msg, cause);
    }

    public InvalidAccessTokenException(Throwable cause) {
        super(cause);
    }
}
