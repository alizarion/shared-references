package com.alizarion.reference.security.oauth.oauth2.entities;

import com.alizarion.reference.exception.NotImplementedException;

/**
 * Created by sphinx on 20/10/14.
 */
public enum OAuthResponseType {

    C("code")

    ,

    T("token")

    ,
    //authorization request proceded from tokenEndPoint ex : grant_type password
    N("none"),

    I("id_token");

    private String key;

    OAuthResponseType(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static OAuthResponseType  getInstance(final String key)
            throws NotImplementedException {
        for (OAuthResponseType type : OAuthResponseType.values()){
           if(key.toLowerCase().equals(type.getKey().toLowerCase())){
               return type;
           }
        }

        throw new NotImplementedException(key);
    }
}
