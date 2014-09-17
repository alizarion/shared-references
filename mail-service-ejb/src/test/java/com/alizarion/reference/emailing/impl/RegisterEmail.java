package com.alizarion.reference.emailing.impl;

import com.alizarion.reference.emailing.SimpleTestUser;
import com.alizarion.reference.emailing.entities.Email;
import com.alizarion.reference.emailing.entities.EmailAbstractBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(value = RegisterEmail.TYPE)
public class RegisterEmail extends Email {


    private static final long serialVersionUID = -4901018233807963289L;

    public final static String TYPE = "register-email";

    @Transient
    private List<File> attachments = new ArrayList<>();

    @Transient
    private  SimpleTestUser user;

    public RegisterEmail() {
        super();

    }
    public RegisterEmail(RegisterEmailBuilder builder) {
        this.user = builder.user;
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

    public SimpleTestUser getUser() {
        return user;
    }



    public static class RegisterEmailBuilder extends EmailAbstractBuilder {

        private SimpleTestUser user;

        public RegisterEmailBuilder(SimpleTestUser user) {
            this.user = user;
        }



        @Override
        public Email builder() {
            return new RegisterEmail(this);
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                "RegisterEmail{" +
                "user=" + user +
                '}' + +  '\'' +
                '}';
    }
}
