package com.alizarion.reference.social.entities.exception;


import com.alizarion.reference.exception.ApplicationException;

/**
 * Social package exception parent.
 * @author selim@openlinux.fr.
 */
public class SocialException extends ApplicationException {

    private static final long serialVersionUID = -5841031795842541764L;

    public SocialException(String msg) {
        super(msg);
    }

    public SocialException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SocialException(Throwable cause) {
        super(cause);
    }
}
