package com.alizarion.reference.social.entities.notification;



import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract representation of any subject can be observable.
 * for more details of use see use case in Comment class.
 * @see com.alizarion.reference.social.entities.comment.Comment
 * @author selim@openlinux.fr.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Subject<T extends Observer> implements Serializable {

    private static final long serialVersionUID = 3572723366673303254L;


    @Id
    @TableGenerator(name="social_subject_SEQ", table="sequence",
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="social_subject_SEQ")
    @Column
    private Long id;


    @ManyToMany(targetEntity = Observer.class, fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
            mappedBy = "subjects")
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<T> observers = new HashSet<>();



    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;


    @Transient
    private transient Integer observersCount;

    public Subject() {

        this.creationDate = new Date();
    }

    public void removeAllObservers(){
        this.observers.clear();
    }

    public void  addObserver(T observer){
        this.observers.add(observer);

    }

    public Integer getObserverCount(){
        return this.observersCount;
    }

    public void removeObserver(Observer observer){
        this.observers.remove(observer);
    }

    /**
     * Method to notify many observers.
     * @param notification to use for each of them
     */
    public abstract  void notifyObservers(Notification notification);

    /**
     * Method to notify subject main observer <p>the owner</p>
     * @param notification to use.
     */
    public abstract  void notifyOwner(Notification notification);

    public Set<T> getObservers() {
        return observers;
    }


    public abstract String getSubjectType();


    public void setObservers(Set<T> observers) {
        this.observers = observers;
    }

    public Long getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    @PostLoad
    public void onLoad(){
        this.observersCount = this.observers.size();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Subject)) return false;

        Subject subject = (Subject) o;

        return !(creationDate != null ?
                !creationDate.equals(subject.creationDate) :
                subject.creationDate != null);

    }

    @Override
    public int hashCode() {
        return creationDate != null ? creationDate.hashCode() : 0;
    }
}
