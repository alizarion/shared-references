package com.alizarion.reference.social.entities.comment;

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
@DiscriminatorValue(value = SignaledCommentNotification.TYPE)
public class SignaledCommentNotification extends Notification{

    public final static String TYPE = "comment-signaled";


    private static final long serialVersionUID = -2124571666348432445L;

    /**
     * who will be notified, can be Observer
     * child or other specific class,
     * for example system broadcast message
     * with abstract class Broadcaster...
     */
    @OneToOne(targetEntity = Observer.class)
    @JoinColumn(name = "notifier_id")
    private Notifier notifier;

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public Notifier getNotifier() {
        return this.notifier;
    }


    protected SignaledCommentNotification() {

    }


    public SignaledCommentNotification(
            final Subject subject,
            final Observer observer,
            final Notifier notifier) {
        super(subject, observer);
        this.notifier =  notifier;
    }

    /**
     * Method to get same notification for different observers,
     * to loop on observers that followed the same subject.
     * @param observer new notified observer.
     * @return new notification instance.
     */
    @Override
    public Notification getInstance(final Observer observer) {
        return new SignaledCommentNotification(getSubject(),observer,notifier);
    }
}
