package com.alizarion.reference.emailing.helper;

import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.entities.GenericRegisterEmail;
import com.alizarion.reference.emailing.entities.GenericResetPasswordEmail;
import com.alizarion.reference.emailing.exception.EmailRenderingException;
import com.alizarion.reference.emailing.tools.EmailHelper;
import com.alizarion.reference.location.entities.ElectronicAddress;
import com.alizarion.reference.person.entities.PhysicalPerson;
import com.alizarion.reference.person.entities.Title;
import com.alizarion.reference.person.entities.ValidateEmailToken;
import com.alizarion.reference.security.entities.Credential;
import com.alizarion.reference.security.entities.ResetPasswordToken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Locale;
import java.util.Map;

/**
 * @author selim@openlinux.fr.
 */
public  class EmailTestHelper {

    public static PhysicalPerson getPhysicalPersonWithAddress(){
        return (PhysicalPerson) getValidateEmailToken().
                updatePersonWithAddress();
    }

    public static ValidateEmailToken getValidateEmailToken(){
        //TODO refaire le test avec les bonnes entités
        PhysicalPerson person = new PhysicalPerson();
        person.setFirstName("Bensenouci");
        person.setLastName("Selim");
        person.setTitle(Title.MR);
        ElectronicAddress electronicAddress =
                new ElectronicAddress("selim@openlinux.fr");
        ValidateEmailToken validateEmailToken =
                new ValidateEmailToken(electronicAddress,person);
        return validateEmailToken;

    }

    public static GenericRegisterEmail getGenericRegisterEmail(URI uri){
        return (GenericRegisterEmail) new
                GenericRegisterEmail.RegisterEmailBuilder(
                "selim@openlinux.fr",getValidateEmailToken(),
                uri, Locale.getDefault()).builder();
    }

    public static GenericResetPasswordEmail getGenericResetPasswordEmail(URI uri){
        return  (GenericResetPasswordEmail) new  GenericResetPasswordEmail.
                GenericResetPasswordEmailBuilder("selim@openlinux.fr",
                getPhysicalPersonWithAddress(),
                uri,Locale.FRENCH,
                new ResetPasswordToken(new Credential())).builder();
    }



    public static void createTemplateFiles(Email email) throws EmailRenderingException, IOException {
        File subject = new File(EmailHelper.
                getStringTemplatePartPath(email, Email.MAIL_SUBJECT_TEMPLATE));
        subject.getParentFile().mkdir();
        //temporaryFolder.newFolder();
        Map<String,Object> mapsubject =  email.getParams().get(Email.MAIL_SUBJECT_TEMPLATE);

        String subjectArgs = null;
        String subjectContent = "";
        for (Map.Entry<String,Object> entry: mapsubject.entrySet()){
            subjectContent = subjectContent +" " +"$"+entry.getKey()+"$" +" ";
            if (subjectArgs==null){
                subjectArgs =  entry.getKey();
            } else {
                subjectArgs = subjectArgs + "," + entry.getKey();
            }
        }



        BufferedWriter out1 = new BufferedWriter(new FileWriter(subject));
        out1.write("subject("+subjectArgs+") ::= \"hello "+subjectContent+"\"");
        out1.close();

        Map<String,Object> maphtml =  email.getParams().get(Email.MAIL_HTML_BODY_TEMPLATE);

        String htmlArgs = null;
        String htmlContent ="";
        for (Map.Entry<String,Object> entry: maphtml.entrySet()){
            htmlContent = htmlContent +" " +"$"+entry.getKey()+"$" +" ";

            if (htmlArgs==null){
                htmlArgs =  entry.getKey();
            } else {
                htmlArgs = htmlArgs + "," + entry.getKey();
            }
        }

        File bodyHTML = new File(EmailHelper.
                getStringTemplatePartPath(email, Email.MAIL_HTML_BODY_TEMPLATE));

        BufferedWriter out2 = new BufferedWriter(new FileWriter(bodyHTML));
        out2.write("bodyHTML("+htmlArgs+") ::= \" bodyHTML "+htmlContent+"  \"");
        out2.close();

        Map<String,Object> maptext =  email.getParams().get(Email.MAIL_TEXT_BODY_TEMPLATE);

        String textArgs =null;
        String textContent = "";
        for (Map.Entry<String,Object> entry: maptext.entrySet()){
            textContent = textContent +" " +"$"+entry.getKey()+"$" +" ";
            if (textArgs==null){

                textArgs =  entry.getKey();
            } else {
                textArgs = textArgs + "," + entry.getKey();
            }
        }

        File bodyText = new File(EmailHelper.
                getStringTemplatePartPath(email, Email.MAIL_TEXT_BODY_TEMPLATE));
        BufferedWriter out3 = new BufferedWriter(new FileWriter(bodyText));
        out3.write("bodyText("+textArgs+") ::= \" bodyText "+textContent+"  \"");

        out3.close();
    }
}
