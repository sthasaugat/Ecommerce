package com.dev.app.playground.JavaObject;

/**
 * Created by Saugat Shrestha on 12/7/2016.
 */

public class Add2wishList {
    String pID, image, productName, price, wishID;

    public Add2wishList(String pID, String productName, String price, String image, String wishID) {
        this.image = image;
        this.price = price;
        this.productName = productName;
        this.wishID = wishID;
        this.pID = pID;
    }

    public Add2wishList() {

    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String product) {
        this.productName = product;
    }

    public String getWishID() {
        return wishID;
    }

    public void setWishID(String wishID) {
        this.wishID = wishID;
    }

}
