package com.alizarion.reference.location.entities;

import javax.persistence.*;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "location_geographical_address")
@DiscriminatorValue(value = "geographical")
@PrimaryKeyJoinColumn(name = "geographical_address_id")
@NamedQueries({@NamedQuery(
        name = PhysicalAddress.FIND_BY_PART,
        query = "select pa from PhysicalAddress pa where" +
                " pa.name like :part or " +
                "pa.street like :part"),
        @NamedQuery(
                name = PhysicalAddress.
                        FIND_BY_PART_WITH_COUNTRY_ZIP,
                query = "select pa from PhysicalAddress pa where " +
                        "pa.name like :part  and pa.street" +
                        " like :part and pa.zipCode like :zipCode" +
                        " and pa.country.id like :countryId")})
public class PhysicalAddress extends Address {

    public static final String  FIND_BY_PART =
            "PhysicalAddress.FIND_BY_PART";

    public static final String
            FIND_BY_PART_WITH_COUNTRY_ZIP =
            "PhysicalAddress." +
                    "FIND_BY_PART_WITH_COUNTRY_ZIP";

    @Column(name = "address_name",
            nullable = true)
    private String name;


    @Column(name = "street",nullable = false)
    private String street;

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
        this.street = street;
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
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

        return !(street != null ? !street.equals(that.street)
                : that.street != null) &&
                !(country != null ?
                        !country.equals(that.country) :
                        that.country != null) &&
                !(name != null ? !name.equals(that.name) :
                        that.name != null) &&
                !(zipCode != null ? !zipCode.equals(that.zipCode) :
                        that.zipCode != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (street != null
                ? street.hashCode() : 0);
        result = 31 * result + (zipCode != null
                ? zipCode.hashCode() : 0);
        result = 31 * result + (country != null
                ? country.hashCode() : 0);
        return result;
    }
}
