package com.alizarion.reference.person.entities;

/**
 * Define physical person title
 * @author selim@openlinux.fr.
 */
public enum Title {

    MR("Mr"),
    MISS("Miss"),
    MRS("Mrs"),
    MS("Ms");

    private String title;

    /**
     * Default title constructor
     * @param title
     */
    Title(final String title) {
        this.title = title;
    }

    /**
     * Method to get the title key that will be
     * used for internationalization
     * @return titleKey param
     */
    public String getTitleKey(){
        return this.title.toUpperCase();
    }

}

