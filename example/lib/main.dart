import 'dart:collection';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:cm/cm.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _state = 'Unknown';

  @override
  void initState() {
    super.initState();
    _initCm();
    _adState();
  }

  Future<Null> _initCm() async {
    String result;
    Map map = new HashMap();
    map["appId"] = "5001121";
    map["appName"] = "APP测试媒体";
    map["debug"] = true;

    result = await Cm.initCm(map);

    if (!mounted) return;

    setState(() {
      _state = result;
    });

  }

  /**
   * 加载激励视频
   */
  Future<Null> _loadRewardAd() async {
    String result;
    //广告id
    Map map = new HashMap();
    map["adId"] = "901121430";

    result = await Cm.loadReward(map);

    if (!mounted) return;

    setState(() {
      _state = result;
    });
  }

  /**
   * 展示激励视频
   */
  Future<Null> _showRewardAd() async {
    String result;
    /**
     * 需要处理这个result
     */
    result = await Cm.showReward();

    if (!mounted) return;

    setState(() {
      _state = result;
    });
  }

  /**
   * 加载全屏广告
   */
  Future<Null> _loadFullAd() async {
    String result;
    //广告id
    Map map = new HashMap();
    map["adId"] = "901121375";
    result = await Cm.loadFull(map);

    if (!mounted) return;

    setState(() {
      _state = result;
    });
  }

  /**
   * 展示全屏广告
   */
  Future<Null> _showFullAd() async {
    String result;
    /**
     * 需要处理这个result
     */
    result = await Cm.showFull();

    if (!mounted) return;

    setState(() {
      _state = result;
    });
  }

  /**
   * 监听当前ad播放状态
   */
  static _adState() {
    // ignore: missing_return
    Cm.channel.setMethodCallHandler((handler) {
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
        case "adSkip":
          print("adSkip");
          break;
          break;
        default:
          break;
      }
    });
  }
  bool load = false;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
          appBar: AppBar(
            title: const Text('Plugin example app'),
          ),
          body: Center(
            child: GestureDetector(
              child:Text(
                'AD State: $_state\n',
              ),
              onTap: _showFullAd,
            )
          ),
          floatingActionButton: FloatingActionButton(
            onPressed: _loadFullAd,
            tooltip: 'Increment',
            child: Icon(Icons.add),
          )),
    );
  }
}
