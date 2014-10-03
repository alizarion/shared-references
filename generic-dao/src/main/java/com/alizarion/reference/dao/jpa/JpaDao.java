package com.alizarion.reference.dao.jpa;

import com.alizarion.reference.dao.Dao;

import javax.persistence.EntityManager;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Java Persistence API implementation of the generic DAO.
 * @author selim@openlinux.fr.
 * @see com.alizarion.reference.dao.Dao
 */
public abstract class JpaDao<K,E>  implements Dao<K,E>{

    protected Class<E> entityClass;

    protected EntityManager em;

    @SuppressWarnings(value = "unchecked")
    protected JpaDao(EntityManager entityManager) {
        this.em = entityManager;
        ParameterizedType genericSuperclass =
                (ParameterizedType) getClass()
                        .getGenericSuperclass();
        this.entityClass = (Class<E>)
                genericSuperclass.getActualTypeArguments()[1];
    }

    public void persist(E entity){
        em.persist(entity);
    }

    @Override
    public E merge(E entity) {
        return  em.merge(entity);
    }

    @Override
    public E find(K id) {
        return em.find(entityClass,id);
    }

    @Override
    public void remove(E entity) {
        em.remove(entity);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<E> findAll() {
        return em.createQuery("select e from "+
                entityClass.getName() +
                " e").getResultList();
    }



    public EntityManager getEntityManager(){
        return this.em;
    }
}
