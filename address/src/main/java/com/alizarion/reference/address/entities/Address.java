package com.alizarion.reference.address.entities;

/**
 * @author selim@openlinux.fr.
 */

import com.alizarion.reference.staticparams.StaticParam;

import javax.persistence.*;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@NamedQuery(name = Address.FIND_ALL,query = "select a from Address a")
@Table(catalog = StaticParam.CATALOG,name = "address")
@DiscriminatorColumn(name = "type")
public abstract class Address {

    public static final String FIND_ALL = "Address.FIND_ALL";

    @Id
    @TableGenerator(name="Address_SEQ", table="sequence",catalog = StaticParam.CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="Address_SEQ")
    @Column
    private Long id;

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Address)) return false;

        Address address = (Address) o;

        if (id != null ? !id.equals(address.id) :
                address.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
