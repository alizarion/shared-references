package com.alizarion.reference.showcase.web.entities;

import com.alizarion.reference.person.entities.PhysicalPerson;
import com.alizarion.reference.security.entities.Credential;
import com.alizarion.reference.social.entities.notification.Observer;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author selim@openlinux.fr.
 */

@Entity
@Table(name = "showcase_user")
public class User extends Observer {
    private static final long serialVersionUID = -2639637596668757978L;

    @OneToOne
    private Profile profile;

    @OneToOne
    private Credential credential;

    @OneToOne
    private PhysicalPerson physicalPerson;
}


