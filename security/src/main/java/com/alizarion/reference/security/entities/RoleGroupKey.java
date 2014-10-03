package com.alizarion.reference.security.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

/**
 * @author selim@openlinux.fr.
 */
@Embeddable
public class RoleGroupKey  implements Serializable{


    private static final long serialVersionUID = -8386781628720988820L;
    @Column(name = "group_id",
            nullable = false)
    private String group;


    @Column(name = "description",
            nullable = false)
    private String description;

    @Column(name = "creation_date",
            nullable = false)
    private Date creationDate;

    public RoleGroupKey() {

      }

    public RoleGroupKey(final String group,
                        final String description) {
        this.group = group;
        this.description = description;
        this.creationDate = new Date();
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RoleGroupKey)) return false;

        RoleGroupKey that = (RoleGroupKey) o;

        return !(creationDate != null ?
                !creationDate.equals(that.creationDate) :
                that.creationDate != null) &&
                !(description != null ?
                        !description.equals(that.description) :
                        that.description != null) &&
                !(group != null ? !group.equals(that.group) :
                        that.group != null);

    }

    @Override
    public int hashCode() {
        int result = group != null ? group.hashCode() : 0;
        result = 31 * result + (description != null ?
                description.hashCode() : 0);
        result = 31 * result + (creationDate != null ?
                creationDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RoleGroupKey{" +
                "group='" + group + '\'' +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
