package com.alizarion.reference.security;
import com.alizarion.reference.security.entities.Credential;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class CredentialTest {

    private Credential credential;

    @Before
    public void init(){
        credential = new Credential();
        credential.setPassword("TOTO");
    }

    @Test
    public void setCredentialSha1Password(){

        Assert.assertTrue(credential.
                isCorrectPassword("TOTO"));


    }

}