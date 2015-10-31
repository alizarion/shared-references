package com.alizarion.reference.location.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "test_simple_entity")
public class SimpleEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private static final long serialVersionUID = -4007806950819254509L;

    public SimpleEntity() {
    }

    @OneToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "email_id")
    private  ElectronicAddress electronicAddress;

    public SimpleEntity(final ElectronicAddress electronicAddress) {
        this.electronicAddress = electronicAddress;
    }

    public ElectronicAddress getElectronicAddress() {
        return electronicAddress;
    }

    public void setElectronicAddress(final ElectronicAddress electronicAddress) {
        this.electronicAddress = electronicAddress;
    }
}
