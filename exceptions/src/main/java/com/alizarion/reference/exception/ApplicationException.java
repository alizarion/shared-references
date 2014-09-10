package com.alizarion.reference.exception;

/**
 * Main checked exception, must be extended by all checked exceptions.
 * @author selim@openlinux.fr.
 */
public class ApplicationException extends Exception {


    private static final long serialVersionUID = 3622814533011061667L;

    /**
     * Initialize checked exceptions explicitly caught or propagated  message.
     * @param msg that describe de exception
     */
    public ApplicationException(String msg){
        super(msg);
    }

    /**
     * Initialize checked exceptions explicitly caught or propagated  message, and his cause.
     * @param msg that describe de exception
     * @param cause of this exception
     */
    public ApplicationException(String msg, Throwable cause){
        super(msg,cause);
    }

    /**
     * Initialize checked exceptions explicitly caught or propagated  cause.
     * @param cause of this exception
     */
    public ApplicationException(Throwable cause){
        super(cause);
    }
}
