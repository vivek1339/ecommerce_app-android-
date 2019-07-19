package com.example.vivekshashank.begin0;

import android.app.Application;
import android.os.Bundle;

import com.firebase.client.Firebase;

/**
 * Created by vivekshashank on 12/25/2017.
 */

public class begin0 extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

}
