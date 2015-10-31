package com.alizarion.reference.security.oauth.services.oauth;

import com.alizarion.reference.security.oauth.oauth2.entities.OAuthCryptAlgFamily;
import com.alizarion.reference.security.oauth.oauth2.entities.server.OAuthSignatureKeyPair;
import com.alizarion.reference.security.oauth.oauth2.exception.OAuthOpenIDSignatureException;
import com.alizarion.reference.security.oauth.services.resources.CipherHelper;
import com.alizarion.reference.security.oauth.services.resources.OAuthServerMBean;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.ejb.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * @author selim@openlinux.fr.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CipherSecurityInitializer implements Serializable {

    private static final long serialVersionUID = -390686610385733369L;

    /**
     * KeyStore name, KeyStore is used to store AES SecretKey, that is used to
     *  encrypt persisted private key used to sign OpenId token(JWT)
     */
    private static final String KEY_STORE_FILE_RESOURCE_FILE = "keystore.jck";

    /**
     * Encryption algorithm used to sign  OpenID token(JWT)
     */
    public final static OAuthCryptAlgFamily CRYPT_ALG = OAuthCryptAlgFamily.RSA;

    /**
     * OpenID(JWT) sign algorithm.
     */
    public final static String SIGN_ALG = "RS256";

    /**
     * OpenID(JWT) sign key length.
     */
    public final static int ID_TOKEN_SIGN_KEY_SIZE = 1024;

    /**
     *  KeyStore is used to store AES SecretKey, that is used to
     *  encrypt persisted private key used to sign OpenId token(JWT)
     */
    private KeyStore keyStore;

    /**
     *  KeyStore(password) is used to store AES SecretKey, that is used to
     *  encrypt persisted private key used to sign OpenId token(JWT)
     */
    private static final String KEY_STORE_PASSWORD =  "store123";


    /**
     * AES SecretKey Password, AES key used to encrypt persisted private
     * key used to sign OpenId token(JWT).
     */
    private static final String KEY_STORE_OAUTH_ENTRY_PASSWORD = "key123";

    /**
     * OAuth service.
     */
    @EJB
    OAuthServerMBean serverMBean;


    private KeyStore getKeyStore()
            throws CertificateException,
            NoSuchAlgorithmException,
            IOException {
        if (this.keyStore ==null){
            try {
                this.keyStore = KeyStore.getInstance("jceks");
                FileInputStream fileInputStream = new FileInputStream(getKeyStoreURI().toString());
                getKeyStore().load(fileInputStream, KEY_STORE_PASSWORD.toCharArray());
                fileInputStream.close();
            } catch (Exception e) {
                this.keyStore.load(null,KEY_STORE_PASSWORD.toCharArray());
            }

        }
        return keyStore;
    }


    public Key getAESKey() throws OAuthOpenIDSignatureException {
        try {
            this.keyStore = getKeyStore();

            KeyStore.SecretKeyEntry keyEntry =
                    (KeyStore.SecretKeyEntry) keyStore.getEntry("oauth",
                            new KeyStore.PasswordProtection(KEY_STORE_OAUTH_ENTRY_PASSWORD.toCharArray()));

            if (keyEntry != null) {
                return keyEntry.getSecretKey();
            }

            SecretKey newSecretKey = KeyGenerator.getInstance(OAuthSignatureKeyPair.PRIVATE_KEY_ENCRYPT_KEY_ALG).generateKey();

            keyStore.setEntry("oauth",
                    new KeyStore
                            .SecretKeyEntry(newSecretKey),
                    new KeyStore
                            .PasswordProtection(KEY_STORE_OAUTH_ENTRY_PASSWORD.toCharArray()));
            CipherHelper.writeKeyStore(keyStore,
                    getKeyStoreURI(),
                    KEY_STORE_PASSWORD);
            return newSecretKey;
        } catch (GeneralSecurityException | URISyntaxException | IOException e) {
            throw new OAuthOpenIDSignatureException("error on loading aes SecretKey :" +e );
        }
    }


    private URI getKeyStoreURI() throws URISyntaxException {
        return new URI(this.serverMBean.getKeyStoreFolder().toString()
                + File.separator +
                KEY_STORE_FILE_RESOURCE_FILE);
    }

}
