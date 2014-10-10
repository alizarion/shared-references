package com.alizarion.reference.security.exception;

import com.alizarion.reference.exception.ApplicationException;

/**
 * @author selim@openlinux.fr.
 */
public class InvalidUsernameException extends ApplicationException {

    private static final long serialVersionUID = -9113873103531522943L;


    private static final String MSG = "username does not exist ";

    public InvalidUsernameException(String msg) {
        super(MSG + msg);
    }

    public InvalidUsernameException(String msg, Throwable cause) {
        super(MSG + msg, cause);
    }

    public InvalidUsernameException(Throwable cause) {
        super(cause);
    }
}
