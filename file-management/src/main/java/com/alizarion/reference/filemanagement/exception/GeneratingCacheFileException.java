package com.alizarion.reference.filemanagement.exception;

import com.alizarion.reference.exception.ApplicationException;

/**
 * @author selim@openlinux.fr.
 */
public class GeneratingCacheFileException extends ApplicationException {

    private static final long serialVersionUID = 4582618956518551859L;

    public GeneratingCacheFileException(String msg) {
        super(msg);
    }

    public GeneratingCacheFileException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public GeneratingCacheFileException(Throwable cause) {
        super(cause);
    }
}
