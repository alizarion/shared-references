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
@DiscriminatorValue(value = CommentNotification.TYPE)
public class CommentNotification extends Notification<Observer> {

    public final static String TYPE= "COMMENT_NOTIFICATION";

    private static final long serialVersionUID = 3032740949090986489L;

    protected CommentNotification() {
    }

    public CommentNotification(final Subject subject,
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
        return new CommentNotification(getSubject(),observer, (Observer)getNotifier());
    }
}
