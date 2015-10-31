package com.alizarion.reference.security.entities;

import com.alizarion.reference.exception.NotImplementedException;
import com.alizarion.reference.location.entities.ElectronicAddress;
import com.alizarion.reference.person.entities.PhysicalPerson;
import com.alizarion.reference.security.oauth.oauth2.entities.OAuthCredential;
import com.alizarion.reference.security.oauth.oauth2.entities.OAuthRole;
import com.alizarion.reference.security.tools.SecurityHelper;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.*;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * This class contain credentials information for user login.
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "security_credential")
public class Credential implements OAuthCredential<OAuthRole,Long>,Serializable {

    private static final long serialVersionUID = 8712203414431845759L;


    @Id
    @TableGenerator(name="security_credential_SEQ",
            table="sequence",
            pkColumnName="SEQ_NAME",
            valueColumnName="SEQ_COUNT")
    @GeneratedValue(strategy= GenerationType.TABLE,
            generator="security_credential_SEQ")
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name",
            unique = true)
    private String username;

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

    @OneToOne(cascade ={
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH})
    @JoinColumn(name = "person_id")
    private PhysicalPerson person;


    @Deprecated
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
                      final String password,
                      ElectronicAddress electronicAddress,
                      final Set<Role>
                              roles) {
        this.username = userName;
        setPassword(password);
        this.state = CredentialState.P;
        this.logon =  LogOnType.P;
        this.person = new PhysicalPerson();
        this.person.setPrimaryElectronicAddress(electronicAddress);
        this.creationDate = new Date();
        for (Role  role : roles){
            this.credentialRoles.
                    add(new CredentialRole(role,this));
        }
    }

    public Credential() {
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getIdToString() {
        return this.id.toString();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LogOnType getLogon() {
        return logon;
    }

    public void setLogon(LogOnType logon) {
        this.logon = logon;
    }

    public PhysicalPerson getPerson() {
        return person;
    }

    public void setPerson(PhysicalPerson person) {
        this.person = person;
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
        return this.username;
    }

    @Override
    public Set<String> getAvailableScopes() {
        Set<String> scopes = new HashSet<>();
        for (Role role:getRoles()){
            scopes.add(role.getRoleKey().getKey());
        }
        return scopes;
    }

    public void setEmail(final ElectronicAddress email){
        this.person.setPrimaryElectronicAddress(email);
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

    @Override
    public InternetAddress getEmail() {
        try {
            return this.person.getPrimaryElectronicAddress().getInternetAddress();
        } catch (AddressException e) {
            return null;
        }
    }

    @Override
    public Boolean isEmailVerified() {
        return this.person
                .getPrimaryElectronicAddress()
                .getVerified();
    }

    @Override
    public Locale getLocale() {
        return this.person.getLocale();
    }

    @Override
    public String getName() throws NotImplementedException {
        return this.person.getFirstName() + " " +  this.person.getLastName();
    }

    @Override
    public String getGender() throws NotImplementedException {
        return this.person.getTitle().getGenderKey();
    }

    @Override
    public String getFamilyName() throws NotImplementedException {
        return this.person.getLastName();
    }

    @Override
    public String getGivenName() throws NotImplementedException {
        return this.person.getFirstName();
    }

    @Override
    public String getNickname() throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override
    public URL getPicture() throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override
    public URL getWebSite() throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override
    public URL getProfile() throws NotImplementedException {
        throw new NotImplementedException();
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

    public void setUsername(String userName) {
        this.username = userName;
    }

    @Deprecated
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

        return !(creationDate != null ? !creationDate.equals(
                that.creationDate) : that.creationDate != null)
                && !(password != null ? !password.equals(that.password) :
                that.password != null) && !(username != null ?
                !username.equals(that.username) : that.username != null);

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
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
