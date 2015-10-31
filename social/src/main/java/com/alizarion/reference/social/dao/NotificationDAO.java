package com.alizarion.reference.social.dao;

import com.alizarion.reference.social.entities.notification.Notification;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.*;

/**
 * @author selim@openlinux.fr.
 */
public class NotificationDAO implements Serializable{

    private static final long serialVersionUID = 1076544957138027149L;

    private EntityManager em;

    public NotificationDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Method to get notification by observer.
     * @param observerId Observerid
     * @param maxCount max result length
     * @param start start search from timestamp
     * @param setAsViewed set all retrieved notification as viewed
     * @return Set of notifications
     */

    @SuppressWarnings("unchecked")
    public Set<Notification> findInAppNotificationByObserver(
            final Long observerId
            ,final Integer maxCount
            ,Long start
            ,final Boolean setAsViewed){

        if (observerId != null){
            Date startDate ;
            if (start == null){
                startDate = new Date();
                start = startDate.getTime();
            }  else {
                startDate = new Date(start);
            }
            List<Notification> tempResult;
            if(maxCount != null) {
                tempResult = this.em.createNamedQuery(Notification.FIND_INAPP_BY_OBSERVER)
                        .setParameter("observerId", observerId)
                        .setMaxResults(maxCount)
                        .setParameter("startDate", startDate, TemporalType.TIMESTAMP)
                        .getResultList();
            } else {
                tempResult = this.em.createNamedQuery(Notification.FIND_INAPP_BY_OBSERVER)
                        .setParameter("observerId", observerId)
                        .setParameter("startDate", startDate, TemporalType.TIMESTAMP)
                        .getResultList();
            }
            Set<Notification> finalResult = new HashSet<>();
            for (Notification notification :tempResult){
                if(setAsViewed){
                    notification.setViewed(true);
                }
                if(notification.getCreationDate().getTime() < start){
                    finalResult.add(notification);
                }
            }

            return finalResult;
        } else {
            return null;
        }

    }



    public Integer findUnReadNotificaitonCount(final Long observerId){
       return  ((Long)this.em.createNamedQuery(
               Notification.COUNT_UNREAD_BY_OBSERVER)
                                .setParameter("observerId", observerId)
                                .getSingleResult()).intValue();
    }
}
