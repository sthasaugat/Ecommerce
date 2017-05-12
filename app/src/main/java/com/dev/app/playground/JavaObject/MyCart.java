package com.dev.app.playground.JavaObject;

/**
 * Created by Saugat Shrestha on 12/15/2016.
 */

public class MyCart {
    private String productName, image, pID, price, status;

    public MyCart(){

    }
    public MyCart(String image, String product, String price, String pID, String status) {
        this.image = image;
        this.productName = product;
        this.price = price;
        this.pID = pID;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }



}
