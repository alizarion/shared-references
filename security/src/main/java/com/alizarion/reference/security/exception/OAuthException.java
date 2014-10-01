package com.alizarion.reference.security.exception;

import com.alizarion.reference.exception.ApplicationException;

/**
 * @author selim@openlinux.fr.
 */
public class OAuthException extends ApplicationException {

    private static final long serialVersionUID = -6242773197009049923L;

    public OAuthException(String msg) {
        super(msg);
    }

    public OAuthException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public OAuthException(Throwable cause) {
        super(cause);
    }
}
