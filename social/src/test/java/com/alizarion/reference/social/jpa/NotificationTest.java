package com.alizarion.reference.social.jpa;

import com.alizarion.reference.social.entities.UserTest;
import com.alizarion.reference.social.entities.comment.Comment;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author selim@openlinux.fr.
 */
public class NotificationTest {

    private UserTest cecile ;
    private UserTest selim ;

    EntityManager entityManager;

    EntityManagerFactory entityManagerFactory;



    @Before
    public void setUp(){
        cecile = new UserTest("cecile","guyart");
        selim = new UserTest("selim","bensenouci");

        this.entityManagerFactory = Persistence.createEntityManagerFactory("socialTestPU");
        this.entityManager =  this.entityManagerFactory.createEntityManager();
        EntityTransaction trx =  this.entityManager.getTransaction();

        trx.begin();
        cecile = entityManager.merge(cecile);
        entityManager.flush();
        selim = entityManager.merge(selim);
        entityManager.flush();
        // selim add dislike on cecile profile => send a dislike notification to cecile
        cecile.getUserProfile().addDisLikeAppreciation(selim);

        // selim add new comment
        selim.addNewComment("blablablablabla mo blablalba");
        cecile = entityManager.merge(cecile);

        selim = entityManager.merge(selim);

        entityManager.flush();

        //cecile dislike and injure all selim comment => push a dislike and comment notifications
        for (Comment comment :  selim.getComments()){
            comment.addDisLikeAppreciation(cecile);
            comment.addAnswerToComment(cecile,"bouuuo nul!!!");
        }
        cecile = entityManager.merge(cecile);
        selim = entityManager.merge(selim);


        trx.commit();

    }

    @After
    public void breakDown(){
        this.entityManager.close();
        this.entityManagerFactory.close();
    }

    @Test
    public void testNotif(){
        EntityTransaction trx =  this.entityManager.getTransaction();
        trx.begin();
        cecile = entityManager.merge(cecile);
        entityManager.flush();
        selim = entityManager.merge(selim);
        trx.commit();
        Assert.assertFalse(!(cecile.getNotifications().size()>0));
        selim.printNotifications();
        cecile.printNotifications();

    }

    @Test
    public void fetchEntities(){
        EntityTransaction trx =  this.entityManager.getTransaction();
        trx.begin();
        UserTest selimBis = (UserTest)this.entityManager.find(UserTest.class,selim.getId());
        UserTest cecileBis = (UserTest)this.entityManager.find(UserTest.class,cecile.getId());
        selimBis.printNotifications();
        cecileBis.printNotifications();
        Assert.assertEquals(cecileBis.toString(),cecile.toString());
        Assert.assertEquals(selimBis.toString(),selim.toString());

        trx.commit();
    }
}
