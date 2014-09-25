package com.alizarion.reference.emailing.exception;

/**
 * Email attachment size exceed what is allowed in email properties params.
 * @author Selim Bensenouci.
 */
public class EmailAttachmentSizeExceedException extends EmailException {

    private static final long serialVersionUID = 5004482285827007157L;

    public EmailAttachmentSizeExceedException(String msg) {
        super(msg);
    }

    public EmailAttachmentSizeExceedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
