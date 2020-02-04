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
      print("init cm 15");
      initResult = await channel.invokeMethod('initcm', map);
    } on Exception {
      initResult = "CM_Plugin: Cm init failed";
    }
    return initResult;
  }

  /**
   * 请求激励视频ad
   */
  static Future<String> loadReward(Map map) async {
    String loadRes;
    try {
      loadRes = await channel.invokeMethod('rewardad', map);
    } on Exception {
      loadRes = "CM_Plugin: Failed to load reward ad";
    }
    return loadRes;
  }

  /**
   * 展示激励视频ad
   */
  static Future<String> showReward() async {
    String loadRes;
    try {
      loadRes = await channel.invokeMethod('showreward');
    } on Exception {
      print(loadRes);
      loadRes = "CM_Plugin: Cannot show reward ad";
    }
    return loadRes;
  }

  /**
   * 请求全屏ad
   */
  static Future<String> loadFull(Map map) async {
    String loadRes;
    try {
      loadRes = await channel.invokeMethod('fullad', map);
    } on Exception {
      print("asada $loadRes");
      loadRes = "CM_Plugin: Failed to load full ad";
    }
    return loadRes;
  }

  /**
   * 展示全屏ad
   */
  static Future<String> showFull() async {
    String loadRes;
    try {
      loadRes = await channel.invokeMethod('showfull');
    } on Exception {
      loadRes = "CM_Plugin: Cannot show full ad";
    }
    return loadRes;
  }


//  static adState() {
//    // ignore: missing_return
//    channel.setMethodCallHandler((handler) {
//      switch (handler.method) {
//        case "adShow":
//          print("adShow");
//          break;
//        case "adSkip":
//          print("adSkip");
//          break;
//        case "videoComplete":
//          print("videoComplete");
//          break;
//        case "adClose":
//          print("adClose");
//          break;
//        case "videoError":
//          print("videoError");
//          break;
//        default:
//          break;
//      }
//    });
//  }
}
