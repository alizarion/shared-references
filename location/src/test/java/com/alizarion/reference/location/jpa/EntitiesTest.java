package com.alizarion.reference.location.jpa;

import com.alizarion.reference.location.entities.ElectronicAddress;
import com.alizarion.reference.location.entities.SimpleEntity;
import com.alizarion.reference.location.entities.WebAddress;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author selim@openlinux.fr.
 */
public class EntitiesTest {

    private EntityManagerFactory emf;
    private EntityManager em;


    @Before
    public void init(){
        emf = Persistence.createEntityManagerFactory("AddressTestPU");
        em = emf.createEntityManager();
    }

    @After
    public void destroy(){
        em.close();
        emf.close();
    }

    @Test
    public void testEntities() throws
            MalformedURLException,
            URISyntaxException {

        //Get a new transaction
        EntityTransaction trx = em.getTransaction();

            //Start the transaction
            trx.begin();
            //Persist the object in the DB
            WebAddress webAddress = new WebAddress("http://pixefolio.com/cecile") ;

            em.persist(webAddress);
            //TODO persist some objects
            //Commit and end the transaction
            List<WebAddress> webAddresses =
                    em.createQuery("SELECT we FROM WebAddress we ").
                            getResultList();
            assertFalse(webAddresses.isEmpty());
            trx.commit();
    }


    @Test
    public void testEntityPersistenceCascade() throws AddressException {

        SimpleEntity entity =  new SimpleEntity();

        entity.setElectronicAddress(new ElectronicAddress(new InternetAddress("selim@openlinux.fr")));

        EntityTransaction  trx = this.em.getTransaction();
        trx.begin();
        entity = this.em.merge(entity);
        trx.commit();
        assertNotNull(entity.getElectronicAddress().getId());

    }
}
