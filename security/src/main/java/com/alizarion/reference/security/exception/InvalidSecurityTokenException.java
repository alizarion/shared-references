package com.alizarion.reference.security.exception;

import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
public class InvalidSecurityTokenException extends
        SecurityException implements Serializable {

    private static final long serialVersionUID = 382613733451931723L;


    public InvalidSecurityTokenException(String msg) {
        super(msg);
    }

    public InvalidSecurityTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidSecurityTokenException(Throwable cause) {
        super(cause);
    }
}
