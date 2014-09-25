package com.alizarion.reference.social.entities.notification;



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
public abstract class Subject implements Serializable {

    private static final long serialVersionUID = 3572723366673303254L;
    @Id
    @TableGenerator(name="social_subject_SEQ", table="sequence",
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy=GenerationType.TABLE, generator="social_subject_SEQ")
    @Column
    private Long id;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "subject",
            cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<Observer> observers = new HashSet<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "subject_owner")
    private Observer subjectOwner;

    @Column(name = "creation_date")
    private Date creationDate;


    protected Subject() {

    }

    public Subject(Observer observer) {

        this.creationDate = new Date();
        this.subjectOwner = observer;
    }

    public void  addObserver(Observer observer){
        this.observers.add(observer);
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

    public Set<Observer> getObservers() {
        return observers;
    }

    public Observer getSubjectOwner() {
        return subjectOwner;
    }

    public abstract String getSubjectType();

    public void setSubjectOwner(Observer subjectOwner) {
        this.subjectOwner = subjectOwner;
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

        return !(creationDate != null ?
                !creationDate.equals(subject.creationDate) :
                subject.creationDate != null) &&
                !(id != null ? !id.equals(subject.id) :
                        subject.id != null) &&
                !(subjectOwner != null ?
                        !subjectOwner.equals(subject.subjectOwner) :
                        subject.subjectOwner != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (subjectOwner != null
                ? subjectOwner.hashCode() : 0);
        result = 31 * result + (creationDate != null
                ? creationDate.hashCode() : 0);
        return result;
    }
}
