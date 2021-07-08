import 'dart:async';
import 'package:flutter/services.dart';
import 'package:sound_manager/src/utils/utils.dart';

class SoundManager {
  static const MethodChannel _channel = const MethodChannel('sound_manager');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  ///Requests permissions to record audio and write to external storage on Android
  static Future<void> init() async {
    await _channel.invokeMethod("requestPermission");
  }

  //sound recording

  ///Starts recording audio
  static Future<bool> record({
    String? fileName,
    OutputFormat outputFormat = OutputFormat.three_gpp,
    AudioEncoder audioEncoder = AudioEncoder.amr_nb,
    AudioSource audioSource = AudioSource.mic,
  }) async {
    return await _channel.invokeMethod(
      "recordAudio",
      {
        "fileName": fileName,
        "outputFormat": outputFormatEnumToInt(outputFormat),
        "audioEncoder": audioEncoderEnumToInt(audioEncoder),
        "audioSource": audioSourceEnumToInt(audioSource),
      },
    );
  }

  static Future<void> pauseRecording() async {
    return await _channel.invokeMethod("pauseRecording");
  }

  static Future<void> resumeRecording() async {
    return await _channel.invokeMethod("resumeRecording");
  }

  static Future<void> cancelRecording() async {
    return await _channel.invokeMethod("cancelRecording");
  }

  static Future<void> saveRecording() async {
    return await _channel.invokeMethod("saveRecording");
  }

  //Audio playing

  Future<void> play({required String filePath}) async {
    //TODO: Mark filePath as required
    return await _channel.invokeMethod("playAudioFile", {"filePath": filePath});
  }

  Future<void> pause() async {
    return await _channel.invokeMethod("pauseAudioPlayback");
  }

  Future<void> stop() async {
    return await _channel.invokeMethod("stopPlayingAudio");
  }

  Future<void> seekTo() async {
    return await _channel.invokeMethod("seekTo");
  }

  Future<void> loop() async {
    return await _channel.invokeMethod("setLooping");
  }
}
