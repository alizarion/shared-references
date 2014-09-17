package com.alizarion.reference.emailing.provider;


import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.exception.EmailConfigException;
import com.alizarion.reference.emailing.exception.EmailException;
import com.alizarion.reference.emailing.exception.SendingEmailException;
import org.apache.commons.lang3.StringUtils;

import javax.mail.*;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author selim@openlinux.fr.
 */
public class SimpleJavaMailProvider implements EmailProvider {


    ResourceBundle resourceBundle =  ResourceBundle.getBundle("email");

    @Override
    public  Email sendMail(Email email) throws EmailException {
        try {
            Session session = getMailSession();
            Message message =  email.getMessage(
                    session,Integer.
                            parseInt(resourceBundle.
                                    getString("mail.smtp.max.attachment.size")));
            Transport.send(message);
        } catch (MessagingException  e) {
            throw new SendingEmailException("failed to send email : "
                    + email.toString(),e);
        } finally {
            email.setSendDate(new Date());
        }
        return email;
    }

    /**
     * Method to get mail session from properties file.
     * @return  mail session
     */
    public Session getMailSession() throws EmailConfigException {
        Properties properties = new Properties();

        if(!StringUtils.isEmpty(resourceBundle.getString("mail.smtp.host"))){
            properties.setProperty("mail.smtp.host",
                    resourceBundle.getString("mail.smtp.host"));
        } else {
            throw new EmailConfigException("cannot send email " +
                    "without mail.smtp.host. property ");
        }


        if(!StringUtils.isEmpty(resourceBundle.getString("mail.smtp.port"))){
            properties.setProperty("mail.smtp.port",
                    resourceBundle.getString("mail.smtp.port"));
        }

        if(!StringUtils.isEmpty(resourceBundle.getString("mail.smtp.starttls.enable"))){
            properties.setProperty("mail.smtp.starttls.enable",
                    resourceBundle.getString("mail.smtp.starttls.enable"));
        }

        if(!StringUtils.isEmpty(resourceBundle.
                getString("mail.smtp.auth"))){
            properties.setProperty("mail.smtp.auth",
                    resourceBundle.getString("mail.smtp.auth"));
            if(Boolean.parseBoolean(resourceBundle.
                    getString("mail.smtp.auth"))) {
                if (!StringUtils.isEmpty(resourceBundle.getString("mail.smtp.user")) &&
                        !StringUtils.isEmpty(resourceBundle.getString("mail.smtp.password"))) {
                    return Session.getDefaultInstance(properties,
                            new javax.mail.Authenticator() {
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(
                                            resourceBundle.getString("mail.smtp.user"),
                                            resourceBundle.getString("mail.smtp.password"));
                                }
                            });

                } else {
                    throw new EmailConfigException("cannot send email," +
                            " auth is required but no" +
                            " credential in properties");
                }
            }
        }

        return Session.getDefaultInstance(properties);
    }
}
