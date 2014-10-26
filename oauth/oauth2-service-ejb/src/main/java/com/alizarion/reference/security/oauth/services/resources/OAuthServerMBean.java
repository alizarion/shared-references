package com.alizarion.reference.security.oauth.services.resources;

import com.alizarion.reference.resource.exception.PersistentResourceNotFoundException;
import com.alizarion.reference.resource.mbean.PersistentMBean;

import javax.ejb.Stateless;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

/**
 * @author selim@openlinux.fr.
 */
@Stateless
public class OAuthServerMBean extends PersistentMBean {

    private static final long serialVersionUID = -2814861837506911399L;

    public static final String CATEGORY =
            "com.alizarion.reference.security.oauth.services.oauth.server";

    public static final String REFRESH_TOKEN_DURATION_SECOND =
            "refresh-token-duration-second";

    public static final String ACCESS_TOKEN_DURATION_SECOND =
            "access-token-duration-second";


    public static final String KEY_STORE_FOLDER =
            "key-store-folder";


    /**
     * Cipher KeyPair used to sign openid tokens
     */
    public static final String KEY_PAIR_DURATION_SECOND =
            "key-pair-duration-second";

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
     * @return duration in second
     */
    public Long getAccessTokenDurationSecond(){
        try {
            return Long.parseLong(
                    getValue(ACCESS_TOKEN_DURATION_SECOND));
        } catch (PersistentResourceNotFoundException e) {
            return  Long.parseLong(
                    bundle.getString(ACCESS_TOKEN_DURATION_SECOND));

        }
    }

    /**
     * Method to get KeyPair duration
     * @return duration in second
     */
    public Long getKeyPairDurationSecond(){
        try {
            return Long.parseLong(
                    getValue(KEY_PAIR_DURATION_SECOND));
        } catch (PersistentResourceNotFoundException e) {
            return  Long.parseLong(
                    bundle.getString(KEY_PAIR_DURATION_SECOND));

        }
    }

    /**
     * Method to get KeyPair duration
     * @return duration in second
     */
    public URI getKeyStoreFolder() throws URISyntaxException {
        try {
            return new URI(
                    getValue(KEY_STORE_FOLDER));
        } catch (PersistentResourceNotFoundException e) {
            return  new URI(
                    bundle.getString(KEY_STORE_FOLDER));

        }
    }
}
