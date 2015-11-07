package com.alizarion.reference.security.entities;

import com.alizarion.reference.security.oauth.oauth2.entities.Token;
import com.alizarion.reference.security.tools.SecurityHelper;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Token used to reset the credential password.
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_token_reset_password")
@NamedQueries({@NamedQuery(
        name = ResetPasswordToken.FIND_TOKEN_BY_VALUE,
        query = "select rpt from ResetPasswordToken rpt" +
                " where rpt.token.value = :tokenValue")
})
public class ResetPasswordToken implements Serializable{

    public final static String TYPE = "reset-password-token";

    public final static String FIND_TOKEN_BY_VALUE  = "ResetPasswordToken.FIND_TOKEN_BY_VALUE" ;

    private static final long serialVersionUID = -7429964200621113257L;

    @Id
      @TableGenerator(
              name = "security_token_reset_password_SEQ",
              pkColumnName = "SEQ_NAME",
              valueColumnName = "SEQ_COUNT",
              table = "sequence")
      @GeneratedValue(strategy = GenerationType.TABLE,
              generator = "security_token_reset_password_SEQ")
      private Long id;

    @ManyToOne
    private Credential credential;

    @Embedded
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

    public Token getToken() {
        return token;
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
