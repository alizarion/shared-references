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
@DiscriminatorValue(value = LikeNotification.TYPE)
public class LikeNotification extends Notification<Observer> {


    public final static String TYPE =  "NOTIFICATION_LIKE";
    private static final long serialVersionUID = -4633808052765176934L;


    protected LikeNotification() {

    }

    public LikeNotification(final Subject subject,
                            final Observer observer,
                            final Observer notifier) {
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
        return new LikeNotification(getSubject(),observer,(Observer)getNotifier()) ;
    }


}
