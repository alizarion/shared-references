package com.alizarion.reference.social.jpa;

import com.alizarion.reference.social.entities.notification.Observer;
import com.alizarion.reference.staticparams.StaticParam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(catalog = StaticParam.CATALOG, name = "account_test")
public class Account extends Observer implements  Serializable {



    @Column
    private String firstName;

    @Column
    private String lastName;





}
