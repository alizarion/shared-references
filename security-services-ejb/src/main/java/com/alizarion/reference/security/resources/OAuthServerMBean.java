package com.alizarion.reference.security.resources;

import com.alizarion.reference.resource.exception.PersistentResourceNotFoundException;
import com.alizarion.reference.resource.mbean.PersistentMBean;

import javax.ejb.Stateless;
import java.util.ResourceBundle;

/**
 * @author selim@openlinux.fr.
 */
@Stateless
public class OAuthServerMBean extends PersistentMBean {

    private static final long serialVersionUID = -2814861837506911399L;

    public static final String CATEGORY =
            "com.alizarion.reference.security.oauth.server";

    public static final String REFRESH_TOKEN_DURATION_SECOND =
            "refresh-token-duration-second";

    public static final String ACCESS_TOKEN_DURATION_SECOND =
            "access-token-duration-second";

    ResourceBundle bundle = ResourceBundle.getBundle("oauth");

    @Override
    public String getCategory() {
        return CATEGORY;
    }

    public Long getRefreshTokenDurationSecond(){
        try {
            return  Long.parseLong(
                    getValue(REFRESH_TOKEN_DURATION_SECOND));
        } catch (PersistentResourceNotFoundException e) {
            return  Long.parseLong(
                    bundle.getString(REFRESH_TOKEN_DURATION_SECOND));
        }
    }

    /**
     * Method to get access token duration value  in second
     * @return
     */
    public Long getAccessTokenDurationSecond(){
        try {
            return Long.parseLong(
                    getValue(REFRESH_TOKEN_DURATION_SECOND));
        } catch (PersistentResourceNotFoundException e) {
            return  Long.parseLong(
                    bundle.getString(REFRESH_TOKEN_DURATION_SECOND));

        }
    }
}
