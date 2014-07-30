package com.alizarion.reference.social.entities.comment;

import com.alizarion.reference.social.entities.appretiation.DisLike;
import com.alizarion.reference.social.entities.appretiation.Like;
import com.alizarion.reference.social.entities.appretiation.LikeNotification;
import com.alizarion.reference.social.entities.notification.Notification;
import com.alizarion.reference.social.entities.notification.Notifier;
import com.alizarion.reference.social.entities.notification.Observer;
import com.alizarion.reference.social.entities.notification.Subject;
import com.alizarion.reference.staticparams.StaticParam;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple comment object class
 * @author selim@openlinux.fr.
 */
@Entity
@Table(catalog = StaticParam.CATALOG, name = "comment")
public class Comment extends Subject implements Serializable{



    @Column(name = "value")
    private String value;

    @Column
    private Boolean signaled;

    @ManyToOne
    @JoinColumn(name="answer_to_id")
    private Comment answerToComment;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "answerToComment",
            cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<Comment> answers = new HashSet<Comment>();


    @OneToMany(fetch = FetchType.LAZY,
            orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<Like> likes= new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY,
            orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<DisLike> disLikes = new HashSet<>();


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }



    public Boolean getSignaled() {
        return signaled;
    }

    public void setSignaled(Boolean signaled, Notifier notifier) {
        if (!this.signaled && signaled) {
            notifyOwner(new SignaledCommentNotification(),notifier);
        }
        this.signaled = signaled;
    }

    public Comment getAnswerToComment() {
        return answerToComment;
    }

    public void setAnswerToComment(Comment answerToComment) {
        this.answerToComment = answerToComment;
    }

    public Set<Comment> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Comment> answers) {
        this.answers = answers;
    }

    @Override
    public void notifyObservers(Notification notification,Notifier  notifier) {
        for (Observer ob : getObservers()) {
            ob.notify(notification.getInstance(this,ob,(notifier != null ? notifier : null)));
        }
    }

    @Override
    public void notifyOwner(Notification notification, Notifier notifier) {
        Observer owner = getSubjectOwner();
        owner.notify(notification.getInstance(this,owner,notifier));
    }

    public void addLikeAppreciation(Notifier notifier){
        Like like = new Like((Observer)notifier);
        this.likes.add(like);
        notifyOwner(new LikeNotification(),notifier);
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        if (!(o instanceof Comment)) return false;
        if (!super.equals(o)) return false;

        Comment comment = (Comment) o;

        if (value != null ? !value.equals(comment.value)
                : comment.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}

