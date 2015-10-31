package com.alizarion.reference.emailing.dao;

import com.alizarion.reference.dao.jpa.JpaDao;
import com.alizarion.reference.emailing.entities.Email;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author selim@openlinux.fr.
 */
public class EmailDao extends JpaDao<Long,Email> {

    public EmailDao(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * Method to count sent emails since a specific date.
     * @param date date since we count.
     * @return sent email count.
     */
    public Integer countSentEmailSince(Date date){
        return ((Long)this.em.createNamedQuery(Email.COUNT_SENT_EMAIL)
                .setParameter("since",date, TemporalType.TIMESTAMP)
                .getSingleResult()).intValue();
    }
}
