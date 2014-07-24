package com.alizarion.reference.person.entities;


import com.alizarion.reference.address.entities.Address;
import com.alizarion.reference.staticparams.StaticParam;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract definition of business person
 * @author selim@openlinux.fr.
 */
@Entity
@NamedQuery(name = Person.FIND_ALL,query = "select p from Person p")
@Table(catalog =  StaticParam.CATALOG,name = "person")
public  class Person implements Serializable {

    public final static String FIND_ALL = "Person.FIND_ALL";


    @Id
    @TableGenerator(name="Person_SEQ", table="sequence",catalog = StaticParam.CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="Credential_SEQ")
    @Column
    private Long id;

    @OneToMany(fetch = FetchType.EAGER,
            orphanRemoval = true,cascade = CascadeType.ALL)
    private Set<Address> addresses = new HashSet<Address>();





}
