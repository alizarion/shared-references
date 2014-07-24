package com.alizarion.reference.social.jpa;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author selim@openlinux.fr.
 */
public class EntitiesTest {

    EntityManagerFactory emf;
    EntityManager em;

    @Before
    public void init(){
        emf = Persistence.createEntityManagerFactory("socialTestPU");
        em =  emf.createEntityManager();
    }

    @After
    public void destroy(){
        em.close();
        emf.close();
    }

    @Test
    public void testJPAEntities(){
        EntityTransaction trx = em.getTransaction();

        trx.begin();


        trx.commit();


    }

}
