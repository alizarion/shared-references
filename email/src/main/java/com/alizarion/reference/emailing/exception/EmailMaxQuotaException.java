package com.alizarion.reference.emailing.exception;

/**
 * @author selim@openlinux.fr.
 * Thows when max quota
 */
public class EmailMaxQuotaException extends EmailException {

    private static final long serialVersionUID = 3678708832700735465L;

    public EmailMaxQuotaException(String msg) {
        super(msg);
    }

    public EmailMaxQuotaException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public EmailMaxQuotaException(Throwable cause) {
        super(cause);
    }
}
