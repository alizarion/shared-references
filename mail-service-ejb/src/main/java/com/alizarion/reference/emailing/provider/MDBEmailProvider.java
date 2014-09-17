package com.alizarion.reference.emailing.provider;

import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.exception.EmailException;
import com.alizarion.reference.emailing.exception.SendingEmailException;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.*;
import java.util.Date;
import java.util.Queue;

/**
 * @author selim@openlinux.fr.
 */
@JMSDestinationDefinition(
        name = "java:/queue/EmailSendingQueue",
        interfaceName = "javax.jms.Queue",
        destinationName = "EmailSendingQueue"
)
@Stateless
public class MDBEmailProvider implements EmailProvider {

    @Inject
    private JMSContext context;

    @Resource(lookup = "java:/queue/EmailSendingQueue")
    private Queue queue;

    @Override
    public  Email sendMail(Email email) throws EmailException {
        try {
            ObjectMessage objectMessage =  context.createObjectMessage();
            objectMessage.setObject(email);
            final Destination destination  = (Destination) queue;
            context.createProducer().send(destination, objectMessage);
        } catch (JMSException | ClassCastException e) {
            throw new SendingEmailException("Error on sending " +
                    "email to jms queue : " +
                    email.toString(),e);
        }
        email.setSendDate(new Date());
        return email;
    }
}
