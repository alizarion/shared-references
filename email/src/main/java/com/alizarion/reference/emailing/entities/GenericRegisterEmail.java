package com.alizarion.reference.emailing.entities;


import com.alizarion.reference.person.entities.ValidateEmailToken;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(value = GenericRegisterEmail.TYPE)
public class GenericRegisterEmail extends Email {

    private static final long serialVersionUID = -4901018233807963289L;

    public final static String TYPE = "register-email";

    @Transient
    private List<File> attachments = new ArrayList<>();

    @Transient
    private  ValidateEmailToken activationToken;

    public GenericRegisterEmail() {
        super();

    }
    public GenericRegisterEmail(RegisterEmailBuilder builder) {
        this.activationToken = builder.getToken();
        super.setTo(builder.getTo());
        super.setCc(builder.getCc());
        super.setCci(builder.getCci());
        super.setFrom(builder.getFrom());
        super.setLocale(builder.getLocale());
    }

    @Override
    public String getSubject() {
        return "my simple mail subject";
    }

    @Override
    public String getTextBody() {
        return "my simple text mail body";
    }

    @Override
    public String getHTMLBody() {
        return "my <p>html</p> mail <h1>body</h1>";
    }

    @Override
    public List<File> getAttachments() {
        return this.attachments;
    }

    public void addAttachment(File file){
        this.attachments.add(file);

    }

    public ValidateEmailToken getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(ValidateEmailToken activationToken) {
        this.activationToken = activationToken;
    }

    public static class RegisterEmailBuilder extends EmailAbstractBuilder {


        private ValidateEmailToken token;

        public RegisterEmailBuilder(final String from,
                                    final ValidateEmailToken token,
                                    final Locale locale
                                    ) {
            super(from,token.getElectronicAddress().getEmailAddress(),locale);
            this.token = token;
        }

        public ValidateEmailToken getToken() {
            return token;
        }

        public void setToken(ValidateEmailToken token) {
            this.token = token;
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
                ", activationToken=" + activationToken +
                '}';
    }
}
