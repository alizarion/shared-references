package com.alizarion.reference.security.exception;

import com.alizarion.reference.exception.ApplicationException;

/**
 * @author selim@openlinux.fr.
 */
public class DuplicateEmailException extends ApplicationException{

    private static final long serialVersionUID = -3217947553979522072L;

    private static final String MSG =  "credential email already exist";

    public DuplicateEmailException(String msg) {
        super(MSG + msg);
    }

    public DuplicateEmailException(String msg, Throwable cause) {
        super(MSG + msg, cause);
    }

    public DuplicateEmailException(Throwable cause) {
        super(cause);
    }
}
