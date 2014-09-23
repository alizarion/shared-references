package com.alizarion.reference.location.entities;

/**
 * @author selim@openlinux.fr.
 */

import javax.persistence.*;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@NamedQuery(name = Address.FIND_ALL,query = "select a from Address a")
@Table(name = "location_address")
@DiscriminatorColumn(name = "type")
public abstract class Address {

    public static final String FIND_ALL = "Address.FIND_ALL";

    @Id
    @TableGenerator(name="Address_SEQ", table="sequence",
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="Address_SEQ")
    @Column
    private Long id;

    @Column(name = "audited")
    private Boolean audited;

    public Address() {
        this.audited = false;
    }

    public Long getId() {
        return id;
    }


    public Boolean getAudited() {
        return audited;
    }

    public void setAudited(Boolean audited) {
        this.audited = audited;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Address)) return false;

        Address address = (Address) o;

        return !(id != null ? !id.equals(address.id) :
                address.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
