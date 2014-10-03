package com.alizarion.reference.security.exception.oauth;

/**
 * @author selim@openlinux.fr.
 */
public class ClientIdNotFoundException extends OAuthException {

    private static final long serialVersionUID = -4562800490083191407L;

    private final static String MSG = "error cannot found this client id : ";

    public ClientIdNotFoundException(String msg) {
        super( MSG + msg);
    }

    public ClientIdNotFoundException(String msg, Throwable cause) {
        super( MSG + msg, cause);
    }

    public ClientIdNotFoundException(Throwable cause) {
        super(cause);
    }
}
