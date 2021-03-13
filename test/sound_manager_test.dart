import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'file:///C:/projects/sound_manager/lib/src/sound_manager.dart';

void main() {
  const MethodChannel channel = MethodChannel('sound_manager');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await SoundManager.platformVersion, '42');
  });
}
