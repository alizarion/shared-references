package com.alizarion.reference.emailing;

import com.alizarion.reference.emailing.dao.EmailDao;
import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.entities.GenericRegisterEmail;
import com.alizarion.reference.emailing.helper.EmailTestHelper;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import javax.mail.internet.AddressException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author selim Bensneouci.
 */
public class EmailEntitiesTest {

    private static EntityManagerFactory managerFactory ;
    private static EntityManager entityManager;

    private Connection connection;
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @BeforeClass
    public static void before(){

        managerFactory = Persistence.createEntityManagerFactory("emailingPU");
        entityManager =  managerFactory.createEntityManager();
    }

    @AfterClass
    public static void after() throws SQLException {
        entityManager.close();
        managerFactory.close();

    }

    @Test
    public void entityBiding() throws AddressException {

        EntityTransaction trx = entityManager.getTransaction();
        GenericRegisterEmail email = EmailTestHelper.getGenericRegisterEmail(temporaryFolder.getRoot().toURI());
        trx.begin();
        email.setSendDate(new Date());
        entityManager.persist(email);
        entityManager.flush();
        trx.commit();
        List<Email> list =  entityManager.createQuery("select e from Email e").getResultList();

    }

    @Test
    public void countEmails() throws AddressException {

        EntityTransaction trx = entityManager.getTransaction();

        EmailDao emailDao =  new EmailDao(entityManager);

        trx.begin();
        Integer count = emailDao.countSentEmailSince(new Date(new Date().getTime() - (3600 * 1000)));
        trx.commit();
        Assert.assertEquals((int)count,1);
    }



}
