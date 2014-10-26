package com.alizarion.reference.security.oauth.oauth2.entities.server;

import com.alizarion.reference.security.oauth.oauth2.entities.OAuthCryptAlgFamily;
import com.alizarion.reference.security.oauth.oauth2.entities.OAuthSignature;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.UUID;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_oauth_server_signature_key_pair")
@NamedQuery(name = OAuthSignatureKeyPair.FIND_ALIVE,
        query = "select kp from OAuthSignatureKeyPair kp where" +
                " kp.expireDate is not null and kp.expireDate > :today")
public class OAuthSignatureKeyPair extends OAuthSignature {

    private static final long serialVersionUID = -3393276404725075374L;

    public static final String PRIVATE_KEY_ENCRYPT_KEY_ALG = "AES";


    /**
     * AES key length used to encrypt persisted private
     * key used to sign OpenId token(JWT).
     */
    public static final int PRIVATE_KEY_LENGTH = 1024;


    public static final String FIND_ALIVE =  "OAuthSignatureKeyPair.FIND_ALIVE";


    /**
     * Will be used as kid in json web key.
     */
    @Column(name = "kid",
            nullable = false,
            unique = true,
            updatable = false)
    private String kid;

    protected OAuthSignatureKeyPair() {

    }

    /**
     * private keys are encoded with advanced encryption standard algorithm
     * first it hast been encrypted with aes key,
     * and secondly base64 encoded to be stored in db.
     */
    @Column(name = "private_key_encrypted",nullable = false,length = 4096)
    private String privateKeyAesEncrypted;

    public OAuthSignatureKeyPair(final Key aesCryptKey,
                                 final OAuthCryptAlgFamily cryptAlgFamily,
                                 final String signatureAlgorithm,
                                 final long duration)
            throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException {
        super(cryptAlgFamily,signatureAlgorithm,duration);
        KeyPairGenerator generator = KeyPairGenerator.getInstance(
                cryptAlgFamily.name());
        generator.initialize(PRIVATE_KEY_LENGTH);

        KeyPair keyPair=  generator.genKeyPair();
        this.publicKey = new String(
                Base64.encodeBase64URLSafe(
                        keyPair.getPublic().getEncoded()));
        //for safety reason we encrypt the persisted private key
        //set encrypt algorithm
        Cipher encrypt =  Cipher.getInstance(PRIVATE_KEY_ENCRYPT_KEY_ALG);
        // base64 decode the aes secret key


        encrypt.init(Cipher.ENCRYPT_MODE,
                aesCryptKey);

        this.privateKeyAesEncrypted  = new String(Base64
                .encodeBase64(
                        encrypt.doFinal(
                                keyPair
                                        .getPrivate()
                                        .getEncoded())));

        this.cryptAlgFamily = cryptAlgFamily;
        this.signatureAlgorithm = signatureAlgorithm;
        this.kid = UUID.randomUUID().toString();
    }


    public PrivateKey decryptPrivateKey(final Key aesCryptKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            BadPaddingException,
            IllegalBlockSizeException, InvalidKeyException
            , InvalidKeySpecException {
        Cipher cipher = Cipher.getInstance(PRIVATE_KEY_ENCRYPT_KEY_ALG);
        cipher.init(Cipher.DECRYPT_MODE,
                aesCryptKey);
        byte[] encodedPK = cipher.doFinal(Base64.decodeBase64(
                this.privateKeyAesEncrypted.getBytes())) ;
        KeyFactory kf = KeyFactory.getInstance(this.cryptAlgFamily.name());
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPK);

        return kf.generatePrivate(privateKeySpec);

    }



    /**
     * Method to get half time life of the cert, used to reset.
     * @return time in millisecond
     */
    public Long getHalfLife(){
        return (this.getExpireDate().getTime() - this.getCreationDate().getTime())/2;
    }

    public String getPrivateKeyAesEncrypted() {
        return privateKeyAesEncrypted;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }
}
