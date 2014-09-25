package com.alizarion.reference.location.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "location_email_address")
@DiscriminatorValue(value = ElectronicAddress.TYPE)
@PrimaryKeyJoinColumn(name = "email_address_id")
public class ElectronicAddress extends Address implements Serializable {

    private static final long serialVersionUID = 3778051241478489845L;

    public final static String TYPE = "email";

    @Column(name = "email_address",unique = true)
    private String emailAddress;

    public ElectronicAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public ElectronicAddress() {
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ElectronicAddress)) return false;

        ElectronicAddress that = (ElectronicAddress) o;

        if (emailAddress != null ? !emailAddress.equals(
                that.emailAddress) : that.emailAddress != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return emailAddress != null ? emailAddress.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ElectronicAddress{" +
                "emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
