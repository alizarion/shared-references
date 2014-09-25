package com.alizarion.reference.social.entities.appretiation;

import com.alizarion.reference.social.entities.notification.Observer;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(value = Like.TYPE)
public class Like extends Appreciation implements Serializable {

    public final static String TYPE =  "LIKE";

    private static final long serialVersionUID = -3292816253909098351L;


    protected Like() {
    }

    public Like(Observer observer) {
        super(observer);
    }



}
