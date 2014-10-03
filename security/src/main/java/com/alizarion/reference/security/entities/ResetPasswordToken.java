package com.alizarion.reference.security.entities;

import com.alizarion.reference.security.tools.SecurityHelper;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Token used to reset the credential password.
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_token_reset_password")
public class ResetPasswordToken implements Serializable{

    public final static String TYPE = "reset-password-token";

    private static final long serialVersionUID = -7429964200621113257L;

    @ManyToOne
    private Credential credential;

    @EmbeddedId
    private Token token;

    protected ResetPasswordToken() {
    }

    /**
     * instantiate new reset password token
     * @param duration time to keep token , in seconds.
     * @param credential credential.
     */
    public ResetPasswordToken(final long duration,Credential credential) {
        this.token = new Token(duration,
                SecurityHelper.getRandomAlphaNumericString(130));
        this.credential = credential;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    @Override
    public String toString() {
        return "ResetPasswordToken{" +
                "credential=" + credential +
                '}';
    }
}
