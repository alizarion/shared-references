package com.alizarion.reference.person.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "moral_person")
@DiscriminatorValue(value = "moral")
@PrimaryKeyJoinColumn(name = "moral_person_id")
public class MoralPerson extends Person {


    private static final long serialVersionUID = -4319034734820858415L;
}
