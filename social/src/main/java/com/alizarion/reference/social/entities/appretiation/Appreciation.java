package com.alizarion.reference.social.entities.appretiation;

import com.alizarion.reference.social.entities.notification.Observer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "social_appreciation")
@DiscriminatorColumn(name = "type")
public abstract class Appreciation  implements Serializable{


    private static final long serialVersionUID = -3463066032135328655L;

    @Id
    @TableGenerator(
            name = "social_appreciation_SEQ",
            table = "sequence",
            pkColumnName = "SEQ_NAME",
            valueColumnName= "SEQ_COUNT")
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "social_appreciation_SEQ")
    private Long id;

    /**
     * who appreciate
     */
    @ManyToOne
    private Observer owner;

    @Column(name = "creation_date",nullable = false)
    private Date creationDate;

    protected Appreciation() {
        this.creationDate = new Date();
    }

    public Appreciation(Observer owner) {
        this.creationDate = new Date();
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

        return !(creationDate != null ?
                !creationDate.equals(that.creationDate) :
                that.creationDate != null) &&
                !(id != null ? !id.equals(that.id) :
                        that.id != null) &&
                !(owner != null ?
                        !owner.equals(that.owner) :
                        that.owner != null);

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
