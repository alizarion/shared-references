package com.alizarion.reference.security;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CredentialTest {

    private Credential credential;

    @Before
    public void init(){
        credential = new Credential();
        credential.setPassword("TOTO");
    }

    @Test
    public void setCredentialSha1Password(){
        Credential newCredential = new Credential();
        newCredential.setPassword("TOTO");
        assertEquals(newCredential.getPassword(),credential.getPassword());

    }

}