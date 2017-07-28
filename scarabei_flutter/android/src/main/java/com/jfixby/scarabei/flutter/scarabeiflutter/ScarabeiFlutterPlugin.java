package com.jfixby.scarabei.flutter.scarabeiflutter;

import com.jfixby.scarabei.android.api.Android;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * ScarabeiFlutterPlugin
 */
//public class ScarabeiFlutterPlugin implements MethodCallHandler {
//    /**
//     * Plugin registration.
//     */
//    public static void registerWith(Registrar registrar) {
//        final MethodChannel channel = new MethodChannel(registrar.messenger(), "scarabei_flutter");
//        channel.setMethodCallHandler(new ScarabeiFlutterPlugin());
//    }
//
//    @Override
//    public void onMethodCall(MethodCall call, Result result) {
//        if (call.method.equals("getPlatformVersion")) {
//
////            L.d("Hello!");
//            String message = "Android " + android.os.Build.VERSION.RELEASE + " " + Android.getSystemInfo();
////            String message = "Android " + android.os.Build.VERSION.RELEASE;
//
//            result.success(message);
//        } else {
//            result.notImplemented();
//        }
//    }
//}
