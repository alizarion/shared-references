package com.alizarion.reference.address.jpa;

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
    public void testEntities(){

        //Get a new transaction
        EntityTransaction trx = em.getTransaction();
        try {

            //Start the transaction
            trx.begin();
            //Persist the object in the DB
            //TODO persist some objects
            //Commit and end the transaction
            trx.commit();
        } catch (RuntimeException e) {
            if (trx != null && trx.isActive()) {
                trx.rollback();
            }
            throw e;
        }

    }
}
