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
public class CommentNotification extends Notification {

    public final static String TYPE= "comment";

    public CommentNotification() {
        super();
    }

    public CommentNotification(Subject subject, Observer observer) {
        super(subject, observer);
    }

    @Override
    public String getTypeKey() {
        return this.TYPE+"-notif-key";
    }
}
