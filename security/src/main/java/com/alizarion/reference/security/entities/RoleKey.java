package com.alizarion.reference.security.entities;

import javax.persistence.*;
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
    @Column(name = "role_name",
            nullable = false,
            length = 25)
    private String name;


    /**
     * role key label
     */
    @Column(name = "unique_key",
            unique = true,
            nullable = false,
            length = 25)
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


    protected RoleKey(final String key) {

    }

    public RoleKey(final String name,
                   final String key,
                   final String description) {
        this.name = name;
        this.key = key;
        this.description = description;
        this.creationDate = new Date();
    }

    /**
     * Simple method to get role key.
     * @return  role key
     */
    public String getName() {
        return name;
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
        if (!(o instanceof RoleKey)) return false;

        RoleKey roleKey = (RoleKey) o;

        return !(key != null ?
                !key.equals(roleKey.key)
                : roleKey.key != null);

    }

    @Override
    public int hashCode() {
        return key != null ?
                key.hashCode() : 0;
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
                "role='" + name + '\'' +
                ", key='" + key + '\'' +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
