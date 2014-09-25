package com.alizarion.reference.emailing.exception;

import com.alizarion.reference.exception.ApplicationException;

/**
 * Must be extended by all email exception.
 * @author Selim Bensenouci.
 */
public class EmailException  extends ApplicationException{

    private static final long serialVersionUID = 7711994263158708650L;

    public EmailException(String msg) {
        super(msg);
    }

    public EmailException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public EmailException(Throwable cause) {
        super(cause);
    }
}
