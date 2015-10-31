package com.alizarion.reference.email.services;

import com.alizarion.reference.emailing.dao.EmailDao;
import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.exception.EmailException;
import com.alizarion.reference.emailing.exception.EmailMaxQuotaException;
import com.alizarion.reference.emailing.exception.SendingEmailException;
import com.alizarion.reference.emailing.provider.EmailProvider;
import com.alizarion.reference.resource.exception.PersistentResourceNotFoundException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author selim@openlinux.fr.
 */
@Stateless
public class EmailProviderService implements EmailProvider ,Serializable {

    private static final long serialVersionUID = 6398768495566279758L;
    /**
     * Resource for sending the email. The mail subsystem
     * is defined in either standalone.xml
     * or domain.xml in your respective
     * configuration directory.
     */
    @Resource(mappedName = "java:jboss/mail/Default")
    private Session mailSession;

    @PersistenceContext
    EntityManager em;


    private EmailDao  emailDao;


    @EJB
    EmailMBean emailMBean;


    @PostConstruct
    public void setUp(){
        this.emailDao = new EmailDao(this.em);
    }

    private ResourceBundle resourceBundle = ResourceBundle.getBundle("email");

    @Override
    public Email sendMail(Email email) throws EmailException {
        MimeMessage message =  email.getMessage(
                mailSession,20);
        Integer emailSentSince1Hour = emailDao.countSentEmailSince(
                                    new Date(new Date().getTime()-3600000));
        try {
            if (emailMBean.getMaxPerHour() < emailSentSince1Hour){
                   throw  new EmailMaxQuotaException(emailSentSince1Hour +
                           "email sent since last hour, " +
                           "and only  "+emailMBean.getMaxPerHour()+" are permit" );
            }  else {
                Transport.send(message);
            }
        } catch (MessagingException  | PersistentResourceNotFoundException e) {
            throw new SendingEmailException("Unexpected error " +
                    "on sending email  "+ email.toString(),e);
        }
        email.setSendDate(new Date());
        return email;
    }
}
