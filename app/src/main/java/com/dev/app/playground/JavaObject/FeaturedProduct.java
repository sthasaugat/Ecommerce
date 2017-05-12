package com.dev.app.playground.JavaObject;

import java.io.Serializable;

/**
 * Created by dell on 11/16/2016.
 */
public class FeaturedProduct implements Serializable {
    private String productName, image, category_id, pID, price, status;

    public FeaturedProduct(){

    }

    public FeaturedProduct(String image, String product, String price, String category_id, String pID, String status) {
        this.image = image;
        this.productName = product;
        this.price = price;
        this.category_id = category_id;
        this.pID = pID;
        this.status = status;
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

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
