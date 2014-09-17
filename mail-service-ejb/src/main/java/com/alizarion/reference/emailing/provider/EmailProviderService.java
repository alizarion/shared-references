package com.alizarion.reference.emailing.provider;

import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.exception.EmailException;
import com.alizarion.reference.emailing.exception.SendingEmailException;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author selim@openlinux.fr.
 */
@Stateless
public class EmailProviderService implements EmailProvider {

    /**
     * Resource for sending the email. The mail subsystem
     * is defined in either standalone.xml
     * or domain.xml in your respective
     * configuration directory.
     */
    @Resource(mappedName = "java:jboss/mail/Default")
    private Session mailSession;

    private ResourceBundle resourceBundle = ResourceBundle.getBundle("email");

    @Override
    public Email sendMail(Email email) throws EmailException {
        Message message =  email.getMessage(
                mailSession,Integer.
                        parseInt(resourceBundle.
                                getString("mail.smtp.max.attachment.size")));
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            throw new SendingEmailException("Unexpected error " +
                    "on sending email  "+ email.toString(),e);
        }
        email.setSendDate(new Date());
        return email;
    }
}
