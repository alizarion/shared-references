package com.alizarion.reference.security;
import com.alizarion.reference.security.entities.Credential;
import org.junit.Assert;
import org.junit.Before;


public class CredentialTest {

    private Credential credential;

    @Before
    public void init(){
        //TODO corriger le test
     //   credential = new Credential();
      //  credential.setPassword("TOTO");
    }

   // @Test
    public void setCredentialSha1Password(){

        Assert.assertTrue(credential.
                isCorrectPassword("TOTO"));


    }

}