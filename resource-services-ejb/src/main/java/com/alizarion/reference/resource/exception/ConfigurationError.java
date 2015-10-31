package com.alizarion.reference.resource.exception;

import com.alizarion.reference.exception.ApplicationError;

/**
 * @author selim@openlinux.fr.
 */
public class ConfigurationError extends ApplicationError {


    private static final long serialVersionUID = -6012398979305970587L;

    public ConfigurationError(String msg) {
        super(msg);
    }

    public ConfigurationError(String msg, Throwable cause) {
        super(msg, cause);
    }
}
