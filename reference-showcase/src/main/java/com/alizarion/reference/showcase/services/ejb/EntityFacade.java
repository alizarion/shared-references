package com.alizarion.reference.showcase.services.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 * @author selim@openlinux.fr.
 */
@Stateless
@Named
public class EntityFacade implements Serializable {

    private static final long serialVersionUID = -408231799490128702L;

    @PersistenceContext
    EntityManager  em;

    @PostConstruct
    public void setUp(){
          //TODO instancier dao manager
    }

}
