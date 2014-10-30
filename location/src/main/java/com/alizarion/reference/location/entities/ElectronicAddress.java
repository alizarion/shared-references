package com.alizarion.reference.location.entities;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.*;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "location_email_address")
@DiscriminatorValue(value = ElectronicAddress.TYPE)
@PrimaryKeyJoinColumn(name = "email_address_id")
@NamedQueries(
        {@NamedQuery(name =
                ElectronicAddress.FIND_BY_PART,
                query = "select ea from ElectronicAddress ea where " +
                        "ea.emailAddress like :emailPart"),
                @NamedQuery(name =
                        ElectronicAddress.FIND_BY_EMAIL,
                        query = "select ea from ElectronicAddress ea where " +
                                "ea.emailAddress = :email")}
)

public class ElectronicAddress extends Address implements Serializable {

    public static final String FIND_BY_PART =
            "ElectronicAddress.FIND_BY_PART";

    public static final String FIND_BY_EMAIL =
            "ElectronicAddress.FIND_BY_EMAIL";

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

    public InternetAddress getInternetAddress() throws AddressException {
          return new InternetAddress(emailAddress);
      }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ElectronicAddress)) return false;

        ElectronicAddress that = (ElectronicAddress) o;

        return !(emailAddress != null ?
                !emailAddress.equals(
                        that.emailAddress) :
                that.emailAddress != null);

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
