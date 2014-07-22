package com.alizarion.reference.address.jpa;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author selim@openlinux.fr.
 */
public class EntitiesTest {

      @Test
    public void testEntities(){
          // Get the entity manager for the tests.
          EntityManagerFactory emf = Persistence.createEntityManagerFactory("ingressPU");
          EntityManager em = emf.createEntityManager();
          EntityTransaction trx = em.getTransaction();

          try {
             //Get a new transaction

             //Start the transaction
             trx.begin();
             //Persist the object in the DB
             //Commit and end the transaction
             trx.commit();
          } catch (RuntimeException e) {
             if (trx != null && trx.isActive()) {
                trx.rollback();
             }
             throw e;
          } finally {
             //Close the manager
             em.close();
             emf.close();
          }

    }
}
