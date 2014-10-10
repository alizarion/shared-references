package com.alizarion.reference.security.tools;

import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Simple security helper
 * @author selim@openlinux.fr.
 */
public class SecurityHelper {

    //TODO  implement 3rd party auth @see https://github.com/keycloak/keycloak and OAuth


    public static String getRandomAlphaNumericString(Integer bitsLength){
        SecureRandom random =  new SecureRandom();
        return new BigInteger(bitsLength,random).toString(32);
    }

    public static String getSHA1Value(String value){
       return DigestUtils.sha1Hex(value);
    }
}
