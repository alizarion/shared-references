package com.alizarion.reference.social.entities.appretiation;

import com.alizarion.reference.social.entities.notification.Notification;
import com.alizarion.reference.social.entities.notification.Observer;
import com.alizarion.reference.social.entities.notification.Subject;
import com.alizarion.reference.staticparams.StaticParam;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Appreciation  implements Serializable{


    @Id
    @TableGenerator(
            name = "Appreciation_SEQ",
            table = "sequence",
            pkColumnName = "SEQ_NAME",
            pkColumnValue = "SEQ_COUNT")
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "Appreciation_SEQ")
    @Column
    private Long id;

    /**
     * who appreciate
     */
    @ManyToOne
    private Observer owner;

    @Column(name = "creation_date")
    private Date creationDate;

    public Appreciation() {

    }

    public Appreciation(Observer owner) {
        this.owner = owner;
    }


    public Long getId() {
        return id;
    }

    public Observer getOwner() {
        return owner;
    }

    public void setOwner(Observer owner) {
        this.owner = owner;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Appreciation)) return false;

        Appreciation that = (Appreciation) o;

        if (creationDate != null ? !creationDate.equals(that.creationDate)
                : that.creationDate != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null)
            return false;
        if (owner != null ? !owner.equals(that.owner)
                : that.owner != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (owner != null
                ? owner.hashCode() : 0);
        result = 31 * result + (creationDate != null
                ? creationDate.hashCode() : 0);
        return result;
    }
}
