package com.alizarion.reference.security.oauth.toolkit;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author selim@openlinux.fr.
 */
public class OAuthHelper {

    public static String getRandomAlphaNumericString(Integer bitsLength){
        return new BigInteger(bitsLength,new SecureRandom()).toString(32);
    }

}
