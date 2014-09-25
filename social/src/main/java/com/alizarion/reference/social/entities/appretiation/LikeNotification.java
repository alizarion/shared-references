package com.alizarion.reference.social.entities.appretiation;

import com.alizarion.reference.social.entities.notification.Notification;
import com.alizarion.reference.social.entities.notification.Notifier;
import com.alizarion.reference.social.entities.notification.Observer;
import com.alizarion.reference.social.entities.notification.Subject;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(LikeNotification.TYPE)
public class LikeNotification extends Notification {


    public final static String TYPE =  "like";
    private static final long serialVersionUID = -2454973965126700138L;

    /**
     * who will be notified, can be Observer
     * child or other specific class,
     * for example system broadcast message
     * with abstract class Broadcaster...
     */
    @OneToOne(targetEntity = Observer.class)
    @JoinColumn(name = "notifier_id")
    private Notifier notifier;

    protected LikeNotification() {

      }

    public LikeNotification(final Subject subject,
                            final Observer observer,
                            final Notifier notifier) {
        super(subject, observer);
        this.notifier = notifier;
    }

    public Notifier getNotifier() {
        return notifier;
    }

    public void setNotifier(Notifier notifier) {
        this.notifier = notifier;
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
        return new LikeNotification(getSubject(),observer,notifier) ;
    }


}
