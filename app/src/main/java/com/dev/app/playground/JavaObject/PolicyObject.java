package com.dev.app.playground.JavaObject;

import java.security.PublicKey;

/**
 * Created by Saugat Shrestha on 12/29/2016.
 */
public class PolicyObject {
    String description;
    String title;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PolicyObject() {
        //empty constructor
    }

    public PolicyObject(String title, String description) {
        this.title = title;
        this.description = description;
    }

}
