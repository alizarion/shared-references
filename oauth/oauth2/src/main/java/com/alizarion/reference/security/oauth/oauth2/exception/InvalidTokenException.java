package com.alizarion.reference.security.oauth.oauth2.exception;

import com.alizarion.reference.exception.ApplicationException;

/**
 * @author selim@openlinux.fr.
 */
public class InvalidTokenException extends ApplicationException {

    private static final long serialVersionUID = -4922404297496135960L;

    public InvalidTokenException(String msg) {
        super(msg);
    }

    public InvalidTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidTokenException(Throwable cause) {
        super(cause);
    }
}
