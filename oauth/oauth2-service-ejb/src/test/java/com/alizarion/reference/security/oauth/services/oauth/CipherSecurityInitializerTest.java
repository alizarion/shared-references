package com.alizarion.reference.security.oauth.services.oauth;

import com.alizarion.reference.security.oauth.oauth2.entities.OAuthCryptAlgFamily;
import com.alizarion.reference.security.oauth.oauth2.entities.server.OAuthSignatureKeyPair;
import com.alizarion.reference.security.oauth.oauth2.exception.OAuthOpenIDSignatureException;
import com.alizarion.reference.security.oauth.services.resources.OAuthServerMBean;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.crypto.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

@RunWith(MockitoJUnitRunner.class)
public class CipherSecurityInitializerTest {


    @Mock
    OAuthServerMBean serverMBean;

    @InjectMocks
    private CipherSecurityInitializer securityInitializer;

    @Before
    public void init() throws URISyntaxException {
        Mockito.when(serverMBean.getKeyStoreFolder()).thenReturn(new URI(System.getProperty("java.io.tmpdir")));
        Mockito.when(serverMBean.getKeyPairDurationSecond()).thenReturn(3500L);

    }

    @Test
    public void testKeyPairCrypt() throws OAuthOpenIDSignatureException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        System.out.println("default length " + keyGenerator.generateKey().getEncoded().length);


        OAuthSignatureKeyPair  signatureKeyPair = new OAuthSignatureKeyPair(
                this.securityInitializer.getAESKey(),
                OAuthCryptAlgFamily.RSA,"RS256",3600L);

        PrivateKey privateKey =
                signatureKeyPair.decryptPrivateKey(this.securityInitializer.getAESKey()) ;
        System.out.println("private key base64 " + new String(Base64.encodeBase64URLSafe(privateKey.getEncoded())));

        Assert.assertNotNull(new String(Base64.encodeBase64URLSafe(privateKey.getEncoded())));


    }

}