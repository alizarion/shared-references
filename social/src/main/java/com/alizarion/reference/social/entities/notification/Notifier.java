package com.alizarion.reference.social.entities.notification;

/**
 * Interface used as abstract link between notification and the
 * entity that have produce the notification, obsever, systemInformation or others
 * @author  selim@openlinux.fr on 29/07/14.
 */
public abstract class Notifier {

    public abstract Long getId();

    public abstract String getQualifier();
}
