package com.alizarion.reference.security.exception;

import com.alizarion.reference.exception.ApplicationException;

/**
 * @author selim@openlinux.fr.
 */
public class BadCredentialException extends ApplicationException {

    private static final long serialVersionUID = -3062083779485953072L;

    private static final String MSG  =  "no user credenial for ";

    public BadCredentialException(String msg) {
        super(MSG + msg);
    }

    public BadCredentialException(String msg, Throwable cause) {
        super(MSG + msg, cause);
    }

    public BadCredentialException(Throwable cause) {
        super(cause);
    }
}
