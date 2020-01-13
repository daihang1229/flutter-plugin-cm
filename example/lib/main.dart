import 'dart:collection';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
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

  }

  void _onEvent(Object event) {
    print(event);
  }

  void _onError(Object error) {
    PlatformException exception = error;
    exception?.message ?? '';
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

  Future<Null> _loadAd() async {
    String result;
    //广告id
    Map map = new HashMap();
    map["adId"] = "901121430";

    result = await Cm.loadAd(map);

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
        default:
          break;
      }
    });
  }


  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
          appBar: AppBar(
            title: const Text('Plugin example app'),
          ),
          body: Center(
            child: Text(
              'AD State: $_state\n',
            ),
          ),
          floatingActionButton: FloatingActionButton(
            onPressed: _loadAd,
            tooltip: 'Increment',
            child: Icon(Icons.add),
          )),
    );
  }
}
