package com.alizarion.reference.security.oauth.oauth2.entities.client;

import com.alizarion.reference.security.oauth.oauth2.entities.OAuthApplication;
import com.alizarion.reference.security.oauth.oauth2.entities.OAuthCryptAlgFamily;
import com.alizarion.reference.security.oauth.oauth2.entities.OAuthSignature;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_oauth_cached_jwk_certificate")
public class OAuthCachedJWKCertificate extends OAuthSignature {

    private static final long serialVersionUID = 4757994791704334573L;

    @ManyToOne(optional = false)
    private OAuthServerApplication application;

    protected OAuthCachedJWKCertificate() {

    }

    public OAuthCachedJWKCertificate(final OAuthCryptAlgFamily cryptAlgFamily,
                                     final String publicKey,
                                     final String signatureAlgorithm,
                                     final OAuthApplication application,
                                     final Long duration) {
        super(cryptAlgFamily, signatureAlgorithm,duration);
        this.publicKey = publicKey;
        this.application = (OAuthServerApplication) application;
    }


    public OAuthServerApplication getApplication() {
        return application;
    }

    public void setApplication(OAuthServerApplication application) {
        this.application = application;
    }


}
