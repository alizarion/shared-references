package com.alizarion.reference.emailing.tools;

import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.exception.EmailRenderingException;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Simple email tool helper.
 * @author selim@openlinux.fr.
 */
public class EmailHelper {

    /**
     * Simple method used get needed part of the mail template for rendering.
     * @param email that we want to render.
     * @param part part of the mail to render :  subject, bodyHTML,bodyText.
     * @return  Template to render part of the mail.
     * @throws EmailRenderingException if cannot access to email part files.
     * @see org.antlr.stringtemplate.StringTemplate
     */
    public static ST getStringTemplate(Email email,String part)
            throws EmailRenderingException {
        String path =  email.getTemplateRoot().toString()+
                File.separator + email.getType() +
                File.separator + part +
                File.separator + "_"+
                email.getLocale().getLanguage() + ".stg";
        try {

            File subjectFile = new File(new URI(path));
            STGroupFile stFile = new STGroupFile(
                    subjectFile.getAbsolutePath(),'$','$');
            return stFile.getInstanceOf(part);
        } catch (URISyntaxException e) {
            throw new EmailRenderingException("cannot" +
                    " access file :" +path ,e );
        }

    }

}
