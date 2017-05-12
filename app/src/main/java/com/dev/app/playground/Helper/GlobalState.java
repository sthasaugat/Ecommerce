package com.dev.app.playground.Helper;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;

import java.io.IOException;
import java.net.InetAddress;

// preference hold information for certain period
//

public class GlobalState extends Application {

    SharedPreferences isLoggedIn;
    SharedPreferences.Editor isLoggedInEditor;

    SharedPreferences validUserInfo;
    SharedPreferences.Editor validUserInfoEditor;
    public static GlobalState singleton;
    public static int gCartCount = 0;
    public static int gWishlistCount = 0;

    public static final String PREFS_IS_LOGGED_IN = "is logged in"; // to check if user is logged in
    public static final String PREFS_VALID_USER_INFO = "valid user info"; // to store the user information like name and phone number

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCreate() {
        super.onCreate();
        // mapped with isLogged which is defined above
        isLoggedIn = this.getSharedPreferences(PREFS_IS_LOGGED_IN, 0);

        // mapped with isLoggedInEditor which is defined above
        isLoggedInEditor = isLoggedIn.edit();

        validUserInfo = this.getSharedPreferences(PREFS_VALID_USER_INFO, 0);
        validUserInfoEditor = validUserInfo.edit();

        singleton = this;
    }

    /**
     * @return MySIngleton instance
     */
    public GlobalState getInstance() {
        return singleton;
    }

    public String getPrefsIsLoggedIn(){
        return isLoggedIn.getString(PREFS_IS_LOGGED_IN,"");
    }

    public void setPrefsIsLoggedIn(String loggedStatus,int resultCode){
        if (resultCode == 1) {
            isLoggedInEditor.putString(PREFS_IS_LOGGED_IN, loggedStatus).commit();
        } else {
            isLoggedInEditor.remove(PREFS_IS_LOGGED_IN).commit();
        }
    }

    public String getPREFS_VALID_USER_INFO(){
        return validUserInfo.getString(PREFS_VALID_USER_INFO,"");
    }

    public void setPrefsValidUserInfo(String validUser,int resultCode){
        if (resultCode == 1) {
            validUserInfoEditor.putString(PREFS_VALID_USER_INFO, validUser).commit();
        } else {
            validUserInfoEditor.remove(PREFS_VALID_USER_INFO).commit();
        }
    }
    public static boolean isNetworkOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
    public static boolean isGoogleReachableWithInetAddress() {
        try {
            InetAddress inetAddress = InetAddress.getByName("www.google.com");

            return inetAddress != null && !inetAddress.equals("");
        } catch (Exception ex) {
            return false;
        }
    }


}