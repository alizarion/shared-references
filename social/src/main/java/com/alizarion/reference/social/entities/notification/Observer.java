package com.alizarion.reference.social.entities.notification;


import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract representation of any subject observer, must be extended
 * by any notifiable entity.
 * @author selim@openlinux.fr.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Observer implements Notifier, Serializable {

    private static final long serialVersionUID = -7335375139085431884L;

    @Id
    @TableGenerator(name="social_observer_SEQ", table="sequence",
            pkColumnName="SEQ_NAME",
            valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="social_observer_SEQ")
    @Column
    private Long id;

    @ManyToOne
    private Subject subject;

    @OneToMany(mappedBy = "observer",
            cascade = CascadeType.ALL)
    private Set<Notification> notifications= new HashSet<>();

    protected Observer() {
    }

    /**
     * Method to add some new notifications to this Observer.
     * @param notification to add.
     */
    public void notify(Notification notification){
        this.notifications.add(notification);
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Observer)) return false;

        Observer observer = (Observer) o;

        return !(id != null ? !id.equals(observer.id) :
                observer.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Observer{" +
                "id=" + id +
                ", subject=" + subject.getClass() +
                ", notifications=" + notifications.size() +
                '}';
    }
}
