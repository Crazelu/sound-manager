import 'dart:async';

import 'package:flutter/services.dart';

class SoundManager {
  static final String _REQUEST_PERMISSION = "requestPermission";
  static final String _RECORD_AUDIO = "recordAudio";
  static final String _PAUSE_RECORDING = "pauseRecording";
  static final String _RESUMING_RECORDING = "resumeRecording";
  static final String _CANCEL_RECORDING = "cancelRecording";
  static final String _SAVE_RECORDING = "saveRecording";
  static final String _PLAY_AUDIO = "playAudioFile";
  static final String _PAUSE_AUDIO = "pauseAudioFile";
  static final String _STOP_PLAYING_AUDIO = "stopPlayingAudioFile";
  static final String _SEEK_TO = "seekTo";
  static final String _SET_LOOPING = "setLooping";

  static const MethodChannel _channel = const MethodChannel('sound_manager');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<void> init() async {
    await _channel.invokeMethod(_REQUEST_PERMISSION);
  }

  //sound recording

  Future<bool> record({String? fileName, String? filePath}) async {
    return await _channel.invokeMethod(_RECORD_AUDIO);
  }

  Future<void> pauseRecording() async {
    return await _channel.invokeMethod(_PAUSE_RECORDING);
  }

  Future<void> cancelRecording() async {
    return await _channel.invokeMethod(_CANCEL_RECORDING);
  }

  Future<void> saveRecording() async {
    return await _channel.invokeMethod(_SAVE_RECORDING);
  }

  //Audio playing

  Future<void> play({required String filePath}) async {
    //TODO: Mark filePath as required
    return await _channel.invokeMethod(_PLAY_AUDIO, {"filePath": filePath});
  }

  Future<void> pause() async {
    return await _channel.invokeMethod(_PAUSE_AUDIO);
  }

  Future<void> stop() async {
    return await _channel.invokeMethod(_STOP_PLAYING_AUDIO);
  }
}
