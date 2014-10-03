package com.alizarion.reference.security.exception.oauth;

/**
 * 'grant_type' parameter was invalid or unsupported.
 * @author selim@openlinux.fr.
 */
public class UnsupportedGrantTypeException  extends OAuthException{

    private static final long serialVersionUID = -7534253325153529901L;

    public final static String MSG = "'grant_type' parameter was invalid or unsupported ";

    public UnsupportedGrantTypeException(String msg) {
        super(MSG + msg);
    }

    public UnsupportedGrantTypeException(String msg, Throwable cause) {
        super(MSG + msg, cause);
    }

    public UnsupportedGrantTypeException(Throwable cause) {
        super(cause);
    }
}
