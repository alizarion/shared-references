package com.alizarion.reference.location.utils;

import com.alizarion.reference.location.entities.Country;

import java.util.ResourceBundle;

/**
 *
 * @author selim@openlinux.fr.
 */
public class Helper {

    public static final String PHONE_PREFIX_BUNDLE = "phone_prefix";

    public static String getPhoneCallingCodeByCountry(Country country){
        return ResourceBundle.getBundle(
                PHONE_PREFIX_BUNDLE).getString(
                country.getId());
    }

}
