package com.alizarion.reference.location.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Locale;

/**
 * @author selim@openlinux.fr.
 */
@Entity
@Table(name = "location_language")
@NamedQueries({@NamedQuery(name = Language.FIND_ALL_MANAGED,
        query = "select lang from Language lang where " +
                "lang.managed = true order by lang.langCode desc")})
public class Language implements Serializable {

    private static final long serialVersionUID = -1283144562330733745L;

    public static final String FIND_ALL_MANAGED =  "Language.FIND_ALL_MANAGED";


    @Column(name = "managed")
    private Boolean managed;

    @Id
    @Column(name = "lang_id")
    private String langCode;



    public Language() {
        this.managed = false;
    }

    public Language(Locale locale) {
        this.managed = false;
        if (locale!=null) {
            this.langCode = locale.getLanguage().toUpperCase();
        }
        //this.managed = false;
    }

    public Language(String language) {
        this.langCode = language.toUpperCase();
        // this.managed = false;
    }

    public Boolean getManaged() {
        if(this.managed == null){
          return false;
        }  else {
            return managed;

        }
    }

    public void setManaged(Boolean managed) {
        this.managed = managed;
    }

    public String getLangCode() {
        return langCode;
    }

    public Locale getLocale(){

        return Locale.forLanguageTag(this.langCode);
    }

    public void setLangCode(String langCode) {
        if(langCode!=null){
            langCode = langCode.toUpperCase();

        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Language)) return false;

        Language language = (Language) o;

        if (langCode != null ? !langCode.equals(language.langCode) : language.langCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return langCode != null ? langCode.hashCode() : 0;
    }
}
