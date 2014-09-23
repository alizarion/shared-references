package com.alizarion.reference.security.exception;

import com.alizarion.reference.exception.ApplicationException;

/**
 * Bad token, not found in persisted tokens.
 * @author selim@openlinux.fr.
 */
public class TokenNotFoundException extends ApplicationException {


    private static final long serialVersionUID = 3309797415102353262L;

    public TokenNotFoundException(String msg) {
        super(msg);
    }

    public TokenNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TokenNotFoundException(Throwable cause) {
        super(cause);
    }
}
