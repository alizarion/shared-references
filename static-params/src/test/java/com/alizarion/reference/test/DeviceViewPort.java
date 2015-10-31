package com.alizarion.reference.test;

/**
 * Created by sphinx on 05/11/14.
 */
public enum DeviceViewPort {

    PHONE(500),
    TAB(959),
    DESKTOP(1920);


    private Integer viewportWidth;


    DeviceViewPort(Integer portrait) {
        this.viewportWidth = portrait;
    }

    public Integer getViewportWidth() {
        return viewportWidth;
    }
}
