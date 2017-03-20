package com.pixelall.pixellib.util;

import java.io.File;

/**
 * Created by lxl on 2017/3/14.
 */

class PixelParams {
    private String companyName;
    private String app_key;
    private File Photo;

    public File getPhoto() {
        return Photo;
    }

    public void setPhoto(File photo) {
        Photo = photo;
    }
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }
}
