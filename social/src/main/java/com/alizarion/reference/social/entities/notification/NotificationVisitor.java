package com.alizarion.reference.social.entities.notification;

import com.alizarion.reference.exception.ApplicationException;

/**
 * @author selim@openlinux.fr.
 */
public interface NotificationVisitor  {

    public void visit(Notification notification) throws ApplicationException;
}
