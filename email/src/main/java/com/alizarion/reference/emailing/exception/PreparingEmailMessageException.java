package com.alizarion.reference.emailing.exception;

/**
 * And exception occur on preparing email message.
 * @author selim@openlinux.fr.
 */
public class PreparingEmailMessageException extends EmailException{

    private static final long serialVersionUID = -3988394437116568366L;

    public PreparingEmailMessageException(String msg) {
        super(msg);
    }

    public PreparingEmailMessageException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PreparingEmailMessageException(Throwable cause) {
        super(cause);
    }
}
