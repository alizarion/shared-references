package com.alizarion.reference.security.oauth.oauth2.exception;

/**
 * @author selim@openlinux.fr.
 */

public class InvalidAuthCodeException extends OAuthException {


    private static final long serialVersionUID = -1214223813091928645L;

    private static final String MSG =  "bad code or has expired ";


    public InvalidAuthCodeException(String msg) {
        super(MSG + msg);
    }

    public InvalidAuthCodeException(String msg, Throwable cause) {
        super(MSG + msg, cause);
    }

    public InvalidAuthCodeException(Throwable cause) {
        super(cause);
    }
}
