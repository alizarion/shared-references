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
public class SignaledCommentNotification extends Notification{

    public final static String TYPE = "comment-signaled";

    @Override
    public String getTypeKey() {
        return TYPE+"-notif-key";
    }

    public SignaledCommentNotification() {
    }

    public SignaledCommentNotification(Subject subject, Observer observer) {
        super(subject, observer);
    }

    @Override
    public Notification getInstance(Subject subject, Observer observer) {
        return new SignaledCommentNotification(subject,observer);
    }
}
