package com.alizarion.reference.email.services;

import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.exception.EmailException;
import com.alizarion.reference.emailing.exception.SendingEmailException;
import com.alizarion.reference.emailing.provider.EmailProvider;

import javax.ejb.*;
import javax.jms.*;

/**
 * @author selim@openlinux.fr.
 */
@MessageDriven(name = "EmailSendingMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/queue/EMAILSENDINGQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "DLQMaxResent", propertyValue = "0")
})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class MDBEmailQueue implements MessageListener{

    private static final long serialVersionUID = 1910345343612365629L;


    @EJB(beanName = "EmailProviderService")
    private EmailProvider emailService;

    @Override
    public void onMessage(Message message) {
        if(message instanceof TextMessage){
            throw new SendingEmailException("Error on catching message," +
                    " Email and expected return TextMessage Object");
        } else if (message instanceof ObjectMessage){
            ObjectMessage objectMessage =  (ObjectMessage) message;
            try {
                Email email = (Email) objectMessage.getObject();
                emailService.sendMail(email);
            } catch (JMSException | EmailException e) {
                throw new SendingEmailException("Exception" +
                        " during manage queued email : " ,e );
            }
        }   else {
            throw new SendingEmailException("Error on catching message," +
                    " Email and expected return an unknown object");
        }

    }
}
