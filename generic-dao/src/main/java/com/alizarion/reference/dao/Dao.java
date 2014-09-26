package com.alizarion.reference.dao;

import java.util.List;

/**
 * Generic Data Access Object interface, that contain main method, <br/>
 * and must be implemented by all orms data access layer
 * @author Selim BENSENOUCI
 * @see com.alizarion.reference.dao.jpa.JpaDao
 */
public interface Dao<K,E> {

    /**
     * Method to persist dao entity.
     * @param entity to persist
     */
    void persist(E entity);

    /**
     * Method to merge dao entity.
     * @param entity to merge
     * @return merged entity
     */
    E merge(E entity);

    /**
     * Method to get entity by id.
     * @param id identifier of the desired entity
     * @return founded entity
     */
    E find(K id);

    /**
     * Method to remove a persisted entity
     * @param entity entity to remove
     */
    void remove(E entity);

    /**
     * Method to fil all entities
     * @return  list of entities
     */
    List<E> findAll();


}
