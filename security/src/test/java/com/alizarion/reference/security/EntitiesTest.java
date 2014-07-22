package com.alizarion.reference.security;

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
        emf = Persistence.createEntityManagerFactory("SecurityTestPU");
        em = emf.createEntityManager();

    }

    @After
    public void destroy(){
        em.close();
        emf.close();
    }

    /**
     * Simple Method to test coherency of jpa annotations
     */
    @Test
    public void TestJPAEntities(){
        EntityTransaction trx = em.getTransaction();

        try {
            // Start new transaction
            trx.begin();

            // Add some objects

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
