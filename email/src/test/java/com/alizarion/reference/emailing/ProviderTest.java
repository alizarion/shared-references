package com.alizarion.reference.emailing;

import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.provider.EmailProvider;

/**
 * @author Selim Bensenouci.
 */
public class ProviderTest implements EmailProvider {


    @Override
    public Email sendMail(Email email) {
        return null;
    }
}
