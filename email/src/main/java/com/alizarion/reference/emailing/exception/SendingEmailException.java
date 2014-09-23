package com.alizarion.reference.emailing.exception;

import com.alizarion.reference.exception.ApplicationError;

/**
 * Error that occur on sending transaction,
 * for some reason that is beyond us
 * @author selim@openlinux.fr.
 */
public class SendingEmailException extends ApplicationError {


    private static final long serialVersionUID = -3365456319189826145L;

    public SendingEmailException(String msg) {
        super(msg);
    }

    public SendingEmailException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
