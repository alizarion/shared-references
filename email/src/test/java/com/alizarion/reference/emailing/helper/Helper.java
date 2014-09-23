package com.alizarion.reference.emailing.helper;

import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.entities.GenericRegisterEmail;
import com.alizarion.reference.location.entities.ElectronicAddress;
import com.alizarion.reference.person.entities.PhysicalPerson;
import com.alizarion.reference.person.entities.ValidateEmailToken;

import java.util.Locale;

/**
 * @author selim@openlinux.fr.
 */
public  class Helper {

    public static Email getRegisterEmail(){
        //TODO refaire le test avec les bonnes entités
        return new GenericRegisterEmail.RegisterEmailBuilder("totoro",
                new ValidateEmailToken(new ElectronicAddress(),
                        new PhysicalPerson()),Locale.getDefault()).
                setCc("selim.bensenouci@gmail.com").
                setFrom("selim.bensenouci@gmail.com").
                builder();
    }
}
