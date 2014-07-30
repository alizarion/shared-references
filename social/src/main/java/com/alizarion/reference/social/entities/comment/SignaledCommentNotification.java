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

    @OneToOne(targetEntity = Observer.class)
    @JoinColumn(name = "notifier_id")
    private Notifier notifier;

    @Override
    public String getType() {
        return TYPE;
    }

    public SignaledCommentNotification() {
    }

    public SignaledCommentNotification(Subject subject, Observer observer, Notifier notifier) {
        super(subject, observer);
        this.notifier =  notifier;
    }

    @Override
    public Notification getInstance(Subject subject, Observer observer,Notifier notifier) {
        return new SignaledCommentNotification(subject,observer,notifier);
    }
}
