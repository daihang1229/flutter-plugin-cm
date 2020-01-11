import 'dart:async';

import 'package:flutter/services.dart';

class Cm {
  static const MethodChannel _channel =
      const MethodChannel('im.weshine.plugin/cm');

  /**
   * 初始化cm
   */
  static Future<String> initCm(Map map) async {
    String initResult;
    try {
      initResult = await _channel.invokeMethod('initcm', map);
    } on Exception {
      initResult = "CM_Plugin: Failed to init ad";
    }
    return initResult;
  }

  /**
   * 加载ad
   */
  static Future<String> loadAd(Map map) async {
    String loadRes;
    try {
      loadRes = await _channel.invokeMethod('loadad', map);
    } on Exception {
      loadRes = "CM_Plugin: Failed to load ad";
    }
    return loadRes;
  }
}
