package com.alizarion.reference.emailing.provider;

import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.exception.EmailException;

/**
 * @author selim@openlinux.fr
 */
public interface EmailProvider  {

    /**
     * Method that implement sending email
     * @param email to send
     * @return  mail with sending horodating
     */
    public Email sendMail(Email email) throws EmailException;

}
