import 'dart:async';

import 'package:flutter/services.dart';

class ScarabeiFlutter {
  static const MethodChannel _channel =
      const MethodChannel('scarabei_flutter');

  static Future<String> get platformVersion =>
      _channel.invokeMethod('getPlatformVersion');
}
