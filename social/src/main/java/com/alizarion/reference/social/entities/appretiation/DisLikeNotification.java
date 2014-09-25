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
@DiscriminatorValue(DisLike.TYPE)
public class DisLikeNotification extends Notification {

    public final static String TYPE =  "dislike";

    private static final long serialVersionUID = 3962952568182256944L;

    /**
     * who will be notified, can be Observer
     * child or other specific class,
     * for example system broadcast message
     * with abstract class Broadcaster...
     */
    @OneToOne(targetEntity = Observer.class)
    @JoinColumn(name = "notifier_id")
    private Notifier notifier;

    protected DisLikeNotification() {

    }

    public DisLikeNotification(Subject subject, Observer observer, Notifier notifier) {

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
        return new DisLikeNotification(getSubject(),observer,notifier);
    }
}
