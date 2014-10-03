package com.alizarion.reference.security.entities;

/**
 * @author selim@openlinux.fr.
 */
public enum LogOnType {

    /**
     * Service any service using oauth
     */
    S("service"),
    /**
     *  Password login.
     */
    P("password");

    private String key;

    LogOnType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
