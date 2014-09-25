package com.alizarion.reference.social.entities.comment;

import com.alizarion.reference.social.entities.appretiation.*;
import com.alizarion.reference.social.entities.notification.Notification;
import com.alizarion.reference.social.entities.notification.Notifier;
import com.alizarion.reference.social.entities.notification.Observer;
import com.alizarion.reference.social.entities.notification.Subject;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple comment object class
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "social_comment")
public class Comment extends Subject implements Serializable{


    private static final long serialVersionUID = -1225445014114020990L;

    public static final String SUBJECT_TYPE = "COMMENT";

    @Column(name = "comment_value")
    private String value;

    @Column(name = "reported")
    private Boolean reported;

    @ManyToOne
    @JoinColumn(name="answer_to_id")
    private Comment answerToComment;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "answerToComment",
            cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<Comment> answers = new HashSet<>();


    @OneToMany(fetch = FetchType.LAZY,cascade =
            {CascadeType.MERGE,
                    CascadeType.PERSIST},
            orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<Appreciation> appreciations= new HashSet<>();

    protected Comment() {
    }

    public Comment(Observer owner, String value) {
        super(owner);
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }



    public Boolean getSignaled() {
        return reported;
    }

    public void setSignaled(Boolean signaled, Notifier notifier) {
        if (!this.reported && signaled) {
            notifyOwner(
                    new SignaledCommentNotification(
                            this,
                            getSubjectOwner(),
                            notifier));
        }
        this.reported = signaled;
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
    public void notifyObservers(Notification notification) {
        for (Observer ob : getObservers()) {
            ob.notify(notification.getInstance(ob));
        }
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

    public Comment addAnswerToComment(Notifier notifier,String commentValue){
        Comment answer = new Comment((Observer)notifier,commentValue);
        answer.setAnswerToComment(this);
        this.answers.add(answer);
        notifyOwner(new CommentNotification(this,getSubjectOwner(),notifier));
        return answer;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        if (!(o instanceof Comment)) return false;
        if (!super.equals(o)) return false;

        Comment comment = (Comment) o;

        return !(value != null ?
                !value.equals(comment.value)
                : comment.value != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "value='" + value + '\'' +
                ", signaled=" + reported +
                ", answerToComment=" + answerToComment +
                ", answers=" + answers.size() +
                ", appreciations=" + appreciations.size() +
                '}';
    }
}

