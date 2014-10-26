package com.alizarion.reference.showcase.web.entities;

import com.alizarion.reference.social.entities.notification.Notification;
import com.alizarion.reference.social.entities.notification.Subject;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "showcase_profile")
public class Profile extends Subject {


    private static final long serialVersionUID = 7697090716196859385L;


    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<ImageContent> imageContent =  new HashSet<>();

    @Override
    public void notifyObservers(Notification notification) {
        //TODO a implémenter
    }

    @Override
    public void notifyOwner(Notification notification) {
        //TODO a implémenter

    }

    @Override
    public String getSubjectType() {
        //TODO a implémenter

        return null;
    }
}
