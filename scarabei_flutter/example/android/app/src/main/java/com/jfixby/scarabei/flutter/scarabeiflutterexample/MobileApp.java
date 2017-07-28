package com.jfixby.scarabei.flutter.scarabeiflutterexample;

import android.support.multidex.MultiDex;


public class MobileApp extends io.flutter.app.FlutterApplication {

    protected void attachBaseContext(android.content.Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
