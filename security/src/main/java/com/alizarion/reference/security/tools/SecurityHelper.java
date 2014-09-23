package com.alizarion.reference.security.tools;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        String result = value;
        if(value != null) {
            MessageDigest md ;
            try {
                md = MessageDigest.getInstance("SHA-1"); //or "MD5"
                md.update(value.getBytes());
                BigInteger hash = new BigInteger(1, md.digest());
                result = hash.toString(16);
                while(result.length() < 32) {
                    result = "0" + result;
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
