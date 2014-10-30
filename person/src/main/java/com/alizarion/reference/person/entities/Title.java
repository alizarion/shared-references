package com.alizarion.reference.person.entities;

/**
 * Define physical person title
 * @author selim@openlinux.fr.
 */
public enum Title {

    MR("Mr","male"),
    MISS("Miss","female"),
    MRS("Mrs","male"),
    MS("Ms","female");

    private String title;
    private String gender;


    /**
     * Default title constructor
     * @param title
     */
    Title(final String title,final String gender) {
        this.title = title;
        this.gender = gender;
    }

    /**
     * Method to get the title key that will be
     * used for internationalization
     * @return titleKey param
     */
    public String getTitleKey(){
        return this.title.toUpperCase();
    }


    /**
     * Method to get the title key that will be
     * used for internationalization
     * @return titleKey param
     */
    public String getGenderKey(){
        return this.gender.toLowerCase();
    }

}

