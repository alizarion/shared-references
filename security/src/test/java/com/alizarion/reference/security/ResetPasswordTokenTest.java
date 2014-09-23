package com.alizarion.reference.security;

import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;

public class ResetPasswordTokenTest {


    @Test
    public void resetPasswordTest(){
        //TODO ecrire les tests
         SecureRandom random = new SecureRandom();
        BigInteger bigInteger =new BigInteger(130, random);
        System.out.println(bigInteger);
        System.out.println(bigInteger.toString(32));

    }

}