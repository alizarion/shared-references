package com.alizarion.reference.location.exception;

import com.alizarion.reference.exception.ApplicationException;

/**
 * @author selim@openlinux.fr.
 */
public class EmailMalformedException extends ApplicationException {


    private static final long serialVersionUID = 6957579319167033612L;

    private final static String MSG = "Malformed e-mail ";

    public EmailMalformedException(String msg) {
        super(MSG  + msg);
    }

    public EmailMalformedException(String msg, Throwable cause) {
        super(MSG  + msg, cause);
    }

    public EmailMalformedException(Throwable cause) {
        super(cause);
    }
}
