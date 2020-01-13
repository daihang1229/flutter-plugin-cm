import 'dart:async';

import 'package:flutter/services.dart';

class Cm {
  static const MethodChannel channel =
      const MethodChannel('im.weshine.plugin/cm');

  /**
   * 初始化cm
   */
  static Future<String> initCm(Map map) async {
    String initResult;
    try {
      initResult = await channel.invokeMethod('initcm', map);
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
      loadRes = await channel.invokeMethod('loadad', map);
    } on Exception {
      loadRes = "CM_Plugin: Failed to load ad";
    }
    return loadRes;
  }

  static adState() {
    // ignore: missing_return
    channel.setMethodCallHandler((handler) {
      switch (handler.method) {
        case "adShow":
          print("adShow");
          break;
        case "videoComplete":
          print("videoComplete");
          break;
        case "adClose":
          print("adClose");
          break;
        case "videoError":
          print("videoError");
          break;
        default:
          break;
      }
    });
  }
}
