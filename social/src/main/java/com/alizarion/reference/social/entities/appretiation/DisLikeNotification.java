package com.alizarion.reference.social.entities.appretiation;

import com.alizarion.reference.social.entities.notification.Notification;
import com.alizarion.reference.social.entities.notification.Observer;
import com.alizarion.reference.social.entities.notification.Subject;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(DisLike.TYPE)
public class DisLikeNotification extends Notification<Observer> {

    public final static String TYPE =  "NOTIFICATION_DISLIKE";

    private static final long serialVersionUID = 3962952568182256944L;


    protected DisLikeNotification() {

    }

    public DisLikeNotification(Subject subject, Observer observer, Observer notifier) {

        super(subject, observer,notifier);

    }

   @Override
    public String getType() {
        return TYPE;
    }

    /**
     * Method to get same notification for different observers,
     * to loop on observers that followed the same subject.
     * @param observer new notified observer.
     * @return new notification instance.
     */
    @Override
    public Notification getInstance(final Observer observer) {
        return new DisLikeNotification(getSubject(),observer,(Observer) getNotifier());
    }
}
