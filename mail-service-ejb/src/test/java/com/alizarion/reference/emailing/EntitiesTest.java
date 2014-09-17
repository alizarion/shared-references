package com.alizarion.reference.emailing;

import com.alizarion.reference.emailing.impl.RegisterEmail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Locale;

/**
 * @author selim@openlinux.fr.
 */
public class EntitiesTest {

    EntityManagerFactory managerFactory ;
    EntityManager entityManager;

    @Before
    public void before(){
        managerFactory = Persistence.createEntityManagerFactory("emailingPU");
        entityManager =  managerFactory.createEntityManager();
    }

    @After
    public void after(){
        entityManager.close();
        managerFactory.close();
    }

    @Test
    public void entityBiding(){

        EntityTransaction trx = entityManager.getTransaction();

        RegisterEmail email = (RegisterEmail) new RegisterEmail.RegisterEmailBuilder(new SimpleTestUser("dsdqs","dqsdqs","cxcvxc")).setTo("dsdfsdf").setCci("fdsfsdf").setCc("dsqdqsd").setFrom("qsdsqdqd").setLocale(Locale.FRENCH).builder();
        trx.begin();
        entityManager.persist(email);
        trx.commit();
    }



}
