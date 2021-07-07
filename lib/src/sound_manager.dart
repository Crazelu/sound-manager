import 'dart:async';

import 'package:flutter/services.dart';

class SoundManager {
  static const MethodChannel _channel = const MethodChannel('sound_manager');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  //sound recording

  Future<void> record() {}

  Future<void> pauseRecording() {}

  Future<void> stopRecording() {}

  //Audio playing

  Future<void> play() {}

  Future<void> pause() {}

  Future<void> stop() {}
}
