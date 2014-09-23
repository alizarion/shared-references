package com.alizarion.reference.email.services;

import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.exception.EmailException;
import com.alizarion.reference.emailing.exception.SendingEmailException;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.*;

/**
 * @author selim@openlinux.fr.
 */
@MessageDriven(name = "EmailSendingQueue", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup",
                propertyValue = "queue/EmailSendingQueue"),
        @ActivationConfigProperty(propertyName = "destinationType",
                propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode",
                propertyValue = "Auto-acknowledge") })
public class MDBEmailQueue implements MessageListener {

    @EJB
    private EmailProviderService emailService;

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
