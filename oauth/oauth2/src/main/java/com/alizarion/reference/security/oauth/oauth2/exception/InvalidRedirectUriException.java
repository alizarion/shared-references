package com.alizarion.reference.security.oauth.oauth2.exception;

import com.alizarion.reference.exception.ApplicationException;

/**
 * @author selim@openlinux.fr.
 */
public class InvalidRedirectUriException extends ApplicationException {

    private static final String MSG = "invalid redirect_uri have been sent ";
    private static final long serialVersionUID = 5224566585980612640L;

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
