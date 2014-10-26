package com.alizarion.reference.security.oauth.oauth2.entities;

import org.apache.commons.codec.binary.Base64;

import javax.persistence.*;
import java.io.Serializable;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

/**
 * Class that contain key pair user to sign openid token.
 * @author selim@openlinux.fr.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class OAuthSignature implements Serializable, Comparable<OAuthSignature> {


    private static final long serialVersionUID = -8617002268356892826L;


    @Id
    @TableGenerator(
            name = "security_oauth_signature_SEQ",
            table = "sequence",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT")
    @GeneratedValue(
            generator ="security_oauth_signature_SEQ",
            strategy = GenerationType.TABLE)
    private Long id;

    /**
     * public key
     */
    @Column(name = "public_key",nullable = false,length = 4096)
    protected String publicKey;


    /**
     * used algorithm to generate key pairs
     */
    @Column(name = "cryptographic_algorithm",nullable = false)
    @Enumerated(EnumType.STRING)
    protected OAuthCryptAlgFamily cryptAlgFamily;


    /**
     * used algorithm to sign
     */
    @Column(name = "signature_algorithm")
    protected String signatureAlgorithm;

    @Column(name = "creation_date" ,
            updatable = false,
            nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "expire_date" ,
            nullable = false,
            updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireDate;

    protected OAuthSignature() {
    }

    protected OAuthSignature(
            final OAuthCryptAlgFamily cryptAlgFamily,
            final String signatureAlgorithm,
            final Long duration) {
        this.cryptAlgFamily = cryptAlgFamily;
        this.signatureAlgorithm = signatureAlgorithm;
        this.creationDate = new Date();
        this.expireDate = new Date(
                this.getCreationDate()
                        .getTime()
                        +(duration*1000));
    }

    public String getPublicKeyBase64String() {
        return publicKey;
    }

    public PublicKey getPublicKey() throws
            NoSuchAlgorithmException,
            InvalidKeySpecException {
        byte[] puk = Base64.decodeBase64(this.publicKey) ;
        KeyFactory kf = KeyFactory.getInstance(this.cryptAlgFamily.name());
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(puk);
        return kf.generatePublic(publicKeySpec);
    }


    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public OAuthCryptAlgFamily getCryptAlgFamily() {
        return cryptAlgFamily;
    }

    public void setCryptAlgFamily(OAuthCryptAlgFamily cryptAlgFamily) {
        this.cryptAlgFamily = cryptAlgFamily;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void revoke(){
        this.expireDate =  new Date();
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OAuthSignature)) return false;

        OAuthSignature that = (OAuthSignature) o;

        return cryptAlgFamily == that.cryptAlgFamily &&
                !(publicKey != null ? !publicKey.equals(that.publicKey) :
                        that.publicKey != null) &&
                !(signatureAlgorithm != null ?
                        !signatureAlgorithm.equals(that.signatureAlgorithm) :
                        that.signatureAlgorithm != null);

    }

    @Override
    public int hashCode() {
        int result = publicKey != null ? publicKey.hashCode() : 0;
        result = 31 * result + (cryptAlgFamily != null ?
                cryptAlgFamily.hashCode() : 0);
        result = 31 * result + (signatureAlgorithm != null ?
                signatureAlgorithm.hashCode() : 0);
        return result;
    }


    public int compareTo( OAuthSignature  oAuthSignature){

        if (this.expireDate.after(oAuthSignature.getExpireDate())){
            return 1;
        }
        else if (this.expireDate.before(oAuthSignature.getExpireDate())){
            return -1;
        }  else {
            return 0;
        }
    }


    public boolean hasExpire(){
        if(this.expireDate != null){
            if (this.expireDate.before(new Date())){
                return true;
            }
        }
        return false;
    }
}
