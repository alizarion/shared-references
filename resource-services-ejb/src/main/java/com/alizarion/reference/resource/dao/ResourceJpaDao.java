package com.alizarion.reference.resource.dao;

import com.alizarion.reference.dao.jpa.JpaDao;
import com.alizarion.reference.resource.entities.PersistentResource;

import javax.persistence.EntityManager;

/**
 * @author selim@openlinux.fr.
 */
public class ResourceJpaDao extends JpaDao<Long,PersistentResource> {

    public ResourceJpaDao(EntityManager entityManager) {
        super(entityManager);
    }

}
