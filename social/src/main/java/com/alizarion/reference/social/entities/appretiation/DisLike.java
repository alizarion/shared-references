package com.alizarion.reference.social.entities.appretiation;

import com.alizarion.reference.social.entities.notification.Observer;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(DisLike.TYPE)
public class DisLike extends Appreciation implements Serializable {

   public final static String TYPE =  "DISLIKE";

    private static final long serialVersionUID = 7359559602461896125L;

    public DisLike() {
        super();
    }

    public DisLike(Observer owner) {
        super(owner);
    }


}
