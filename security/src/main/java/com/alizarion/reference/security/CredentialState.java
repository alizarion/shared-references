package com.alizarion.reference.security;

/**
 * Created by sphinx on 22/07/14.
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
