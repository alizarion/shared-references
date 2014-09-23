package com.alizarion.reference.location.entities;

/**
 * Created by sphinx on 22/09/14.
 */
public enum PhoneType {

    L("landline"),
    M("mobile");
    private String key;

    PhoneType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
