package com.alizarion.reference.security.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.util.Date;

/**
 * Class to define users role
 * @author selim@openlinux.fr.
 */
@Embeddable
public class RoleKey implements Serializable {

    private static final long serialVersionUID = 6917060787992589576L;

    /**
     * role key label
     */
    @Column(name = "role",
            unique = true,
            nullable = false,
            length = 11)
    private String role;


    /**
     * role key label
     */
    @Column(name = "key",
            unique = true,
            nullable = false,
            length = 11)
    private String key;

    /**
     * description field of the role
     */
    @Column(name = "description")
    private String description;

    /**
     * date of creation or last update
     */
    @Column(name = "role_creation_date",nullable = false)
    private Date creationDate;

    protected RoleKey() {

    }

    public RoleKey(final String role,
                   final String key,
                   final String description) {
        this.role = role;
        this.key = key;
        this.description = description;
        this.creationDate = new Date();
    }

    /**
     * Simple method to get role key.
     * @return  role key
     */
    public String getRole() {
        return role;
    }

    /**
     * Simple method to get the role description
     * @return  role description
     */
    public String getDescription() {
        return description;
    }

    /**
     *  Method to set the role description.
     * @param description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Method to get creation/last update date.
     * @return  creation/last update date
     */
    public Date getCreationDate() {
        return creationDate;
    }

    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleKey)) return false;

        RoleKey roleKey = (RoleKey) o;


        return !(description != null ?
                !description.equals(roleKey.description) :
                roleKey.description != null) &&
                !(key != null ? !key.equals(roleKey.key) :
                        roleKey.key != null) &&
                !(role != null ?
                        !role.equals(roleKey.role) :
                        roleKey.role != null);

    }

    @Override
    public int hashCode() {
        int result = role != null ? role.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        return result;
    }

    /**
     * Method to update creation date on persist or merge.
     */
    @PrePersist
    @PreUpdate
    protected void updateCreationDate(){
        this.creationDate = new Date();
    }

    @Override
    public String toString() {
        return "InAppRole{" +
                "role='" + role + '\'' +
                ", key='" + key + '\'' +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
