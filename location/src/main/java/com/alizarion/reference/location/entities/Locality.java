package com.alizarion.reference.location.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table( name = "location_locality")
public class Locality  implements Serializable {

    private static final long serialVersionUID = 4474592091421934227L;

    @Id
    @Column(name = "locality_id")
    private String locality;

    protected Locality() {
       }

    public Locality(String locality) {
        this.locality = locality;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Locality)) return false;

        Locality locality1 = (Locality) o;

        return !(locality != null ?
                !locality.equals(locality1.locality) :
                locality1.locality != null);

    }

    @Override
    public int hashCode() {
        return locality != null ? locality.hashCode() : 0;
    }
}
