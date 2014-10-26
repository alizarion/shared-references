package com.alizarion.reference.security.oauth.oauth2.exception;

/**
 * @author selim@openlinux.fr.
 */
public class OAuthOpenIDSignatureException extends OAuthOpenIDException {


    private static final long serialVersionUID = 1575811847329834567L;

    public OAuthOpenIDSignatureException(Throwable cause) {
        super(cause);
    }

    public OAuthOpenIDSignatureException(String msg) {
        super(msg);
    }

    public OAuthOpenIDSignatureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
