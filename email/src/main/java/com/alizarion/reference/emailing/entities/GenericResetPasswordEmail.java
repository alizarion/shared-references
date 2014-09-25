package com.alizarion.reference.emailing.entities;

import com.alizarion.reference.person.entities.PhysicalPerson;
import com.alizarion.reference.security.entities.ResetPasswordToken;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Generic credentials reset password email
 * @author selim@openlinux.fr.
 */
@Entity
@DiscriminatorValue(value = GenericResetPasswordEmail.TYPE)
public class GenericResetPasswordEmail extends Email {

    public static final String TYPE =  "reset-password";

    @Transient
    private ResetPasswordToken passwordToken;

    @Transient
    private PhysicalPerson person;

    @Transient
    public  Map<String,Map<String,Object>> params = new HashMap<>();


    public GenericResetPasswordEmail() {
    }

    public GenericResetPasswordEmail(
            GenericResetPasswordEmailBuilder
                    builder) {
        super(builder.getFrom(), builder.getTo(),
                builder.getTemplateRoot(),
                builder.getLocale());

        this.person = builder.getPhysicalPerson();
        this.passwordToken = builder.getPasswordToken();

        Map<String,Object> subject = new HashMap<>();
        Map<String,Object> bodyHTML = new HashMap<>();
        Map<String,Object> bodyText = new HashMap<>();

        subject.put("person",builder.getPhysicalPerson());
        bodyHTML.put("passwordToken",builder.getPasswordToken());
        bodyHTML.put("person",builder.getPhysicalPerson());
        bodyText.put("passwordToken",builder.getPasswordToken());
        bodyText.put("person",builder.getPhysicalPerson());

        this.params.put(MAIL_SUBJECT_TEMPLATE,subject);
        this.params.put(MAIL_HTML_BODY_TEMPLATE,bodyHTML);
        this.params.put(MAIL_TEXT_BODY_TEMPLATE,bodyText);
    }

    public Map<String, Map<String, Object>> getParams() {
        return params;
    }

    public void setParams(Map<String, Map<String, Object>> params) {
        this.params = params;
    }



    @Override
    public List<File> getAttachments() {
        return null;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public static class GenericResetPasswordEmailBuilder extends EmailAbstractBuilder{

        private ResetPasswordToken passwordToken;

        private PhysicalPerson physicalPerson;

        public GenericResetPasswordEmailBuilder(String from,
                                                PhysicalPerson to,
                                                URI templateRoot,
                                                Locale locale,
                                                ResetPasswordToken token) {
            super(from,
                    to.getPrimaryElectronicAddress().
                            getEmailAddress(),
                    templateRoot,
                    locale);
            this.physicalPerson = to;
            this.passwordToken = token;
        }

        public GenericResetPasswordEmailBuilder
        setPasswordToken(ResetPasswordToken passwordToken) {
            this.passwordToken = passwordToken;
            return this;
        }

        public GenericResetPasswordEmailBuilder
        setPhysicalPerson(PhysicalPerson physicalPerson) {
            this.physicalPerson = physicalPerson;
            return this;

        }

        public ResetPasswordToken getPasswordToken() {
            return passwordToken;
        }

        public PhysicalPerson getPhysicalPerson() {
            return physicalPerson;
        }

        @Override
        public Email builder() {
            return new GenericResetPasswordEmail(this);
        }
    }
}
