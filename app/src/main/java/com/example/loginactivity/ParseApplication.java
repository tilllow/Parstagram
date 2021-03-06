package com.example.loginactivity;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Post.class);

        //set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed explicitly configured
        // any network interceptors must be added with the configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("9IWybSiMGujjLEXQr6MsVOvdDG3Ki0GOIBKu1EAS")
                .clientKey("tkFKZa4nDMKa3iU5psfAWWi8mjRWsxr9kugpXu2k")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }



}

