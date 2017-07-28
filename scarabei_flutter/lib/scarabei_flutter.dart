import 'dart:async';

import 'package:flutter/services.dart';

class ScarabeiFlutter {
  static const MethodChannel _channel =
      const MethodChannel('com.jfixby.scarabei.red.flutter.ScarabeiFlutterAndroid');

  static Future<String> get platformVersion =>
      _channel.invokeMethod('getPlatformVersion');
}
