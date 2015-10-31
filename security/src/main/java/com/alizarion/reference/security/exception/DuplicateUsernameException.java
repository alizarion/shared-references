package com.alizarion.reference.security.exception;

import com.alizarion.reference.exception.ApplicationException;

/**
 * @author selim@openlinux.fr.
 */
public class DuplicateUsernameException extends ApplicationException {

    private static final long serialVersionUID = -6273212839415005466L;

    private static final String MSG = "username already exist ";

    public DuplicateUsernameException(String msg) {
        super(MSG + msg);
    }

    public DuplicateUsernameException(String msg, Throwable cause) {
        super(MSG + msg, cause);
    }

    public DuplicateUsernameException(Throwable cause) {
        super(cause);
    }
}
