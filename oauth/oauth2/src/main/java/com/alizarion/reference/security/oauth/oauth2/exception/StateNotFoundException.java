package com.alizarion.reference.security.oauth.oauth2.exception;

/**
 * @author selim@openlinux.fr.
 */
public class StateNotFoundException extends OAuthException {

    private static final long serialVersionUID = 5882818935942614822L;

    private static final String MSG ="state not found ";

    public StateNotFoundException(String msg) {
        super(MSG + msg);
    }

    public StateNotFoundException(String msg, Throwable cause) {
        super(MSG + msg, cause);
    }

    public StateNotFoundException(Throwable cause) {
        super(cause);
    }
}
