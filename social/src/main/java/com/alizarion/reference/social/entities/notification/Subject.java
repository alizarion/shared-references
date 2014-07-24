package com.alizarion.reference.social.entities.notification;

import com.alizarion.reference.social.entities.notification.Observer;
import com.alizarion.reference.staticparams.StaticParam;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract representation of any subject can be observable.
 * @author selim@openlinux.fr.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Subject implements Serializable {

    @Id
    @TableGenerator(name="Subject_SEQ", table="sequence",catalog = StaticParam.CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="Subject_SEQ")
    @Column
    private Long id;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "subject",
            cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<Observer> observers = new HashSet<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "subject_owner")
    private Observer subjectOwner;


    public Subject() {

    }

    public Subject(Observer observer) {
        this.subjectOwner = observer;
    }

    public void  addObserver(Observer observer){
        this.observers.add(observer);
    }

    public void removeObserver(Observer observer){
        this.observers.remove(observer);
    }

    public abstract  void notifyObservers();

    public Set<Observer> getObservers() {
        return observers;
    }

    public void setObservers(Set<Observer> observers) {
        this.observers = observers;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Subject)) return false;

        Subject subject = (Subject) o;

        if (id != null ? !id.equals(subject.id) :
                subject.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
