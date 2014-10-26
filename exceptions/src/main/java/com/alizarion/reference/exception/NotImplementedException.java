package com.alizarion.reference.exception;

/**
 * @author selim@openlinux.fr.
 */
public class NotImplementedException extends ApplicationException {


    private static final long serialVersionUID = -7517982065879231793L;

    private static final String MSG = "Not implemented functionality : ";

    public NotImplementedException(String msg) {
        super(MSG + msg);
    }

    public NotImplementedException(String msg, Throwable cause) {
        super(MSG + msg, cause);
    }

    public NotImplementedException(Throwable cause) {
        super(cause);
    }
}
