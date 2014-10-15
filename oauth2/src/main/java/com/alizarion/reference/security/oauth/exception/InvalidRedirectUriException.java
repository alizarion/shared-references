package com.alizarion.reference.security.oauth.exception;

import com.alizarion.reference.exception.ApplicationException;

/**
 * @author selim@openlinux.fr.
 */
public class InvalidRedirectUriException extends ApplicationException {

    private static final String MSG = "invalid redirect_uri have been sent ";

    public InvalidRedirectUriException(String msg) {
        super(MSG + msg);
    }

    public InvalidRedirectUriException(String msg, Throwable cause) {
        super(MSG + msg, cause);
    }

    public InvalidRedirectUriException(Throwable cause) {
        super(cause);
    }
}
