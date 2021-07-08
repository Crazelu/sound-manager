import 'dart:async';

import 'package:flutter/services.dart';

class SoundManager {
  static final String REQUEST_PERMISSION = "requestPermission";
  static final String RECORD_AUDIO = "recordAudio";
  static final String PAUSE_RECORDING = "pauseRecording";
  static final String RESUMING_RECORDING = "resumeRecording";
  static final String CANCEL_RECORDING = "cancelRecording";
  static final String SAVE_RECORDING = "saveRecording";
  static final String PLAY_AUDIO = "playAudioFile";
  static final String PAUSE_AUDIO = "pauseAudioFile";
  static final String STOP_PLAYING_AUDIO = "stopPlayingAudioFile";
  static final String SEEK_TO = "seekTo";
  static final String SET_LOOPING = "setLooping";

  static const MethodChannel _channel = const MethodChannel('sound_manager');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  Future<void> requestPermission() async {
    return await _channel.invokeMethod(REQUEST_PERMISSION);
  }

  //sound recording

  Future<bool> record({String? fileName, String? filePath}) async {
    return await _channel.invokeMethod(RECORD_AUDIO);
  }

  Future<void> pauseRecording() async {
    return await _channel.invokeMethod(PAUSE_RECORDING);
  }

  Future<void> cancelRecording() async {
    return await _channel.invokeMethod(CANCEL_RECORDING);
  }

  Future<void> saveRecording() async {
    return await _channel.invokeMethod(SAVE_RECORDING);
  }

  //Audio playing

  Future<void> play({required String filePath}) async {
    //TODO: Mark filePath as required
    return await _channel.invokeMethod(PLAY_AUDIO, {"filePath": filePath});
  }

  Future<void> pause() async {
    return await _channel.invokeMethod(PAUSE_AUDIO);
  }

  Future<void> stop() async {
    return await _channel.invokeMethod(STOP_PLAYING_AUDIO);
  }
}
