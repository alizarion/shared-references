package com.alizarion.reference.social.entities.comment;

import com.alizarion.reference.social.entities.notification.Notification;
import com.alizarion.reference.social.entities.notification.Observer;
import com.alizarion.reference.social.entities.notification.Subject;
import com.alizarion.reference.staticparams.StaticParam;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
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

    @Column(name = "creation_date")
    @OrderColumn
    private Date creationDate;

    @Column
    private Boolean signaled;

    @ManyToOne
    @JoinColumn(name="answer_to_id")
    private Comment answerToComment;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "answerToComment",
            cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<Comment> answers = new HashSet<Comment>();



    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getSignaled() {
        return signaled;
    }

    public void setSignaled(Boolean signaled) {
        if (!this.signaled && signaled) {
            notifyOwner(new SignaledCommentNotification());
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
    public void notifyObservers(Notification notification) {
        for (Observer ob : getObservers()) {
            ob.notify(notification.getInstance(this,ob));
        }
    }

    @Override
    public void notifyOwner(Notification notification) {
        Observer owner = getSubjectOwner();
        owner.notify(notification.getInstance(this,owner));
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        if (!(o instanceof Comment)) return false;
        if (!super.equals(o)) return false;

        Comment comment = (Comment) o;

        if (creationDate != null ? !creationDate.equals(comment.creationDate)
                : comment.creationDate != null)
            return false;
        if (signaled != null ? !signaled.equals(comment.signaled)
                : comment.signaled != null) return false;
        if (value != null ? !value.equals(comment.value)
                : comment.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        return result;
    }
}

