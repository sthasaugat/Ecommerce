package com.dev.app.playground;

/**
 * Created by Saugat Shrestha on 12/8/2016.
 */

public class loginDetail {
    String sId;

    public loginDetail() {

    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getsFullName() {
        return sFullName;
    }

    public void setsFullName(String sFullName) {
        this.sFullName = sFullName;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getsCIty() {
        return sCIty;
    }

    public void setsCIty(String sCIty) {
        this.sCIty = sCIty;
    }

    public String getsAddress() {
        return sAddress;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public String getsMobile() {
        return sMobile;
    }

    public void setsMobile(String sMobile) {
        this.sMobile = sMobile;
    }

    public String getsImage() {
        return sImage;
    }

    public void setsImage(String sImage) {
        this.sImage = sImage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    String sFullName;
    String sEmail;
    String sCIty;
    String sAddress;
    String sMobile;
    String sImage;
    public String token;

    public loginDetail(String sId, String FullName, String sEmail,
                       String sCIty, String sAddress, String sMobile, String token,String sImage) {
        this.sId = sId;
        this.sEmail = sEmail;
        this.sFullName = FullName;
        this.sCIty = sCIty;
        this.sAddress = sAddress;
        this.sMobile = sMobile;
        this.token = token;
        this.sImage = sImage;
    }
}