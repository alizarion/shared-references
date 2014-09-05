package com.alizarion.reference.person.entities;

import javax.persistence.*;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "physical_person")
@DiscriminatorValue(value = "physical")
@PrimaryKeyJoinColumn(name = "physical_person_id")
public class PhysicalPerson extends Person {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "title",length = 5)
    private Enum Title;



}
