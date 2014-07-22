package com.alizarion.reference.person.entities;

import com.alizarion.reference.staticparams.StaticParam;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(catalog = StaticParam.CATALOG, name = "moral_person")
@DiscriminatorValue(value = "moral")
@PrimaryKeyJoinColumn(name = "moral_person_id")
public class MoralPerson extends Person {


}
