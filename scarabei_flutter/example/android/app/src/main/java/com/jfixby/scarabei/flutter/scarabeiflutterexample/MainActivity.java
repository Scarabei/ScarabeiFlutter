package com.jfixby.scarabei.flutter.scarabeiflutterexample;

import android.os.Bundle;
import com.jfixby.scarabei.red.android.ScarabeiAndroidDeployer;
import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {

    final ScarabeiAndroidDeployer deployer = new ScarabeiAndroidDeployer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);

        deployer.tryToDeploy(this);

    }
}
