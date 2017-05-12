package com.dev.app.playground;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dev.app.playground.Helper.GlobalState;

/**
 * Created by Saugat Shrestha on 12/4/2016.
 */
public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GlobalState globalState = GlobalState.singleton;
        String Check =  globalState.getPrefsIsLoggedIn();

        if (Check.equals("true")){
            startActivity(new Intent(SplashActivity.this,ActivityHome.class));
            finish();
        }else {
            startActivity(new Intent(SplashActivity.this,ActivityLogin.class));
            finish();
        }
    }
}
