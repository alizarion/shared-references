package com.alizarion.reference.security;

import com.alizarion.reference.staticparams.StaticParam;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Class to define users role
 * @author selim@openlinux.fr.
 */
@Entity
@Table(catalog = StaticParam.CATALOG,name = "role")
public class Role implements Serializable {

    /**
     * simple incremental id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * role key label
     */
    @Column(name = "role",
            unique = true,
            nullable = false,
            length = 11)
    private String role;

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

    public Role() {
        this.creationDate = new Date();
    }

    /**
     * Simple method to get role id.
     * @return role id
     */
    public Long getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Role)) return false;

        Role role1 = (Role) o;

        if (creationDate != null ? !creationDate.equals(role1.creationDate)
                : role1.creationDate != null) return false;
        if (description != null ? !description.equals(role1.description)
                : role1.description != null) return false;
        if (id != null ? !id.equals(role1.id) : role1.id != null) return false;
        if (role != null ? !role.equals(role1.role) :
                role1.role != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (role != null ? role.hashCode() : 0);
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

}
