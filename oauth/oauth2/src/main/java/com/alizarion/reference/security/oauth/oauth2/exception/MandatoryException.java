package com.alizarion.reference.security.oauth.oauth2.exception;

/**
 * mandatory field for processing the request was not found or empty.
 * @author selim@openlinux.fr.
 */
public class MandatoryException extends OAuthException {

    private static final long serialVersionUID = -3263408423113252551L;

    public static final String MSG = "mandatory field for " +
            "processing the request was " +
            "not found or empty ";

    public MandatoryException(String msg) {
        super(MSG + msg);
    }

    public MandatoryException(String msg, Throwable cause) {
        super(MSG + msg, cause);
    }

    public MandatoryException(Throwable cause) {
        super(cause);
    }
}
