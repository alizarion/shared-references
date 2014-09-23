package com.alizarion.reference.security.entities;

/**
 *
 * @author selim@openlinux.fr
 */
public enum CredentialState {

    A("ACTIVATED"),
    D("DISABLED"),
    P("PENDING");

    private String state;

    CredentialState(final String state) {
        this.state = state;
    }

    public String getStateKey() {
        return this.state;
    }
}
