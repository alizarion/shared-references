package com.alizarion.reference.emailing.tools;

import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.exception.EmailRenderingException;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.net.URI;

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
    public static ST getStringTemplate(Email email,String part){
        STGroupFile stFile = new STGroupFile(
                getStringTemplatePartPath(email, part).getPath(),'$','$');
        return stFile.getInstanceOf(part);
    }

    public static URI getStringTemplatePartPath(Email email,String part){
        /*return new File(email.getTemplateRoot().toString()+
                File.separator + email.getType() +
                File.separator + part + "_" +
                email.getLocale().getLanguage().toUpperCase() + ".stg").toURI();*/
        return new File(email.getTemplateRoot().toString()+
                       File.separator + email.getType() +
                       File.separator +
                       email.getLocale().getLanguage().toUpperCase() + ".stg").toURI();
    }

}
