package com.alizarion.reference.location.entities;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CountryTest {

    private Country country;

    @Before
    public void init(){
        country = new Country("FR");
    }

    @Test
    public void testGetCurrency() throws Exception {
        Currency  currency=  country.getCurrency();
        assertNotNull(currency);
        assertEquals("€", currency.getSymbol());
    }

    @Test
    public void testGetAvailableLang() throws Exception {
        Locale locale = new Locale("fr","FR");
        List<Locale> locales = new ArrayList<>();
        locales.add(locale);
        assertThat(locales,is(country.getAvailableLang()));
    }

    @Test
    public void getPhoneCallingCountryCode(){
        String callingCode = country.getCallingCountryCode();
        assertNotNull(callingCode);
        assertEquals("+33",callingCode);
    }
}