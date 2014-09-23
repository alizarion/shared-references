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

    private static final long serialVersionUID = 9090733275640921352L;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "title",length = 5)
    private Enum Title;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Enum getTitle() {
        return Title;
    }

    public void setTitle(Enum title) {
        Title = title;
    }
}
