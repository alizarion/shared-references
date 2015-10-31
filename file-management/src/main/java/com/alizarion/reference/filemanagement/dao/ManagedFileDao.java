package com.alizarion.reference.filemanagement.dao;

import com.alizarion.reference.filemanagement.entities.ManagedFile;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author selim@openlinux.fr.
 */
public class ManagedFileDao {


    EntityManager em;

    public ManagedFileDao(EntityManager em) {
        this.em =  em;
    }

    @SuppressWarnings("unchecked")
    public ManagedFile getManagedFileByUUID(String uuid){
        List<ManagedFile> result =  this.em.createNamedQuery(ManagedFile.FIND_BY_UUID).setParameter("uuid",uuid).getResultList();
        if (result.isEmpty()){
            return null;
        } else {
            return result.get(0);
        }
    }

}
