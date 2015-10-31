package com.alizarion.reference.security.oauth.oauth2.toolkit;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * @author selim@openlinux.fr.
 */
public class OAuthSecurityContext implements SecurityContext {


    private TokenInfoDTO tokenInfo;

    @Override
    public Principal getUserPrincipal() {
        return new Principal() {
            @Override
            public String getName() {
                return tokenInfo.getUserId();
            }
        };
    }

    public OAuthSecurityContext(final TokenInfoDTO tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    @Override
    public boolean isUserInRole(final String role) {
        return tokenInfo.getScope().contains(role);
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return null;
    }
}
