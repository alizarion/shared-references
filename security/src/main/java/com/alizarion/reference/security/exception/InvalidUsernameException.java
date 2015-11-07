package com.alizarion.reference.security.exception;

/**
 * @author selim@openlinux.fr.
 */
public class InvalidUsernameException extends SecurityException {

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
