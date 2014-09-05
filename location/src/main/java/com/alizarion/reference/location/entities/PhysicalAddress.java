package com.alizarion.reference.location.entities;

import javax.persistence.*;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "location_geographical_address")
@DiscriminatorValue(value = "geographical")
@PrimaryKeyJoinColumn(name = "geographical_address_id")
public class PhysicalAddress extends Address {

    @Column(name = "address_name",
            nullable = true)
    private String name;


    @Column(name = "street",nullable = false)
    private String Street;

    @Column(name = "postal_code",
            nullable = false,
            length = 11)
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "country_id",nullable = false)
    private Country country;

    public PhysicalAddress(){

    }

    public PhysicalAddress(String street,
                           String zipCode,
                           Country country) {
        Street = street;
        this.zipCode = zipCode;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PhysicalAddress)) return false;

        PhysicalAddress that = (PhysicalAddress) o;

        if (Street != null ? !Street.equals(that.Street)
                : that.Street != null) return false;
        if (country != null ? !country.equals(that.country)
                : that.country != null) return false;
        if (name != null ? !name.equals(that.name)
                : that.name != null) return false;
        if (zipCode != null ? !zipCode.equals(that.zipCode)
                : that.zipCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (Street != null
                ? Street.hashCode() : 0);
        result = 31 * result + (zipCode != null
                ? zipCode.hashCode() : 0);
        result = 31 * result + (country != null
                ? country.hashCode() : 0);
        return result;
    }
}
