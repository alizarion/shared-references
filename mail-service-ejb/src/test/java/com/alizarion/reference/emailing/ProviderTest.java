package com.alizarion.reference.emailing;

import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.provider.EmailProvider;

/**
 * @author selim@openlinux.fr.
 */
public class ProviderTest implements EmailProvider {


    @Override
    public Email sendMail(Email email) {
        return null;
    }
}
