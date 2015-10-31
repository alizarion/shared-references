package com.alizarion.reference.email.services;

import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.exception.EmailException;
import com.alizarion.reference.emailing.exception.SendingEmailException;
import com.alizarion.reference.emailing.provider.EmailProvider;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author selim@openlinux.fr.
 */


@JMSDestinationDefinitions(
        value =  {
                @JMSDestinationDefinition(
                        name = "java:/jms/queue/EMAILSENDINGQueue",
                        interfaceName = "javax.jms.Queue",
                        destinationName = "EmailSendingMDB"
                )
        }
)
@Stateless
public class MDBEmailProvider implements EmailProvider, Serializable {

    private static final long serialVersionUID = 1910345034361673629L;


    @Inject
    private JMSContext context;

    @Resource(lookup = "java:/jms/queue/EMAILSENDINGQueue")
    private Queue queue;

    @Override
    public  Email sendMail(Email email) throws EmailException {
        try {
            //ObjectMessage objectMessage =  context.createObjectMessage();
            //objectMessage.setObject(email);
            //final Destination destination  = (Destination) queue;
            context.createProducer().send((Destination) queue

                                       , context.createObjectMessage(email));
           // context.createProducer().send(destination, objectMessage);
        } catch (ClassCastException e) {
            throw new SendingEmailException("Error on sending " +
                    "email to jms queue : " +
                    email.toString(),e);
        }
        email.setSendDate(new Date());
        return email;
    }
}
