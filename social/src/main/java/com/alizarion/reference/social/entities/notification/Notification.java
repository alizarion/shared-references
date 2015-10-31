package com.alizarion.reference.social.entities.notification;


import com.alizarion.reference.exception.ApplicationException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Notification base class, must be extend by all notifications type.
 * all child must have a mapped notifier as notifier_id column and targeted by the
 * notified entity
 * @see com.alizarion.reference.social.entities.appretiation.DisLikeNotification implémentation
 * @author selim@openlinux.fr.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "social_notification")
@NamedQueries({@NamedQuery(name = Notification.FIND_INAPP_BY_OBSERVER ,
        query = "select notif from Notification notif  join fetch notif.subject " +
                "where notif.observer.id = :observerId and notif.notifyInApp = true and " +
                "notif.creationDate <= :startDate order by " +
                "notif.creationDate desc "),
        @NamedQuery(name = Notification.COUNT_UNREAD_BY_OBSERVER ,
                query = "select count(notif.id) from Notification notif inner join notif.subject " +
                        "where notif.observer.id = :observerId and notif.notifyInApp = true and notif.viewed = false ")
})
@DiscriminatorColumn(name = "type" )
@Cacheable(false)
public abstract class Notification<T extends Notifier> implements Serializable {


    private static final long serialVersionUID = -7099966886907706267L;

    public static final String FIND_INAPP_BY_OBSERVER =  "Notification.FIND_INAPP_BY_OBSERVER";
    public static final String COUNT_UNREAD_BY_OBSERVER =  "Notification.COUNT_UNREAD_BY_OBSERVER";


    @Id
    @TableGenerator(name="social_notification_SEQ", table="sequence",
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="social_notification_SEQ")
    @Column
    private Long id;

    @OneToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name="subject_id",nullable = false)
//    @Fetch(FetchMode.SELECT)
    private Subject subject;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name="observer_id",nullable = false)
  //  @Fetch(FetchMode.SELECT)
    private Observer observer;

    @ManyToOne(optional = false,
            targetEntity = NotificationType.class,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "type",
            nullable = false,
            updatable = false,
            insertable = false)
    private NotificationType notificationType;

    @Column(name = "creation_date",nullable = false)
    private Date creationDate;



    @Column(name = "viewed")
    private Boolean viewed;


    @Column(name = "managed")
    private Boolean managed;


    @Column(name = "notifyInApp")
    private Boolean notifyInApp;



    /**
     * which trigger the notification , can be Observer
     * child or other specific class,
     * for example system broadcast message
     * with abstract class Broadcaster...
     */
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "notifier_id")
    private T notifier;

    protected Notification() {
    }

    public boolean isInAppNotifiable(){
        for (NotificationType  notificationType :
                this.observer.getInAppAutorizedNotifications()){
            if (notificationType.getType().equals(getType())){
                return true;
            }
        }
        return false;
    }

    public boolean isOnDevicesNotifiable(){
        for (NotificationType  notificationType :
                this.observer.getDeviceAutorizedNotifications()){
            if (notificationType.equals(this.notificationType)){
                return true;
            }
        }
        return false;
    }


    public boolean isByEmailNotifiable(){
        for (NotificationType  notificationType :
                this.observer.getEmailAutorizedNotifications()){
            if (notificationType.equals(this.notificationType)){
                return true;
            }
        }
        return false;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public void  accept(NotificationVisitor notificationVisitor) throws ApplicationException{
        notificationVisitor.visit(this);
    }

    protected Notification(final Subject subject,
                           final Observer observer,
                           final T notifier) {

        this.notifier = notifier;
        this.subject = subject;
        this.observer = observer;
        this.viewed = false;
        this.managed = false;
        this.notifyInApp = false;
        this.creationDate =  new Date();
    }

    public abstract String getType();

    public  Notifier getNotifier(){
        return this.notifier;
    }

    public String getTypeKey(){
        return getType()+"-notif-key";
    }

    protected Notification(Subject subject) {
        this.subject = subject;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setManaged(Boolean managed) {
        this.managed = managed;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Observer getObserver() {
        return observer;
    }

    public void setNotifyInApp(Boolean notifyInApp) {
        this.notifyInApp = notifyInApp;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public Boolean getViewed() {
        return viewed;
    }

    public Boolean getManaged() {
        return managed;
    }

    public Boolean getNotifyInApp() {
        return notifyInApp;
    }

    public Long getId() {
        return id;
    }

    public Boolean isViewed() {
        return viewed;
    }

    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }

    public Date getCreationDate() {
        return creationDate;
    }



    /**
     * Method to get same notification for different observers,
     * to loop on observers that followed the same subject.
     * @param observer new notified observer.
     * @return new notification instance.
     */
    public abstract Notification getInstance(Observer observer);

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Notification)) return false;

        Notification that = (Notification) o;

        return !(notifier != null ? !notifier.equals(that.notifier)
                : that.notifier != null) &&
                !(observer != null ? !observer.equals(that.observer) :
                        that.observer != null) &&
                !(subject != null ? !subject.equals(that.subject) :
                        that.subject != null);

    }

    @Override
    public int hashCode() {
        int result = subject != null ? subject.hashCode() : 0;
        result = 31 * result + (observer != null ? observer.hashCode() : 0);
        result = 31 * result + (notifier != null ? notifier.hashCode() : 0);
        return result;
    }
}