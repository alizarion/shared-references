package com.alizarion.reference.resource.entities;

import com.alizarion.reference.resource.dao.ResourceDao;
import com.alizarion.reference.resource.exception.PersistentResourceNotFoundException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class PersistentResourceTest {

    EntityManagerFactory emf ;
    EntityManager em;

    @Before
    public void setUp() throws Exception {
        emf = Persistence.createEntityManagerFactory("ResourcesTextPU");
        em = emf.createEntityManager();
        EntityManager entityManager = mock(EntityManager.class);

    }

    @After
    public void tearDown() throws Exception {
        em.close();
        emf.close();

    }

    @Test
    public void persistResourcesTest() throws PersistentResourceNotFoundException {

        EntityTransaction trx = em.getTransaction();
        trx.begin();
        PersistentResource persistentResource = new PersistentResource();
        persistentResource.setCategory("fake.proprieties.for.unit.test");
        persistentResource.setKey(FakeMBeanEntity.KEY);
        persistentResource.setValue("myFakeValue");
        em.merge(persistentResource);
        FakeMBeanEntity fakeMBeanEntity =new FakeMBeanEntity();
        fakeMBeanEntity.setEm(em);
        String keyValue = fakeMBeanEntity.getKeyValue();
        Assert.assertEquals(persistentResource.getValue(), keyValue);
        List<PersistentResource> persistentResources = new ResourceDao(em).findAll();
        assertFalse(persistentResources.isEmpty());
        trx.commit();

    }

    @Test
    public void testEntities(){



    }


}