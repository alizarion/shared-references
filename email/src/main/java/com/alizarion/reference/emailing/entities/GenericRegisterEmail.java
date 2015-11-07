package com.alizarion.reference.emailing.entities;


import com.alizarion.reference.security.entities.ValidateEmailToken;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.File;
import java.net.URI;
import java.util.*;

/**
 * @author Selim Bensenouci.
 */
@Entity
@DiscriminatorValue(value = GenericRegisterEmail.TYPE)
public class GenericRegisterEmail extends Email {

    private static final long serialVersionUID = -4901018233807963289L;

    public final static String TYPE = "register-email";

    @Transient
    private List<File> attachments = new ArrayList<>();

    @Transient
    private  ValidateEmailToken emailToken;

    public GenericRegisterEmail() {
    }

    public GenericRegisterEmail(RegisterEmailBuilder builder) {
        super(builder.getFrom(),
                builder.getTo(),
                builder.getTemplateRoot(),
                builder.getLocale());
        this.emailToken = builder.getToken();
        super.setCc(builder.getCc());
        super.setCci(builder.getCci());
        Map<String,Object> subject = new HashMap<>();
        Map<String,Object> bodyHTML = new HashMap<>();
        Map<String,Object> bodyText = new HashMap<>();
        subject.put("emailToken",this.emailToken);
        bodyHTML.put("emailToken",this.emailToken);
        bodyHTML.put("username",builder.getUsername());

        bodyText.put("emailToken",this.emailToken);
        bodyText.put("username",builder.getUsername());

        getParams().put(MAIL_SUBJECT_TEMPLATE,subject);
        getParams().put(MAIL_HTML_BODY_TEMPLATE,bodyHTML);
        getParams().put(MAIL_TEXT_BODY_TEMPLATE,bodyText);


    }





    @Override
    public List<File> getAttachments() {
        return this.attachments;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public void addAttachment(File file){
        this.attachments.add(file);

    }

    public ValidateEmailToken getEmailToken() {
        return emailToken;
    }

    public void setEmailToken(ValidateEmailToken emailToken) {
        this.emailToken = emailToken;
    }

    public static class RegisterEmailBuilder extends EmailAbstractBuilder {


        private ValidateEmailToken token;

        private String username;


        public RegisterEmailBuilder(final String from,
                                    final ValidateEmailToken token,
                                    final URI templateRoot,
                                    final Locale locale
        ) {
            super(from,token.getElectronicAddress().getEmailAddress(),templateRoot,locale);
            this.token = token;
            this.username=null;
        }

        public ValidateEmailToken getToken() {
            return token;
        }

        public RegisterEmailBuilder setUsername(String username){
            this.username = username;
            return this;
        }

        public String getUsername() {
            return username;
        }

        @Override
        public Email builder() {
            return new GenericRegisterEmail(this);
        }
    }

    @Override
    public String toString() {
        return "GenericRegisterEmail{" +
                "attachments=" + attachments +
                ", emailToken=" + emailToken +
                '}';
    }
}
