package com.alizarion.reference.location.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table( name = "location_phone_address")
@DiscriminatorValue(value = PhoneAddress.TYPE)
@PrimaryKeyJoinColumn(name = "phone_address_id")
public class PhoneAddress extends Address  implements Serializable{

    public final static String TYPE = "phone";


    @Column(name = "number",nullable = false,length = 50)
    private String number;

    @ManyToOne
    @JoinColumn(name="country_id", nullable = false)
    private Country country;

    public PhoneAddress() {

    }


    public PhoneAddress(final String number) {
        this.number = number;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PhoneAddress))
            return false;

        PhoneAddress that = (PhoneAddress) o;

        if (country != null ? !country.equals(that.country)
                : that.country != null) return false;
        if (number != null ? !number.equals(that.number)
                : that.number != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = number != null ?
                number.hashCode() : 0;
        result = 31 * result + (country != null ?
                country.hashCode() : 0);
        return result;
    }
}
