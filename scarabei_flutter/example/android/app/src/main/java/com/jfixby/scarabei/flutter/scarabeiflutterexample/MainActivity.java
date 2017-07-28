package com.jfixby.scarabei.flutter.scarabeiflutterexample;

import android.os.Bundle;
import com.jfixby.scarabei.red.android.ScarabeiAndroidDeployer;
import com.jfixby.scarabei.red.flutter.ScarabeiFlutterAndroid;
import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class MainActivity extends FlutterActivity {

    final ScarabeiAndroidDeployer deployer = new ScarabeiAndroidDeployer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        GeneratedPluginRegistrant.registerWith(this);

        deployer.tryToDeploy(this);

        ScarabeiFlutterAndroid plugin = new ScarabeiFlutterAndroid();
        registerPlugin(plugin);
    }

    private void registerPlugin(MethodChannel.MethodCallHandler plugin) {
        String channelName = plugin.getClass().getCanonicalName();
        registerPlugin(plugin, channelName);
    }

    private void registerPlugin(MethodChannel.MethodCallHandler plugin, String channelName) {
        PluginRegistry registry = this;
        String className = plugin.getClass().getCanonicalName();
        Registrar registrar = registry.registrarFor(className);
        final MethodChannel channel = new MethodChannel(registrar.messenger(), channelName);
        channel.setMethodCallHandler(plugin);
    }
}
