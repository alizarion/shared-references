package com.alizarion.reference.social.jpa;

import com.alizarion.reference.social.entities.notification.Observer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "account_test")
public class Account extends Observer implements  Serializable {



    @Column
    private String firstName;

    @Column
    private String lastName;


    @Override
    public Long getId() {
        return super.getId();
    }
}
