package com.alizarion.reference.social.entities;

import com.alizarion.reference.social.entities.appretiation.*;
import com.alizarion.reference.social.entities.notification.Notification;
import com.alizarion.reference.social.entities.notification.Notifier;
import com.alizarion.reference.social.entities.notification.Observer;
import com.alizarion.reference.social.entities.notification.Subject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "test_user_profile")
@AssociationOverrides({
        @AssociationOverride(name = "observer" ,
                joinTable = @JoinTable(name = "test_observer_profile",
                        joinColumns = @JoinColumn(name = "profile_id",nullable = false),
                        inverseJoinColumns = @JoinColumn(name = "user_id")))
})
public class ProfileTest extends Subject<Observer> {


    private static final long serialVersionUID = -2711747755867980845L;

    public final static String SUBJECT_TYPE  = "profile";

    @OneToOne(mappedBy = "userProfile")
    @JoinColumn(name = "user_id")
    private UserTest user;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Appreciation> appreciations= new HashSet<>();

    protected ProfileTest(){

    }

    public ProfileTest(UserTest user) {
        this.user = user;
    }

    @Override
    public void notifyObservers(Notification notification) {
        throw new NotImplementedException();
    }

    @Override
    public void notifyOwner(Notification notification) {
        this.user.notify(notification);
    }

    @Override
    public String getSubjectType() {
        return SUBJECT_TYPE;
    }

    public void addLikeAppreciation(Notifier notifier){
        Like like = new Like((Observer)notifier);
        this.appreciations.add(like);
        notifyOwner(new LikeNotification(this,this.user,(Observer)notifier));
    }

    public UserTest getUser() {
        return user;
    }

    public void setUser(UserTest user) {
        this.user = user;
    }

    public void addDisLikeAppreciation(Notifier notifier){
        DisLike disLike = new DisLike((Observer)notifier);
        this.appreciations.add(disLike);
        notifyOwner(new DisLikeNotification(this,this.user,(Observer)notifier));
    }

    @Override
    public String toString() {
        return "ProfileTest{" +
                "appreciations=" + appreciations.size() +
                '}';
    }
}
