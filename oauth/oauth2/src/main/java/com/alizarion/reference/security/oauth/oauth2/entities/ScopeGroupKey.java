package com.alizarion.reference.security.oauth.oauth2.entities;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * @author selim@openlinux.fr.
 */
public class ScopeGroupKey implements Serializable {


    private static final long serialVersionUID = 7350535630928306607L;
    @Column(name = "group_id",
               nullable = false,
               unique = true)
       private String group;


       @Column(name = "description",
               nullable = false)
       private String description;

       @Column(name = "creation_date",
               nullable = false)
       private Date creationDate;

       public ScopeGroupKey() {

       }

       public ScopeGroupKey(final String group,
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
           if (!(o instanceof ScopeGroupKey)) return false;

           ScopeGroupKey that = (ScopeGroupKey) o;

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
