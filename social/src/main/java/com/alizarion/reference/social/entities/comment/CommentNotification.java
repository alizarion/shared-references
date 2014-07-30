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
@DiscriminatorValue(value = CommentNotification.TYPE)
public class CommentNotification extends Notification {

    public final static String TYPE= "comment";

    @OneToOne(targetEntity = Observer.class)
    @JoinColumn(name = "notifier_id")
    private Notifier notifier;


    public CommentNotification() {
        super();
    }

    public CommentNotification(Subject subject, Observer observer,Notifier notifier) {
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
        return this.TYPE;
    }

    @Override
    public Notification getInstance(Subject subject, Observer observer, Notifier notifier) {
        return new CommentNotification(subject,observer, notifier);
    }
}
