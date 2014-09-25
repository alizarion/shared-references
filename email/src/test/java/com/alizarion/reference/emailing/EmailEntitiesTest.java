package com.alizarion.reference.emailing;

import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.entities.GenericRegisterEmail;
import com.alizarion.reference.emailing.helper.EmailTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * @author selim Bensneouci.
 */
public class EmailEntitiesTest {

    EntityManagerFactory managerFactory ;
    EntityManager entityManager;

    private Connection connection;
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void before(){

        managerFactory = Persistence.createEntityManagerFactory("emailingPU");
        entityManager =  managerFactory.createEntityManager();
    }

    @After
    public void after() throws SQLException {
        entityManager.close();
        managerFactory.close();

    }

    @Test
    public void entityBiding(){

        EntityTransaction trx = entityManager.getTransaction();
        GenericRegisterEmail email = EmailTestHelper.getGenericRegisterEmail(temporaryFolder.getRoot().toURI());
        trx.begin();
        entityManager.persist(email);
        entityManager.flush();
        trx.commit();
        List<Email> list =  entityManager.createQuery("select e from Email e").getResultList();

    }



}
