package com.alizarion.reference.social.entities.notification;

import com.alizarion.reference.staticparams.StaticParam;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Notification base class, must be extend by all notifications type.
 * @author selim@openlinux.fr.
 */
@Entity
@Inheritance
@Table(catalog = StaticParam.CATALOG,name = "notification")
@DiscriminatorColumn(name = "type")
public abstract class Notification implements Serializable{


    @Id
    @TableGenerator(name="Notification_SEQ", table="sequence",catalog = StaticParam.CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="Notification_SEQ")
    @Column
    private Long id;

    @OneToOne
    @JoinColumn(name="subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name="observer_id")
    private Observer observer;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "email_notified")
    private Boolean emailNotified;

    @Column(name = "in_app_nofified")
    private Boolean inAppNotified;



    protected Notification() {
        this.creationDate = new Date();
    }

    protected Notification(Subject subject, Observer observer) {
        this.subject = subject;
        this.observer = observer;
    }

    public abstract String getType();



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

    public abstract Notification getInstance(Subject subject,Observer observer,Notifier notifier);

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Notification)) return false;

        Notification that = (Notification) o;

        if (creationDate != null ? !creationDate.equals(that.creationDate)
                : that.creationDate != null) return false;
        if (id != null ? !id.equals(that.id) :
                that.id != null) return false;
        if (observer != null ? !observer.equals(that.observer)
                : that.observer != null) return false;
        if (subject != null ? !subject.equals(that.subject) :
                that.subject != null) return false;

        return true;
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