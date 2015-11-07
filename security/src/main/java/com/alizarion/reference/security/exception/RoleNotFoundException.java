package com.alizarion.reference.security.exception;

/**
 * @author selim@openlinux.fr.
 */
public class RoleNotFoundException extends SecurityException {


    private static final long serialVersionUID = 5943029514383805538L;

    public RoleNotFoundException(String msg) {
        super(msg);
    }

    public RoleNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RoleNotFoundException(Throwable cause) {
        super(cause);
    }
}
