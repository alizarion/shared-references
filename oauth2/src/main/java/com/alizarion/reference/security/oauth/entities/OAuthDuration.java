package com.alizarion.reference.security.oauth.entities;

/**
 * @author selim@openlinux.fr.
 */
public enum  OAuthDuration {

    /**
     * to get auth code with refresh token
     */
    P("permanent")
    ,
    /**
     * to get auth code without refresh token
     */
    T("temporary");

    private String key;

    public final static String DURATION_PARAM = "duration";


    OAuthDuration(String key) {
        this.key = key;
    }

    public String toString(){
        return  this.key;
    }

    public boolean equals(final String key){
        return key != null && key.toLowerCase().equals(this.key);

    }
}
