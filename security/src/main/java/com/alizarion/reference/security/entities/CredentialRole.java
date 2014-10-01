package com.alizarion.reference.security.entities;

import javax.persistence.*;

/**
 *
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_credential_roles")
public class CredentialRole {

    @Id
    @TableGenerator(name = "security_credential_roles_SEQ",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            table = "sequence")
    @GeneratedValue(generator = "security_credential_roles_SEQ",
            strategy = GenerationType.TABLE)
    private Long id;

    @ManyToOne
    private Role role;

    @ManyToOne
    private Credential credential;


    public CredentialRole(final Role role,
                          final Credential credential) {
        this.role = role;
        this.credential = credential;
    }

    public Long getId() {
        return id;
    }


    public Role getRole() {
        return role;
    }



    public Credential getCredential() {
        return credential;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CredentialRole)) return false;

        CredentialRole that = (CredentialRole) o;

        return !(id != null ? !id.equals(that.id) :
                that.id != null) &&
                !(role != null ? !role.equals(that.role) :
                        that.role != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CredentialRole{" +
                "id=" + id +
                ", role=" + role +
                ", credential=" + credential +
                '}';
    }
}
