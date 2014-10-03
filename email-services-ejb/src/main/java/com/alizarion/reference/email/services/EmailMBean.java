package com.alizarion.reference.email.services;

import com.alizarion.reference.exception.ApplicationError;
import com.alizarion.reference.resource.exception.PersistentResourceNotFoundException;
import com.alizarion.reference.resource.mbean.PersistentMBean;

import javax.ejb.Stateless;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Email params.
 * @author Selim Bensenouci.
 */
@Stateless
public class EmailMBean extends PersistentMBean {

    public final static String CATEGORY =
            "com.alizarion.reference.emailing.properties";

    /**
     * Max mail per hour value.
     */
    public final static String MAX_PER_HOUR =
            "max-per-hour";

    /**
     * Max mail per hour value.
     */
    public final static String MAIL_TEMPLATES_ROOT_FOLDER =
            "mail-templates-root-folder";


    /**
     * application mail sent from
     */
    public final static String SENT_MAIL_FROM =
            "mail-from";


    private static final long serialVersionUID = -8209362004394531823L;

    @Override
    public String getCategory() {
        return CATEGORY;
    }

    public Integer getMaxPerHour()
            throws PersistentResourceNotFoundException {
        try {
            return Integer.parseInt(getValue(MAX_PER_HOUR));
        } catch (NumberFormatException e){
            throw  new ApplicationError("bad number" +
                    " format for "+
                    MAX_PER_HOUR +
                    " property ",e);
        }

    }

    public URI getMailTemplatesRootFolder() throws PersistentResourceNotFoundException {
        try {
            return getValueAsURI(MAIL_TEMPLATES_ROOT_FOLDER);
        } catch (URISyntaxException e) {
            throw new ApplicationError("mal formed URI for " +
                    MAIL_TEMPLATES_ROOT_FOLDER +
                    "property " );
        }

    }

    public InternetAddress getMailFrom() throws
            PersistentResourceNotFoundException,
            AddressException {
        return new InternetAddress(getValue(SENT_MAIL_FROM));
    }

}
