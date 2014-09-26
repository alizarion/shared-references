package com.alizarion.reference.resource.exception;

import com.alizarion.reference.exception.ApplicationException;

/**
 *
 * @author selim@openlinux.fr.
 */
public class PersistentResourceNotFoundException extends ApplicationException {

    private static final long serialVersionUID = 1825333419532206846L;

    public PersistentResourceNotFoundException(String msg) {
        super(msg);
    }

    public PersistentResourceNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PersistentResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
