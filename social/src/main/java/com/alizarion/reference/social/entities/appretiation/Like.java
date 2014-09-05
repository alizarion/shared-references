package com.alizarion.reference.social.entities.appretiation;

import com.alizarion.reference.social.entities.notification.Notification;
import com.alizarion.reference.social.entities.notification.Observer;
import com.alizarion.reference.social.entities.notification.Subject;
import com.alizarion.reference.staticparams.StaticParam;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table( name = "like")
public class Like extends Appreciation implements Serializable {


    public Like() {
    }

    public Like(Observer observer) {
        super(observer);
    }



}
