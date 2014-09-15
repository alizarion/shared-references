package com.alizarion.reference.filemanagement.exception;

import com.alizarion.reference.exception.ApplicationException;

/**
 * @author selim@openlinux.fr.
 */
public class ManagedImageFileDataVisitorException extends ApplicationException {


    private static final long serialVersionUID = -1520327575170441704L;

    public ManagedImageFileDataVisitorException(String msg) {
        super(msg);
    }

    public ManagedImageFileDataVisitorException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ManagedImageFileDataVisitorException(Throwable cause) {
        super(cause);
    }
}
