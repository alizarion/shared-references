package com.alizarion.reference.resource.mbean;

import com.alizarion.reference.resource.entities.PersistentResource;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Persistent Mbean must be extented by all
 * resources params that need to be persisted
 * @author selim@openlinux.fr.
 */
public abstract class PersistentMBean implements Serializable {

    @PersistenceContext
    EntityManager em;

    public abstract String getCategory();


    public void setEm(EntityManager em) {
        this.em = em;
    }

    public String getValue(String s){
        try{
            PersistentResource persistentEntry = (PersistentResource)
                    em.createNamedQuery(PersistentResource.
                            GET_PERSISTENT_RESOURCE_VALUE_BY_KEY).
                            setParameter("category", getCategory()).
                            setParameter("key", s).getSingleResult();

            return persistentEntry.getValue();
        } catch (NoResultException e){
            //TODO add specific exception hanlder
            return null;
        }
    }

    public void setValue(String key, String value){
        try{
            PersistentResource persistentEntry = (PersistentResource)
                    em.createNamedQuery(PersistentResource.
                            GET_PERSISTENT_RESOURCE_VALUE_BY_KEY).
                            setParameter("category", getCategory()).
                            setParameter("key", key).getSingleResult();

            persistentEntry.setValue(value);
            em.merge(persistentEntry);

        } catch (NoResultException e){
            //TODO add specific exception hanlder

        }
    }


    public URI getValueAsURI(String s) throws URISyntaxException {
        return new URI(getValue(s));
        //TODO add specific exception hanlder
    }


}
