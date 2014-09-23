package com.alizarion.reference.emailing.helper;

import com.alizarion.reference.location.entities.ElectronicAddress;
import com.alizarion.reference.person.entities.PhysicalPerson;
import com.alizarion.reference.person.entities.Title;
import com.alizarion.reference.person.entities.ValidateEmailToken;

/**
 * @author selim@openlinux.fr.
 */
public  class EmailTestHelper {

    public static ValidateEmailToken getValidateEmailToken(){
        //TODO refaire le test avec les bonnes entités
        PhysicalPerson person = new PhysicalPerson();
        person.setFirstName("Bensenouci");
        person.setLastName("Selim");
        person.setTitle(Title.MR);
        ElectronicAddress electronicAddress =  new ElectronicAddress("selim@openlinux.fr");
        ValidateEmailToken validateEmailToken = new ValidateEmailToken(electronicAddress,person);
         return validateEmailToken;
        /*return new GenericRegisterEmail.RegisterEmailBuilder("totoro",
                new ValidateEmailToken(new ElectronicAddress(),new URI(),
                        new PhysicalPerson()),Locale.getDefault()).
                setCc("selim.bensenouci@gmail.com").
                setFrom("selim.bensenouci@gmail.com").
                builder(); */
    }
}
