package com.alizarion.reference.social.entities.notification;


import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract representation of any subject observer, must be extended
 * by any notifiable entity.
 * @author selim@openlinux.fr.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Observer extends Notifier implements Serializable {

    private static final long serialVersionUID = -7335375139085431884L;


    @Id
    @TableGenerator(name="social_observer_SEQ", table="sequence",
            pkColumnName="SEQ_NAME",
            valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="social_observer_SEQ")
    @Column
    private Long id;


    //TODO passer en lazy en conservant le chargement des entité polymorphe
   /* @ManyToAny(
        metaColumn = @Column( name = "subject_type" )
    )   */
    @ManyToMany(fetch = FetchType.LAZY,
            targetEntity = Subject.class,
            cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.DETACH})
    @JoinTable(name = "social_observer_subject",
            joinColumns = @JoinColumn(name = "observer_id",
                    nullable = false),
            inverseJoinColumns = @JoinColumn(name = "subject_id",nullable = false))
    @LazyCollection(LazyCollectionOption.EXTRA)
    //@Fetch(FetchMode.SELECT)
    private Set<Subject> subjects = new HashSet<>();

    @OneToMany(mappedBy = "observer",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    //@Fetch(FetchMode.SELECT)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<Notification> notifications= new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "social_notification_inapp",
            joinColumns = @JoinColumn(name = "observer_id",nullable = false),
            inverseJoinColumns = @JoinColumn(name = "notification_type",nullable = false))
    private Set<NotificationType> inAppAutorizedNotifications = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "social_notification_device",
            joinColumns = @JoinColumn(name = "observer_id",nullable = false),
            inverseJoinColumns = @JoinColumn(name = "notification_type",nullable = false))
    private Set<NotificationType> deviceAutorizedNotifications = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "social_notification_emails",
            joinColumns = @JoinColumn(name = "observer_id",nullable = false),
            inverseJoinColumns = @JoinColumn(name = "notification_type",nullable = false))
    private Set<NotificationType> emailAutorizedNotifications = new HashSet<>();


    @Transient
    private transient Integer subjectCount;

    protected Observer() {
    }

    /**
     * Method to add some new notifications to this Observer.
     * @param notification to add.
     */
    public void notify(Notification notification){
        this.notifications.add(notification.getInstance(this));
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public List<Subject> getSubjectAsList(){
        return new ArrayList<>(this.subjects);
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<NotificationType> getInAppAutorizedNotifications() {
        return inAppAutorizedNotifications;
    }

    public void setInAppAutorizedNotifications(Set<NotificationType> inAppAutorizedNotifications) {
        this.inAppAutorizedNotifications = inAppAutorizedNotifications;
    }

    public Set<NotificationType> getDeviceAutorizedNotifications() {
        return deviceAutorizedNotifications;
    }

    public void setDeviceAutorizedNotifications(Set<NotificationType> deviceAutorizedNotifications) {
        this.deviceAutorizedNotifications = deviceAutorizedNotifications;
    }

    public Set<NotificationType> getEmailAutorizedNotifications() {
        return emailAutorizedNotifications;
    }

    public void setEmailAutorizedNotifications(Set<NotificationType> emailAutorizedNotifications) {
        this.emailAutorizedNotifications = emailAutorizedNotifications;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Observer)) return false;

        Observer observer = (Observer) o;

        return !(id != null ? !id.equals(observer.id) :
                observer.id != null);

    }

    @SuppressWarnings("unchecked")
    public void addSubject(Subject subject){
        subject.addObserver(this);
        this.subjects.add(subject);

    }

    public Integer getSubjectCount() {
        return subjectCount;
    }

    @PostLoad
    public void postLoad(){
        this.subjectCount = this.subjects.size();
    }

    public void removeSubject(Subject subject){
        subject.removeObserver(this);
        this.subjects.remove(subject);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return getQualifier();
    }
}
