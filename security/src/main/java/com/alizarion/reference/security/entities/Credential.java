package com.alizarion.reference.security.entities;

import com.alizarion.reference.security.oauth.entities.OAuthCredential;
import com.alizarion.reference.security.oauth.entities.OAuthRole;
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
@NamedQueries(@NamedQuery(name = Credential.FIND_BY_USERNAME_NAMED_QUERY,
        query = "select c from Credential c where c.userName = :username "))
public class Credential implements OAuthCredential<OAuthRole>,Serializable {

    private static final long serialVersionUID = 8712203414431845759L;


    @Id
    @TableGenerator(name="security_credential_SEQ",
            table="sequence",
            pkColumnName="SEQ_NAME",
            valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy= GenerationType.TABLE,
            generator="security_credential_SEQ")
    @Column
    private Long id;

    @Column(name = "user_name",
            unique = true)
    private String userName;

    /**
     * SHA1 user password
     */
    @Column(name = "password")
    private String password;

    /**
     * Credential state, to now if the credentials has been
     * activated A / reset P / disabled D
     */
    @Enumerated(EnumType.STRING)
    @Column(name="state",
            nullable = false,
            length = 11)
    private CredentialState state;

    /**
     * Credential state, to now if the credentials has been
     * activated A / reset P / disabled D
     */
    @Enumerated(EnumType.STRING)
    @Column(name="logon",
            nullable = false,
            length = 11)
    private LogOnType logon;

    @Column(name = "credential_creation_date",
            nullable = false)
    private Date creationDate;

    @OneToMany(mappedBy = "credential",cascade = {CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REMOVE},
            fetch = FetchType.EAGER)
    private Set<CredentialRole> credentialRoles =
            new HashSet<>();


    public Credential(final Set<Role>
                              roles) {
        for (Role  role : roles){
            this.credentialRoles.
                    add(new CredentialRole(role,this));
        }
        this.state = CredentialState.P;
        this.logon =  LogOnType.S;
        this.creationDate = new Date();
    }


    public Credential(final String userName,
                      final Set<Role>
                              roles) {
        this.userName = userName;
        this.state = CredentialState.P;
        this.logon =  LogOnType.P;
        this.creationDate = new Date();
        for (Role  role : roles){
            this.credentialRoles.
                    add(new CredentialRole(role,this));
        }
    }

    public Credential() {
    }

    public LogOnType getLogon() {
        return logon;
    }

    public void setLogon(LogOnType logon) {
        this.logon = logon;
    }

    public String getId() {
        return id.toString();
    }

    public String getStringId() {
        return id.toString();
    }

    @Override
    public OAuthCredential init(Set<String> scopes) {
        Set<Role>  roles= new HashSet<>();
        for (String scope : scopes){
            roles.add(new Role(new RoleKey(scope)));
        }
        return new Credential(roles);

    }


    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public Set<String> getAvailableScopes() {
        Set<String> scopes = new HashSet<>();
        for (Role role:getRoles()){
            scopes.add(role.getRoleKey().getKey());
        }
        return scopes;
    }

    @Override
    public boolean setScopes(Set<String> scopes) {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<OAuthRole> getOAuthRoles() {
        return (Set<OAuthRole>)(Set<?>)  getRoles();


    }

    @Override
    public boolean addScope(String scope) {
        return false;
    }


    public String getUserName() {
        return userName;
    }


    public Set<CredentialRole> getCredentialRoles() {
        return credentialRoles;
    }

    public void setCredentialRoles(final Set<CredentialRole> credentialRoles) {
        this.credentialRoles = credentialRoles;
    }

    public Set<Role> getRoles(){
        Set<Role> roles = new HashSet<>();
        for (CredentialRole  credentialRole : this.credentialRoles){
            roles.add(credentialRole.getRole());
        }
        return roles;
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
