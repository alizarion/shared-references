package com.alizarion.reference.social.entities;

import com.alizarion.reference.social.entities.comment.Comment;
import com.alizarion.reference.social.entities.notification.Notification;
import com.alizarion.reference.social.entities.notification.Observer;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */

@Entity
@Table(name = "test_user")
public class UserTest extends Observer  {

    private static final long serialVersionUID = 3828785258188798326L;

    @OneToOne(cascade = CascadeType.ALL)
    private ProfileTest userProfile;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Comment<UserTest>> comments  = new HashSet<>();

    private UserTest() {
    }



    public UserTest(
            String firstName,
            String lastName) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.userProfile = new ProfileTest(this);
        //this.setSubject(userProfile);

    }

    public void addNewComment(String content){
        this.comments.add(new Comment(this,content));
    }

    public Set<Comment<UserTest>> getComments() {
        return comments;
    }

    public void setComments(Set<Comment<UserTest>> comments) {
        this.comments = comments;
    }

    public UserTest(ProfileTest userProfile) {
        this.userProfile = userProfile;
    }

    public ProfileTest getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(ProfileTest userProfile) {
        this.userProfile = userProfile;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void printNotifications(){
        for (Notification notification :  getNotifications()){
            System.out.println(this.firstName +" => "+
                    ((UserTest) notification.getNotifier()).
                            getFirstName() +" has "+
                    notification.getType() + " your " +
                    notification.getSubject().getSubjectType()+
                    ":"+ notification.getSubject().getId());
        }

    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserTest)) return false;
        if (!super.equals(o)) return false;

        UserTest userTest = (UserTest) o;

        if (firstName != null ? !firstName.equals(userTest.firstName) : userTest.firstName != null) return false;
        if (lastName != null ? !lastName.equals(userTest.lastName) : userTest.lastName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String commentss = " ";
        for(Comment comment : this.getComments()){
            commentss = commentss + ", " +comment.toString();
        }
        return super.toString() + " UserTest{" +
                "userProfile=" + userProfile +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", comments=" + comments.size() + " { " +  commentss + " } "+
                '}';
    }

    @Override
    public String getQualifier() {
        return this.firstName + ", " + this.lastName;
    }
}
