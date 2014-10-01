package com.alizarion.reference.security.entities.oauth.server;

import com.alizarion.reference.security.entities.CredentialRole;
import com.alizarion.reference.security.tools.SecurityHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * class that contain main information of each <br/>
 * Oauth register application, must be extended in each<br/>
 * application to define extra information like <br/>
 * application avatar, link person ...
 * @author selim@openlinux.fr.
 * @see OAuthApplicationKey
 */
@Entity
@Table(name = "security_oauth_application")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NamedQueries({@NamedQuery(name = OAuthApplication.FIND_BY_ClIENT_ID,
        query = "select oa from OAuthApplication oa where" +
                " oa.applicationKey.clientId = :clientId " ),
        @NamedQuery(name = OAuthApplication.FIND_BY_APP_NAME,
                query = "select oa from OAuthApplication" +
                        " oa where oa.name = :appName")})
public abstract class OAuthApplication implements Serializable {

    private static final long serialVersionUID = 7604489775936326241L;

    public final static String FIND_BY_APP_NAME = "OAuthApplication.FIND_BY_APP_NAME";
    public final static String FIND_BY_ClIENT_ID = "OAuthApplication.FIND_BY_ClIENT_ID";


    @Id
    @TableGenerator(name = "security_oauth_application_SEQ",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT",
            table = "sequence")
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "security_oauth_application_SEQ")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "home_page_url",
            nullable = false,
            unique = true)
    private URL homePage;

    @Column(name = "callback_url",
            nullable = false,
            unique = true)
    private URL callback;

    @Embedded
    private OAuthApplicationKey applicationKey;

    @OneToMany(cascade = {CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REMOVE})
    private Set<OAuthAuthorization> authorizations = new HashSet<>();


    protected OAuthApplication() {

    }

    public OAuthApplication(final String name,
                            final URL homePage,
                            final URL callback) {
        this.name = name;
        this.homePage = homePage;
        this.callback = callback;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getHomePage() {
        return homePage;
    }

    public void setHomePage(URL homePage) {
        this.homePage = homePage;
    }

    public URL getCallback() {
        return callback;
    }

    public void setCallback(URL callback) {
        this.callback = callback;
    }

    public OAuthAuthorization addAuthorization(final long refreshTokenDuration,
                                               final Set<CredentialRole> roles){
        OAuthAuthorization  authorization =  new OAuthAuthorization(
                refreshTokenDuration,this,roles);
        this.authorizations.add(authorization);
        return authorization;
    }


    public Set<OAuthAuthorization> getAuthorizations() {
        return authorizations;
    }

    public OAuthApplicationKey getApplicationKey() {
        return applicationKey;
    }

    @PrePersist
    public void prePersist(){
        this.applicationKey = new OAuthApplicationKey(
                UUID.randomUUID().toString(),
                SecurityHelper.
                        getRandomAlphaNumericString(300));
    }

    @Override
    public String toString() {
        return "OAuthApplication{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", homePage=" + homePage +
                ", callback=" + callback +
                ", applicationKey=" + applicationKey +
                ", authorizations=" + authorizations.size() +
                '}';
    }
}
