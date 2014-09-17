package com.alizarion.reference.emailing.entities;

import java.util.Locale;

/**
 * @author selim@openlinux.fr.
 */
public abstract class EmailAbstractBuilder {



    private String from;

    private String to;

    private String cc;

    private String cci;

    private String type;

    private Locale locale;

    protected EmailAbstractBuilder() {
    }

    public EmailAbstractBuilder setFrom(String from) {
        this.from = from;
        return this;
    }

    public EmailAbstractBuilder setTo(String to) {
        this.to = to;
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

    public EmailAbstractBuilder setLocale(Locale locale) {
        this.locale = locale;
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
