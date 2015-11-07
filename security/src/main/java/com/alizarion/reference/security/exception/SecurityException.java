package com.alizarion.reference.security.exception;

import com.alizarion.reference.exception.ApplicationException;

/**
 * @author selim@openlinux.fr.
 */
public  class SecurityException extends
        ApplicationException  {

    private static final long serialVersionUID = -2080434103508047819L;


    public SecurityException(String msg) {
        super(msg);
    }

    public SecurityException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SecurityException(Throwable cause) {
        super(cause);
    }
}
