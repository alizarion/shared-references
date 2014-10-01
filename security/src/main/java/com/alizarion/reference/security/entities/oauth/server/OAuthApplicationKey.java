package com.alizarion.reference.security.entities.oauth.server;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author selim@openlinux.fr.
 */
@Embeddable
public class OAuthApplicationKey {

    protected OAuthApplicationKey() {

    }

    public OAuthApplicationKey(final String clientId,
                               final String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Column(name = "client_id",
            nullable = false,
            unique = true,
            updatable = false)
    private String clientId;

    @Column(name = "client_secret",
            nullable = false,
            unique = true)
    private String clientSecret;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @Override
    public String toString() {
        return "OauthApplicationKey{" +
                "clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                '}';
    }
}
