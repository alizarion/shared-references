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

    private static final long serialVersionUID = -3137992525091670060L;


    @Column(name = "number",nullable = false,length = 50)
    private String number;

    @ManyToOne
    @JoinColumn(name="country_id", nullable = false)
    private Country country;

    @Enumerated(EnumType.STRING)
    @Column(name = "phone_type")
    private PhoneType phoneType;

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

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PhoneAddress)) return false;
        if (!super.equals(o)) return false;

        PhoneAddress that = (PhoneAddress) o;

        return !(country != null ? !country.equals(that.country) :
                that.country != null) &&
                !(number != null ? !number.equals(that.number) :
                        that.number != null) &&
                phoneType == that.phoneType;

    }

    @Override
       public String getType() {
           return TYPE;
       }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (phoneType != null ? phoneType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PhoneAddress{" +
                "number='" + number + '\'' +
                ", country=" + country +
                ", phoneType=" + phoneType +
                '}';
    }
}
