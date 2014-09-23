package com.alizarion.reference.security.entities;

import com.alizarion.reference.security.tools.SecurityHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This class contain credentials information for user login.
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_credential")
public class Credential implements Serializable {

    private static final long serialVersionUID = 8712203414431845759L;

    @Id
    @TableGenerator(name="security_credential_SEQ", table="sequence",
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy= GenerationType.TABLE, generator="security_credential_SEQ")
    @Column
    private Long id;

    @Column(name = "user_name",unique = true,nullable = false)
    private String userName;

    /**
     * SHA1 user password
     */
    @Column(name = "password",nullable = false)
    private String password;

    /**
     * Credential state, to now if the credentials has been
     * activated A / reset P / disabled D
     */
    @Enumerated(EnumType.STRING)
    @Column(name="state", nullable = false,length = 11)
    private CredentialState state;

    @Column(name = "credential_creation_date" ,nullable = false)
    private Date creationDate;

    @ManyToMany
    @JoinTable(name = "credential_role",
            joinColumns=@JoinColumn(name = "credential_id"),
            inverseJoinColumns=@JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public Long getId() {
        return id;
    }


    public Credential() {
        this.creationDate = new Date();
    }

    public String getUserName() {
        return userName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(final String password){
        this.password =
                SecurityHelper.getSHA1Value(password);
    }

    public Boolean isCorrectPassword(String test){
        return this.password.equals(SecurityHelper.
                getSHA1Value(test));
    }

    public CredentialState getState() {
        return state;
    }

    public void setState(CredentialState state) {
        this.state = state;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Credential)) return false;

        Credential that = (Credential) o;

        return !(creationDate != null ?
                !creationDate.equals(that.creationDate) :
                that.creationDate != null) &&
                !(id != null ? !id.equals(that.id) :
                        that.id != null) && !(password != null ?
                !password.equals(that.password) :
                that.password != null) &&
                state == that.state &&
                !(userName != null ?
                        !userName.equals(that.userName) :
                        that.userName != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
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
