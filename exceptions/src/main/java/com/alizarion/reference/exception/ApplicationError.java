package com.alizarion.reference.exception;

/**
 * Main class that describe unchecked exceptions, must be extended by all unchecked case.
 * @author selim@openlinux.fr.
 */
public class ApplicationError extends RuntimeException {

    private static final long serialVersionUID = -8135126337321695386L;

    /**
     * Initialize unchecked error.
     * @param msg message that describe the error.
     */
    public ApplicationError(String msg){
        super(msg);
    }

    /**
     * Initialize unchecked error.
     * @param msg message that describe the error.
     * @param cause of this error.
     */
    public ApplicationError(String msg,Throwable cause ){
        super(msg,cause);
    }
}
