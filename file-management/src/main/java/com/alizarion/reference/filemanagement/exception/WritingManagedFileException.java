package com.alizarion.reference.filemanagement.exception;

import com.alizarion.reference.exception.ApplicationException;

/**
 * @author selim@openlinux.fr.
 */
public class WritingManagedFileException extends ApplicationException {

    private static final long serialVersionUID = -2442091188394000454L;

    public WritingManagedFileException(String msg) {
        super(msg);
    }

    public WritingManagedFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public WritingManagedFileException(Throwable cause) {
        super(cause);
    }

}
