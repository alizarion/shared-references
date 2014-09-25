package com.alizarion.reference.emailing.entities;

import com.alizarion.reference.emailing.exception.EmailAttachmentSizeExceedException;
import com.alizarion.reference.emailing.exception.EmailException;
import com.alizarion.reference.emailing.exception.EmailRenderingException;
import com.alizarion.reference.emailing.exception.PreparingEmailMessageException;
import com.alizarion.reference.emailing.tools.EmailHelper;
import org.stringtemplate.v4.ST;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.*;

/**
 * class representing the email app,
 * only sent mail are persisted database,<br/>
 * which allows for a mastery of the
 * number of dispatch by time
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "email_email_log")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "mail_type")
public abstract class Email implements Serializable {

    private static final long serialVersionUID = 6097443812365423254L;

    @Id
    @TableGenerator(name = "mail_logger_SEQ",
            pkColumnName = "SEQ_NAME",
            pkColumnValue = "SEQ_VALUE",
            table = "sequence")
    @GeneratedValue(strategy = GenerationType.TABLE,
            generator = "mail_logger_SEQ")
    private Long id;


    @Column(name = "creation_date",nullable = false)
    private Date creationDate;

    @Column(name = "send_date")
    private Date sendDate;

    @Column(name = "mail_from",nullable = false)
    private String from;

    @Column(name = "mail_to", length = 2048,nullable = false)
    private String to;

    @Column(name = "mail_cc", length = 2048)
    private String cc;

    @Column(name = "mail_cci", length = 2048)
    private String cci;

    @Column(name = "mail_type",updatable = false,insertable = false)
    private String type;

    @Column(name = "mail_locale")
    private Locale locale;

    @Transient
    private URI templateRoot;

    @Transient
    public  Map<String,Map<String,Object>> params = new HashMap<>();


    /**
     * Email default constructor with required fields.
     * @param from email of the sender.
     * @param to electronic address of the destination.
     * @param templateRoot root folder that contain email template files.
     * @param locale language that will be used to render the mail.
     */
    public Email(String from,
                 String to,
                 URI templateRoot,
                 Locale locale) {
        this.templateRoot = templateRoot;
        this.creationDate = new Date();
        this.locale =locale;
        this.from = from;
        this.to = to;

    }

    public final static String MAIL_SUBJECT_TEMPLATE = "subject";

    public final static String MAIL_TEXT_BODY_TEMPLATE = "bodyText";

    public final static String MAIL_HTML_BODY_TEMPLATE = "bodyHTML";


    public abstract  Map<String, Map<String, Object>> getParams();

    /**
     * Method to get the email rendered subject.
     * @return email subject as String.
     * @throws EmailRenderingException oups problems.
     */
    public  String getSubject() throws EmailRenderingException{
        ST st = EmailHelper.getStringTemplate(this, MAIL_SUBJECT_TEMPLATE);
        for(Map.Entry entry :  getParams().get(MAIL_SUBJECT_TEMPLATE).entrySet()) {
            st.add((String)entry.getKey(), entry.getValue());
        }
        return st.render();

    }

    /**
     * Method to get the rendered body text part of the mail.
     * @return email body text string.
     * @throws EmailRenderingException  on problems.
     */
    public  String getTextBody() throws EmailRenderingException{
        ST st = EmailHelper.getStringTemplate(this, MAIL_TEXT_BODY_TEMPLATE);
        for(Map.Entry entry :  getParams().get(MAIL_TEXT_BODY_TEMPLATE).entrySet()) {
            st.add((String)entry.getKey(), entry.getValue());
        }
        return st.render();
    }


    /**
     * Method to get the rendered body html part of the mail.
     * @return email body html string.
     * @throws EmailRenderingException on problems.
     */
    public  String getHTMLBody() throws EmailRenderingException{
        ST st = EmailHelper.getStringTemplate(this, MAIL_HTML_BODY_TEMPLATE);
        for(Map.Entry entry :  getParams().get(MAIL_HTML_BODY_TEMPLATE).entrySet()) {
            st.add((String)entry.getKey(), entry.getValue());
        }
        return st.render();
    }

    public abstract List<File> getAttachments();

    public Long getId() {
        return id;
    }

    public URI getTemplateRoot() {
        return templateRoot;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getFrom() {
        return from;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getCci() {
        return cci;
    }

    public void setCci(String cci) {
        this.cci = cci;
    }

    public abstract String getType() ;

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Method to get ready to send message.
     * @param session mail session used to get message.
     * @param attachmentMaxSize max size of the mail attachment.
     * @return message that will be sent.
     * @throws EmailException on problems.
     */
    public MimeMessage getMessage(Session session,
                                  final Integer attachmentMaxSize)
            throws EmailException {

        MimeMessage message = new MimeMessage(session);

        try {
            message.setSubject(getSubject());
            message.setText(getTextBody());

            /* adding multipart message that
            contain html text and attachment parts*/
            final Multipart mp = new MimeMultipart();

             /* html part */
            final MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(getHTMLBody(), "text/html");
            mp.addBodyPart(htmlPart);

            long size = 0;
            for (File file : getAttachments()){
                size +=file.length();
                if (size/1048576 > attachmentMaxSize){
                    throw new EmailAttachmentSizeExceedException(
                            "email attachment size" +
                                    " exceed the allowed limit");
                }
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(file);
                mp.addBodyPart(attachmentPart);
            }

            message.setContent(mp);
            message.setFrom(getFrom());
            message.addRecipients(Message.RecipientType.TO, getTo());
            if (getCc() != null) {
                message.addRecipients(Message.RecipientType.CC, getCc());
            }
            if (getCci() != null) {
                message.addRecipients(Message.RecipientType.BCC, getCci());
            }
        } catch (MessagingException | IOException e) {
            throw new PreparingEmailMessageException(
                    "Error occur on " +
                            "preparing email message : " +
                            this.toString() , e) ;
        }
        return message;

    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Email)) return false;

        Email email = (Email) o;

        return !(cc != null ? !cc.equals(email.cc) :
                email.cc != null) &&
                !(cci != null ? !cci.equals(email.cci) :
                        email.cci != null) &&
                !(from != null ? !from.equals(email.from) :
                        email.from != null) &&
                !(id != null ? !id.equals(email.id) :
                        email.id != null) &&
                !(creationDate != null ?
                        !creationDate.equals(email.creationDate) :
                        email.creationDate != null) &&
                !(to != null ? !to.equals(email.to) :
                        email.to != null) &&
                !(type != null ? !type.equals(email.type) :
                        email.type != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (creationDate != null ?
                creationDate.hashCode() : 0);
        result = 31 * result + (from != null ?
                from.hashCode() : 0);
        result = 31 * result + (to != null ?
                to.hashCode() : 0);
        result = 31 * result + (cc != null ?
                cc.hashCode() : 0);
        result = 31 * result + (cci != null ?
                cci.hashCode() : 0);
        result = 31 * result + (type != null ?
                type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Email{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", sendDate=" + sendDate +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", cc='" + cc + '\'' +
                ", cci='" + cci + '\'' +
                ", type='" + type + '\'' +
                ", locale=" + locale +  '\''
                ;
    }
}
