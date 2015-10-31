package com.alizarion.reference.person.entities;


import com.alizarion.reference.location.entities.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract definition of business person
 * @author selim@openlinux.fr.
 */
@Entity
@NamedQuery(name = Person.FIND_ALL,
        query = "select p from Person p")
@Table(name = "person_person")
public  class Person implements Serializable {

    public final static String FIND_ALL = "Person.FIND_ALL";

    private static final long serialVersionUID = -2272558289030075762L;

    @Id
    @TableGenerator(name="person_person_SEQ", table="sequence",
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="person_person_SEQ")
    @Column
    private Long id;

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "primary_email_id")
    private ElectronicAddress primaryElectronicAddress;

    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "primary_phone_id")
    private PhoneAddress primaryPhoneAddress;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "primary_address_id")
    private PhysicalAddress primaryPhysicalAddress;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    private Set<Address> secondaryAddresses = new HashSet<>();


    public ElectronicAddress getPrimaryElectronicAddress() {
        return this.primaryElectronicAddress;
    }

    public void setPrimaryElectronicAddress(
            final ElectronicAddress primaryElectronicAddress) {
        this.primaryElectronicAddress = primaryElectronicAddress;
    }

    public Long getId() {
        return id;
    }

    public PhoneAddress getPrimaryPhoneAddress() {
        return primaryPhoneAddress;
    }

    protected void setPrimaryPhoneAddress(
            final PhoneAddress primaryPhoneAddress) {
        this.primaryPhoneAddress = primaryPhoneAddress;
    }

    public PhysicalAddress getPrimaryPhysicalAddress() {
        return primaryPhysicalAddress;
    }

    protected void setPrimaryPhysicalAddress(
            final PhysicalAddress primaryPhysicalAddress) {
        this.primaryPhysicalAddress = primaryPhysicalAddress;
    }

    public Set<Address> getSecondaryAddresses() {
        return secondaryAddresses;
    }

    public void setSecondaryAddresses(Set<Address> secondaryAddresses) {
        this.secondaryAddresses = secondaryAddresses;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", primaryElectronicAddress=" + primaryElectronicAddress +
                ", primaryPhoneAddress=" + primaryPhoneAddress +
                ", primaryPhysicalAddress=" + primaryPhysicalAddress +
                ", secondaryAddresses=" + secondaryAddresses +
                '}';
    }
}
