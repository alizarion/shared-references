package com.alizarion.reference.security;

import com.alizarion.reference.staticparams.StaticParam;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This entity contain credentials information for user login.
 * @author selim@openlinux.fr.
 */
@Entity
@Table(catalog = StaticParam.CATALOG,name = "credential")
public class Credential implements Serializable {

    @Id
    @TableGenerator(name="Credential_SEQ", table="sequence",catalog = StaticParam.CATALOG,
            pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy= GenerationType.TABLE, generator="Credential_SEQ")
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
    @JoinTable(catalog = StaticParam.CATALOG, name = "credential_role",
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

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password)
    {
        String result = password;
        if(password != null) {
            MessageDigest md = null; //or "MD5"
            try {
                md = MessageDigest.getInstance("SHA-1");
                md.update(password.getBytes());
                BigInteger hash = new BigInteger(1, md.digest());
                result = hash.toString(16);
                while(result.length() < 32) {
                    result = "0" + result;
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        this.password =  result;

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

        if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (state != that.state) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;

        return true;
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
