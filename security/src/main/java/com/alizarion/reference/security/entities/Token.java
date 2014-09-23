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
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "type")
public abstract class Token implements Serializable {

    private static final long serialVersionUID = -3037923959251158170L;

    @Id
    @TableGenerator(name = "security_credential_SEQ",
            pkColumnName = "SEQ_NAME",
            pkColumnValue = "SEQ_VALUE",
            table = "sequence")
    @GeneratedValue(generator = "security_credential_SEQ",
            strategy = GenerationType.TABLE)
    @Column(unique = true)
    private Long id;

    @Column(name = "creation_date",nullable = false)
    private Date creationDate;

    @Column(name = "token",unique = true)
    private String token;

    @Column(name = "expire_date",nullable = false)
    private Date expireDate;

    public Token() {
        this.creationDate =  new Date();
        this.expireDate = new Date(creationDate.getTime() + getValid());

    }

    public Token(
            final String generatedToken) {
        this.token = generatedToken;

    }

    public Long getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getToken(){
        return this.token;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    protected void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    /**
     * Method to get token validity time.
     * @return period in milliseconds.
     */
    public abstract long getValid();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Token)) return false;

        Token that = (Token) o;

        return !(creationDate != null ?
                !creationDate.equals(that.creationDate) :
                that.creationDate != null) &&
                !(expireDate != null ? !expireDate.equals(that.expireDate) :
                        that.expireDate != null) &&
                !(token != null ? !token.equals(that.token) :
                        that.token != null);

    }

    @Override
    public int hashCode() {
        int result = creationDate != null ?
                creationDate.hashCode() : 0;
        result = 31 * result + (token != null ?
                token.hashCode() : 0);
        result = 31 * result + (expireDate != null ?
                expireDate.hashCode() : 0);
        return result;
    }

    @PrePersist
    public void prePersist(){
        if (this.token == null){
            this.token = UUID.randomUUID().toString();
        }
    }
}
