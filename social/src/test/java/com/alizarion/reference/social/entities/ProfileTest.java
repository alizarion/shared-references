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
public class ProfileTest extends Subject {


    private static final long serialVersionUID = -2711747755867980845L;

    public final static String SUBJECT_TYPE  = "profile";

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Appreciation> appreciations= new HashSet<>();

    protected ProfileTest(){
        super();
    }

    public ProfileTest(UserTest userTest) {
        super(userTest);
    }

    @Override
    public void notifyObservers(Notification notification) {
        throw new NotImplementedException();
    }

    @Override
    public void notifyOwner(Notification notification) {
        Observer owner = getSubjectOwner();
        owner.notify(notification);
    }

    @Override
    public String getSubjectType() {
        return SUBJECT_TYPE;
    }

    public void addLikeAppreciation(Notifier notifier){
        Like like = new Like((Observer)notifier);
        this.appreciations.add(like);
        notifyOwner(new LikeNotification(this,getSubjectOwner(),notifier));
    }


    public void addDisLikeAppreciation(Notifier notifier){
        DisLike disLike = new DisLike((Observer)notifier);
        this.appreciations.add(disLike);
        notifyOwner(new DisLikeNotification(this,getSubjectOwner(),notifier));
    }

    @Override
    public String toString() {
        return "ProfileTest{" +
                "appreciations=" + appreciations.size() +
                '}';
    }
}
