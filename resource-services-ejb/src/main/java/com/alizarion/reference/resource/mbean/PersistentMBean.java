package com.alizarion.reference.resource.mbean;

import com.alizarion.reference.resource.entities.PersistentResource;
import com.alizarion.reference.resource.exception.PersistentResourceNotFoundException;

import javax.ejb.Stateless;
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
@Stateless
public abstract class PersistentMBean implements Serializable {

    private static final long serialVersionUID = -8901442021995218836L;

    @PersistenceContext
    EntityManager em;

    public abstract String getCategory();


    public void setEm(EntityManager em) {
        this.em = em;
    }

    public String getValue(String s) throws PersistentResourceNotFoundException {
        try{
            PersistentResource persistentEntry = (PersistentResource)
                    em.createNamedQuery(PersistentResource.
                            GET_PERSISTENT_RESOURCE_VALUE_BY_KEY).
                            setParameter("category", getCategory()).
                            setParameter("key", s).getSingleResult();

            return persistentEntry.getValue();
        } catch (NoResultException e){
            throw new PersistentResourceNotFoundException("No values for this key :" + s,e);
        }
    }

    public void setValue(String key, String value) throws PersistentResourceNotFoundException {
        try{
            PersistentResource persistentEntry = (PersistentResource)
                    em.createNamedQuery(PersistentResource.
                            GET_PERSISTENT_RESOURCE_VALUE_BY_KEY).
                            setParameter("category", getCategory()).
                            setParameter("key", key).getSingleResult();

            persistentEntry.setValue(value);
            em.merge(persistentEntry);

        } catch (NoResultException e){
            throw new PersistentResourceNotFoundException("No values for this key :" + key,e);
        }
    }


    public URI getValueAsURI(String s) throws URISyntaxException, PersistentResourceNotFoundException {
        return new URI(getValue(s));
    }


}
