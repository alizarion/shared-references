package com.alizarion.reference.security.entities.oauth;

import com.alizarion.reference.security.entities.Credential;
import com.alizarion.reference.security.entities.RoleGroup;
import com.alizarion.reference.security.exception.oauth.InvalidScopeException;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URL;
import java.util.Set;

/**
 * class that contain main information of each <br/>
 * Oauth register application, must be extended in each<br/>
 * application to define extra information like <br/>
 * application avatar, link person ...
 * @author selim@openlinux.fr.
 * @see OAuthApplicationKey
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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
    protected OAuthApplicationKey applicationKey;

    /**
     * default in app role that
     * this service will grant.
     */
    @OneToOne
    protected RoleGroup defaultRoles;

    protected OAuthApplication() {

    }

    public OAuthApplication(final String name,
                            final URL homePage,
                            final URL callback) {
        this.name = name;
        this.homePage = homePage;
        this.callback = callback;
    }

    public Long getId() {
        return id;
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

    public RoleGroup getDefaultRoles() {
        return defaultRoles;
    }

    public void setDefaultRoles(RoleGroup defaultRoles) {
        this.defaultRoles = defaultRoles;
    }

    public abstract OAuthAuthorization addAuthorization(
            final Credential credential,
            final Set<String> roles) throws InvalidScopeException;

    public void setId(Long id) {
        this.id = id;
    }

    public OAuthApplicationKey getApplicationKey() {
        return applicationKey;
    }


    @Override
    public String toString() {
        return "OAuthApplication{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", homePage=" + homePage +
                ", callback=" + callback +
                ", applicationKey=" + applicationKey +
                '}';
    }
}
