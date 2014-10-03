package com.alizarion.reference.security.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Tokens are used to validate editing requested on credentials.
 * @author selim@openlinux.fr.
 * @see Credential
 */
@Embeddable
public  class Token implements Serializable {

    private static final long serialVersionUID = -3037923959251158170L;

    @Column(name = "token_creation_date",
            nullable = false)
    private Date creationDate;

    @Column(name = "token_value",
            unique = true)
    private String value;

    @Column(name = "token_expire_date",
            nullable = false)
    private Date expireDate;


    public Token() {
    }

    /**
     * default constructor of all tokens
     * @param duration Token duration in second.
     * @param generatedToken Token value.
     */
    public Token(final long duration,
                 final String generatedToken) {
        this.creationDate =  new Date();
        this.expireDate = new Date(creationDate.getTime() +
                (duration * 1000));
        this.value = generatedToken;

    }

    public Token(final String generatedToken) {
           this.creationDate =  new Date();
           this.value = generatedToken;

       }

    public void addTime(final long duration){
        this.expireDate.setTime(this.expireDate.
                getTime()+(duration *1000));
    }

    public boolean isValidToken(){
        return (new Date()).after(this.expireDate);
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getValue(){
        return this.value;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    protected void setExpireDate(final Date expireDate) {
        this.expireDate = expireDate;
    }

    public void revoke(){
        this.expireDate = new Date();
    }

    public long expireIn(){
        long creation =  this.creationDate.getTime();
        long expire =  this.expireDate.getTime();
        long diff =  expire - creation;
        return diff/1000;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Token)) return false;

        Token that = (Token) o;

        return !(creationDate != null ?
                !creationDate.equals(that.creationDate) :
                that.creationDate != null) &&
                !(expireDate != null ? !expireDate.equals(that.expireDate) :
                        that.expireDate != null) &&
                !(value != null ? !value.equals(that.value) :
                        that.value != null);

    }

    @Override
    public int hashCode() {
        int result = creationDate != null ?
                creationDate.hashCode() : 0;
        result = 31 * result + (value != null ?
                value.hashCode() : 0);
        result = 31 * result + (expireDate != null ?
                expireDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Token{" +
                "creationDate=" + creationDate +
                ", value='" + value + '\'' +
                ", expireDate=" + expireDate +
                '}';
    }

    @PrePersist
    public void prePersist(){
        if (this.value == null){
            this.value = UUID.randomUUID().toString();
        }
    }
}
