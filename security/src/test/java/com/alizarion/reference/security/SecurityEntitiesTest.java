package com.alizarion.reference.security;

import com.alizarion.reference.security.entities.Credential;
import com.alizarion.reference.security.entities.Role;

import com.alizarion.reference.security.oauth.entities.OAuthAccessToken;
import com.alizarion.reference.security.oauth.entities.client.OAuthServerApplication;
import com.alizarion.reference.security.oauth.entities.server.OAuthClientApplication;
import com.alizarion.reference.security.oauth.exception.OAuthException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author selim@openlinux.fr.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class SecurityEntitiesTest {

    EntityManagerFactory emf;
    EntityManager em;


    private Set<Role> clientRoles = new HashSet<>();
    private Set<Role> serverRoles = new HashSet<>();


    private  EntityTransaction trx ;
    /**
     * credential that result by the server auth access
     */
    private Credential clientCredential;



    /**
     * server credential that have been
     * requested by the client to the server.
     */
    private Credential serverCredential;

    /**
     * to use in other project as test-jar,
     * just inject the EM and call methods
     * @param em entity manager
     */
    public void init(EntityManager em) {
        this.em = em;
    }

    @Rule
    public ExpectedException expectedException =
            ExpectedException.none();

    @Before
    public void init(){
        emf = Persistence.createEntityManagerFactory("SecurityTestPU");
        em = emf.createEntityManager();
        this.trx = em.getTransaction() ;
        this.clientRoles.add(SecurityTestFactory.getRole("user"));
        this.clientRoles.add(SecurityTestFactory.getRole("auth"));
        this.serverRoles.add(SecurityTestFactory.getRole("appUser"));
        this.serverRoles.add(SecurityTestFactory.getRole("appNotif"));
        trx.begin();
        Set<Role> updatedRoles = new HashSet<>();
        for (Role role : this.clientRoles) {
            updatedRoles.add(em.merge(role));
            //em.refresh(role);
        }
        this.clientRoles = updatedRoles;

        Set<Role> updatedServerRoles = new HashSet<>();
        for (Role role : this.serverRoles) {
            updatedServerRoles.add(em.merge(role));
            //em.refresh(role);
        }
        this.serverRoles = updatedServerRoles;

        this.serverCredential = SecurityTestFactory.
                getPasswordRegistredCredential("server-user", this.serverRoles);

        this.serverCredential =  em.merge(this.serverCredential);
    }

    @After
    public void destroy(){
        trx.commit();

        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }


    /**
     * Simple Method to test coherency of jpa annotations
     */
    @Test
    public void a_persist_clientApplication() throws MalformedURLException, URISyntaxException {
        OAuthClientApplication application =
                SecurityTestFactory.
                        getOAuthClientApplication(this.serverRoles);
        em.merge(application) ;
        // Add some objects
    }


    /**
     * Simple Method to test coherency of jpa annotations
     */
    //@Test
    public void b_persist_serverApplication() throws MalformedURLException, URISyntaxException {
        OAuthServerApplication application =
                SecurityTestFactory.
                        getOAuthServerApplication(this.clientRoles);
        em.merge(application) ;
        // Add some objects
    }




    /**
     * Simple Method to test coherency of jpa annotations
     */
    // @Test
    public void TestOauthEntitiesPersist()
            throws MalformedURLException, OAuthException, URISyntaxException {
        this.expectedException.expect(OAuthException.class);
        EntityTransaction trx = em.getTransaction();
        OAuthClientApplication oauthApplication =
                new OAuthClientApplication("Pixefolio.com",
                        new URL("http://pixefolio.com"),
                        new URI("http://pixefolio.com/callback"));
        try {
            // Start new transaction
            trx.begin();
            oauthApplication = this.em.merge(oauthApplication);
            // Add some objects
            //TODO corrigé le test
            // oauthApplication.addAuthorization(3000);
            System.out.println(oauthApplication);
            System.out.println(new ArrayList<>(
                    oauthApplication.
                            getAuthorizations()).get(0));
            OAuthAccessToken accessToken = new ArrayList<>(
                    oauthApplication.getAuthorizations()).
                    get(0).addAccessToken(34L);
            oauthApplication = this.em.merge(oauthApplication);
            this.em.flush();
            this.em.clear();
            OAuthAccessToken accessToken2  = this.em.find(
                    OAuthAccessToken.class,accessToken.getBearer());

            System.out.println(accessToken2);
            Assert.assertNotNull(oauthApplication.
                    getApplicationKey().toString());
            Assert.assertNotNull(accessToken2.toString());
            System.out.println(accessToken2.
                    getAuthorization().getCredential());
            // commit the transaction
            trx.commit();
        } catch (RuntimeException e) {
            if (trx != null && trx.isActive()) {
                trx.rollback();
            }
            throw e;
        }


    }
}
