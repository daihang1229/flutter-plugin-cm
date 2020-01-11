import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:cm/cm.dart';

void main() {
  const MethodChannel channel = MethodChannel('cm');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await Cm.platformVersion, '42');
  });
}
