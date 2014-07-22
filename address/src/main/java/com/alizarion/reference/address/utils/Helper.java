package com.alizarion.reference.address.utils;

import com.alizarion.reference.address.entities.Country;

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
