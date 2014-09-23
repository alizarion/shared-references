package com.alizarion.reference.emailing.entities;

import java.util.Locale;

/**
 * Builder class that can be used to help building <br/>
 * emails objects that can contains many properties.
 * @author selim@openlinux.fr.
 * @see com.alizarion.reference.emailing.entities.Email
 */
public abstract class EmailAbstractBuilder {

    private String from;

    private String to;

    private String cc;

    private String cci;

    private String type;

    private Locale locale;

    protected EmailAbstractBuilder(final String from,
                                   final String to,
                                   final Locale locale) {
        this.from = from;
        this.to = to;
        this.locale = locale;
    }

    public EmailAbstractBuilder setFrom(String from) {
        this.from = from;
        return this;
    }

    public EmailAbstractBuilder setCc(String cc) {
        this.cc = cc;
        return this;
    }

    public EmailAbstractBuilder setCci(String cci) {
        this.cci = cci;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getCc() {
        return cc;
    }

    public String getCci() {
        return cci;
    }

    public String getType() {
        return type;
    }

    public Locale getLocale() {
        return locale;
    }

    public abstract Email builder();
}
