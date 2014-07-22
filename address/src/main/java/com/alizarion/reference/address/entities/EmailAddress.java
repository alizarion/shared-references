package com.alizarion.reference.address.entities;

import com.alizarion.reference.staticparams.StaticParam;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(catalog = StaticParam.CATALOG, name = "email_address")
@DiscriminatorValue(value = EmailAddress.TYPE)
@PrimaryKeyJoinColumn(name = "email_address_id")
public class EmailAddress extends Address implements Serializable {

    public final static String TYPE = "email";

    @Column(name = "email_address",unique = true)
    private String emailAddress;

    public EmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public EmailAddress() {
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EmailAddress)) return false;

        EmailAddress that = (EmailAddress) o;

        if (emailAddress != null ? !emailAddress.equals(
                that.emailAddress) : that.emailAddress != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return emailAddress != null ? emailAddress.hashCode() : 0;
    }
}
