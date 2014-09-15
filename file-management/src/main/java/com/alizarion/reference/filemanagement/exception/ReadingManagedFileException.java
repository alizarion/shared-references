package com.alizarion.reference.filemanagement.exception;

import com.alizarion.reference.exception.ApplicationException;

/**
 * @author selim@openlinux.fr.
 */
public class ReadingManagedFileException extends ApplicationException {

    private static final long serialVersionUID = -4735365080227640450L;

    public ReadingManagedFileException(String msg){
        super(msg);
    }

    public ReadingManagedFileException(String msg,Throwable cause){
        super(msg,cause);

    }

    public ReadingManagedFileException(Throwable cause){
            super(cause);

        }
}
