package com.alizarion.reference.security.entities;

import com.alizarion.reference.security.tools.SecurityHelper;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Token used to reset the credential password.
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_token_reset_password")
public class ResetPasswordToken extends Token {

    public final static String TYPE = "reset-password-token";

    private final static long VALID_DAYS = 7;

    private static final long serialVersionUID = -7429964200621113257L;


    @ManyToOne
    private Credential credential;

    public ResetPasswordToken() {
        super();
    }

    public ResetPasswordToken(Credential credential) {
        super(SecurityHelper.getRandomAlphaNumericString(130));
        this.credential = credential;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    @Override
    public long getValid() {
        return VALID_DAYS * 24 * 3600;
    }


}
