package com.alizarion.reference.security.oauth.oauth2.entities;

/**
 * @author selim@openlinux.fr.
 */
public enum  OAuthDuration {

    /**
     * to get auth code with refresh token
     */
    P("offline")
    ,
    /**
     * to get auth code without refresh token
     */
    T("online");

    private String key;

    public final static String DURATION_PARAM = "access_type";


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
