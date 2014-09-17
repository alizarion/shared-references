package com.alizarion.reference.emailing.helper;

import com.alizarion.reference.emailing.SimpleTestUser;
import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.impl.RegisterEmail;

import java.util.Locale;

/**
 * @author selim@openlinux.fr.
 */
public  class Helper {

    public static Email getRegisterEmail(){
        return new RegisterEmail.RegisterEmailBuilder(
                new SimpleTestUser("toto", "titi", "no-reply@openlinux.fr")).
                setCc("selim.bensenouci@gmail.com").
                setFrom("selim.bensenouci@gmail.com").
                setLocale(Locale.getDefault()).
                setTo("selim@openlinux.fr").
                builder();
    }
}
