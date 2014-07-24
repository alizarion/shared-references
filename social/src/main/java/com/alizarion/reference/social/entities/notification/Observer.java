package com.alizarion.reference.social.entities.notification;

import com.alizarion.reference.staticparams.StaticParam;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract representation of any subject observer
 * @author selim@openlinux.fr.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Observer implements Serializable {

    @Id
    @TableGenerator(name="Observer_SEQ", table="sequence",catalog = StaticParam.CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="Observer_SEQ")
    @Column
    private Long id;

    @ManyToOne
    private Subject subject;

    @OneToMany(mappedBy = "observer",
            cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<Notification> notifications= new HashSet<>();

    protected Observer() {
    }

    public  void notify(Notification notification){
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Observer)) return false;

        Observer observer = (Observer) o;

        if (id != null ? !id.equals(observer.id) :
                observer.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
