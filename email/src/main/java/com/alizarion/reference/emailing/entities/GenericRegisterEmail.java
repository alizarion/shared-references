package com.alizarion.reference.emailing.entities;


import com.alizarion.reference.emailing.exception.EmailRenderingException;
import com.alizarion.reference.emailing.tools.EmailHelper;
import com.alizarion.reference.person.entities.ValidateEmailToken;
import org.stringtemplate.v4.ST;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.File;
import java.net.URI;
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
    private  ValidateEmailToken emailToken;

    public GenericRegisterEmail(RegisterEmailBuilder builder) {
        super(builder.getFrom(),
                builder.getTo(),
                builder.getTemplateRoot(),
                builder.getLocale());
        this.emailToken = builder.getToken();
        super.setCc(builder.getCc());
        super.setCci(builder.getCci());
    }


    @Override
    public String getSubject() throws EmailRenderingException {
        ST st = EmailHelper.getStringTemplate(this,MAIL_SUBJECT_TEMPLATE);
        st.add("emailToken",this.emailToken);
        return st.render();
    }

    @Override
    public String getTextBody() throws EmailRenderingException {
        ST st = EmailHelper.getStringTemplate(this,MAIL_TEXT_BODY_TEMPLATE);
        st.add("emailToken",this.emailToken);
        return st.render();
    }

    @Override
    public String getHTMLBody() throws EmailRenderingException {
        ST st = EmailHelper.getStringTemplate(this,MAIL_HTML_BODY_TEMPLATE);
        st.add("emailToken",this.emailToken);
        return st.render();
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

        public RegisterEmailBuilder(final String from,
                                    final ValidateEmailToken token,
                                    final URI templateRoot,
                                    final Locale locale
        ) {
            super(from,token.getElectronicAddress().getEmailAddress(),templateRoot,locale);
            this.token = token;
        }

        public ValidateEmailToken getToken() {
            return token;
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
