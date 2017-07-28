
package com.jfixby.scarabei.red.flutter;

import com.jfixby.scarabei.android.api.Android;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** ScarabeiFlutterPlugin */
public class ScarabeiFlutterAndroid implements MethodCallHandler {
	/** Plugin registration. */
	public static void registerWith (final Registrar registrar) {
		final MethodChannel channel = new MethodChannel(registrar.messenger(), "scarabei_flutter");
		channel.setMethodCallHandler(new ScarabeiFlutterAndroid());
	}

	@Override
	public void onMethodCall (final MethodCall call, final Result result) {
		if (call.method.equals("getPlatformVersion")) {

// L.d("Hello!");
			final String message = "Android " + android.os.Build.VERSION.RELEASE + " " + Android.getSystemInfo();
// String message = "Android " + android.os.Build.VERSION.RELEASE;

			result.success(message);
		} else {
			result.notImplemented();
		}
	}
}
