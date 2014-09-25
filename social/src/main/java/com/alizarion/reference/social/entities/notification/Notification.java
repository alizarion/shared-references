package com.alizarion.reference.social.entities.notification;


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
@DiscriminatorColumn(name = "type")
public abstract class Notification implements Serializable {


    private static final long serialVersionUID = -7099966886907706267L;
    @Id
    @TableGenerator(name="social_notification_SEQ", table="sequence",
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="social_notification_SEQ")
    @Column
    private Long id;

    @OneToOne
    @JoinColumn(name="subject_id",nullable = false)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name="observer_id",nullable = false)
    private Observer observer;

    @Column(name = "creation_date",nullable = false)
    private Date creationDate;

    @Column(name = "email_notified")
    private boolean emailNotified;

    @Column(name = "in_app_nofified")
    private boolean inAppNotified;


    protected Notification() {
    }

    protected Notification(final Subject subject,
                           final Observer observer) {

        this.subject = subject;
        this.observer = observer;
        this.creationDate =  new Date();
    }

    public abstract String getType();

    public abstract Notifier getNotifier();

    public String getTypeKey(){
        return getType()+"-notif-key";
    }

    protected Notification(Subject subject) {
        this.subject = subject;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Observer getObserver() {
        return observer;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public Boolean getEmailNotified() {
        return emailNotified;
    }

    public void setEmailNotified(Boolean emailNotified) {
        this.emailNotified = emailNotified;
    }

    public Boolean getInAppNotified() {
        return inAppNotified;
    }

    public void setInAppNotified(Boolean inAppNotified) {
        this.inAppNotified = inAppNotified;
    }

    public Long getId() {
        return id;
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

        return !(creationDate != null ?
                !creationDate.equals(that.creationDate) :
                that.creationDate != null) &&
                !(id != null ? !id.equals(that.id) :
                        that.id != null) &&
                !(observer != null ? !observer.equals(that.observer) :
                        that.observer != null) &&
                !(subject != null ?
                        !subject.equals(that.subject) :
                        that.subject != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (subject != null ?
                subject.hashCode() : 0);
        result = 31 * result + (observer != null ?
                observer.hashCode() : 0);
        result = 31 * result + (creationDate != null ?
                creationDate.hashCode() : 0);
        return result;
    }
}