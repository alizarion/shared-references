package com.alizarion.reference.security.oauth.oauth2.exception;

/**
 * @author selim@openlinux.fr.
 */
public class InvalidRefreshTokenException extends OAuthException {


    private static final long serialVersionUID = -1103819154146952527L;

    private static final String MSG = "invalid or expired refresh token ";

    public InvalidRefreshTokenException(Throwable cause) {
        super(cause);
    }

    public InvalidRefreshTokenException(String msg) {
        super(MSG + msg);
    }

    public InvalidRefreshTokenException(String msg, Throwable cause) {
        super(MSG + msg, cause);
    }
}
