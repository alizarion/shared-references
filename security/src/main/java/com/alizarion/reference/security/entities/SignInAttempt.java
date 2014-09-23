package com.alizarion.reference.security.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Class used to log all log-in attempts.
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_sign_in_attempt_log")
public class SignInAttempt implements Serializable {

    private static final long serialVersionUID = 7779486902957845789L;

    @Id
    @TableGenerator(name = "security_sign_in_attempt_SEQ",
            pkColumnName = "SEQ_NAME",
            pkColumnValue = "SEQ_VALUE",
            table = "sequence")
    @GeneratedValue(generator ="security_sign_in_attempt_SEQ",
            strategy = GenerationType.TABLE)
    @Column(unique = true)
    private Long id;

    /**
     *  Username that used to sign in.
     */
    @Column(nullable = false)
    private String userName;

    /**
     * Attempting date time.
     */
    @Column(name = "attempt_time",nullable = false)
    private Date attemptingDate;

    /**
     * is that attempt success
     */
    @Column(name = "attempt_success",nullable = false)
    private Boolean success;

    /**
     * Attempting ip from.
     */
    @Column(name = "from_ip")
    private String internetAddress;

    public SignInAttempt(final String login,
                         final Date attemptingDate,
                         final Boolean success,
                         final String ip) {
        this.userName = login;
        this.internetAddress = ip;
        this.attemptingDate = attemptingDate;
        this.success = success;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAttemptingDate() {
        return attemptingDate;
    }

    public void setAttemptingDate(Date attemptingDate) {
        this.attemptingDate = attemptingDate;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getUserName() {
        return userName;
    }

    public String getInternetAddress() {
        return internetAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SignInAttempt)) return false;
        SignInAttempt that = (SignInAttempt) o;
        return !(attemptingDate != null ?
                !attemptingDate.equals(that.attemptingDate) :
                that.attemptingDate != null) &&
                !(id != null ? !id.equals(that.id) :
                        that.id != null) &&
                !(success != null ?
                        !success.equals(that.success) :
                        that.success != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (attemptingDate != null ?
                attemptingDate.hashCode() : 0);
        result = 31 * result + (success != null ?
                success.hashCode() : 0);
        return result;
    }
}
