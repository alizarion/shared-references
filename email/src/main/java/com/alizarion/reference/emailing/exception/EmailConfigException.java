package com.alizarion.reference.emailing.exception;

/**
 * And exception occur on handling email config params.
 * @author selim@openlinux.fr.
 */
public class EmailConfigException extends EmailException {

    private static final long serialVersionUID = 8272171816038128028L;

    public EmailConfigException(String msg) {
        super(msg);
    }

    public EmailConfigException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
