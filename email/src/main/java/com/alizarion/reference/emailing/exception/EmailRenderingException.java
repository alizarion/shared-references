package com.alizarion.reference.emailing.exception;

/**
 * Cannot render email template.
 * @author selim@openlinux.fr.
 */
public class EmailRenderingException extends EmailException {

    public EmailRenderingException(String msg) {
        super(msg);
    }

    public EmailRenderingException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public EmailRenderingException(Throwable cause) {
        super(cause);
    }
}
