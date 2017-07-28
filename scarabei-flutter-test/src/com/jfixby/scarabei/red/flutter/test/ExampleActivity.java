
package com.jfixby.scarabei.red.flutter.test;

import com.jfixby.scarabei.api.flutter.plugins.FlutterPlugin;
import com.jfixby.scarabei.api.flutter.plugins.FlutterPluginSpecs;
import com.jfixby.scarabei.api.flutter.plugins.FlutterPlugins;
import com.jfixby.scarabei.api.flutter.plugins.FlutterPluginsComponent;
import com.jfixby.scarabei.red.android.ScarabeiAndroidDeployer;
import com.jfixby.scarabei.red.flutter.RedFlutterPlugins;
import com.jfixby.scarabei.red.flutter.ScarabeiFlutterAndroid;

import android.os.Bundle;
import io.flutter.app.FlutterActivity;

public class ExampleActivity extends FlutterActivity {

	final ScarabeiAndroidDeployer deployer = new ScarabeiAndroidDeployer();

	@Override
	protected void onCreate (final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.deployer.tryToDeploy(this);

		final FlutterPluginsComponent flutterPlugins = new RedFlutterPlugins(this);
		FlutterPlugins.installComponent(flutterPlugins);

		final FlutterPluginSpecs specs = FlutterPlugins.newPluginSpecs();

		specs.methodCallHandler = new ScarabeiFlutterAndroid();
		specs.channelName = null;

		final FlutterPlugin plugin = FlutterPlugins.registerPlugin(specs);

	}

}
