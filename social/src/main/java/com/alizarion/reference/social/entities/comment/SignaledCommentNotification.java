package com.alizarion.reference.social.entities.comment;

import com.alizarion.reference.social.entities.notification.Notification;
import com.alizarion.reference.social.entities.notification.Observer;
import com.alizarion.reference.social.entities.notification.Subject;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(value = SignaledCommentNotification.TYPE)
public class SignaledCommentNotification extends Notification<Observer>{

    public final static String TYPE = "NOTIFICATION_SIGNALED_COMMENT";

    private static final long serialVersionUID = -2124571666348432445L;

    @Override
    public String getType() {
        return TYPE;
    }

    protected SignaledCommentNotification() {

    }

    public SignaledCommentNotification(
            final Subject subject,
            final Observer observer,
            final Observer notifier) {
        super(subject, observer,notifier);
    }

    /**
     * Method to get same notification for different observers,
     * to loop on observers that followed the same subject.
     * @param observer new notified observer.
     * @return new notification instance.
     */
    @Override
    public Notification getInstance(final Observer observer) {
        return new SignaledCommentNotification(getSubject(),observer,(Observer)getNotifier());
    }
}
