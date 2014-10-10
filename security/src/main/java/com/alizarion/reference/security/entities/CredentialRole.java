package com.alizarion.reference.security.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_credential_roles")
public class CredentialRole implements Serializable {

    private static final long serialVersionUID = 1927873365012535601L;

    @Id
    @TableGenerator(name = "security_credential_roles_SEQ",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            table = "sequence")
    @GeneratedValue(generator = "security_credential_roles_SEQ",
            strategy = GenerationType.TABLE)
    private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "credential_id")
    private Credential credential;


    public CredentialRole(final Role role,
                          final Credential credential) {
        this.role = role;
        this.credential = credential;
    }

    public CredentialRole() {
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
