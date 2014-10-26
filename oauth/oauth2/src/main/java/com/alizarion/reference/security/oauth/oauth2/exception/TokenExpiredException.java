package com.alizarion.reference.security.oauth.oauth2.exception;

import com.alizarion.reference.exception.ApplicationException;

/**
 * The actual date time is bigger than token expireDate.
 * @author selim@openlinux.fr.
 */
public class TokenExpiredException extends ApplicationException{


    private static final long serialVersionUID = 1313046984031409821L;

    public TokenExpiredException(String msg) {
        super(msg);
    }

    public TokenExpiredException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TokenExpiredException(Throwable cause) {
        super(cause);
    }
}
