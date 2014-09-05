package com.alizarion.reference.location.entities;

import com.alizarion.reference.location.utils.Helper;
import org.apache.commons.lang3.LocaleUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * @author selim@openlinux.fr.
 */

@Entity
@Table( name = "location_country")
public class Country implements Serializable {

    @Id
    @Column(name = "country_id",length = 2)
    private String id;

    public Country() {
    }

    public Country(final String id) {
        this.id = id;
    }

    public Currency getCurrency(){
        return Currency.getInstance(new Locale("",this.id)) ;
    }

    public List<Locale> getAvailableLang(){
        return LocaleUtils.languagesByCountry(this.id);
    }

    public String getId() {
        return id;
    }



    public String getCallingCountryCode(){
        return Helper.getPhoneCallingCodeByCountry(this);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Country)) return false;

        Country country = (Country) o;

        if (id != null ? !id.equals(country.id) :
                country.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
