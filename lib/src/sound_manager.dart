import 'dart:async';
import 'package:flutter/services.dart';
import 'package:sound_manager/src/utils/utils.dart';

class SoundManager {
  static const MethodChannel _channel =
      const MethodChannel("tech.devcrazelu.sound_manager");

  ///Requests permissions to record audio and write to external storage on Android
  static Future<void> init() async {
    await _channel.invokeMethod("requestPermission");
  }

  //sound recording

  ///Starts recording audio.
  ///
  ///`fileName` -> Name to save the file produced during the recording.
  ///Defaults to a uniquely generated file name with timestamp.
  ///
  ///`directory` -> Directory to save the file produced during the recording.
  ///On Android, it defaults to `Environment.getExternalStorageDirectory()`.
  ///
  ///`outputFormat` -> Output format of the file produced during the recording.
  ///
  ///`audioEncoder` -> Audio encoder to be used for recording.
  ///
  ///`audioSource` -> Audio source to be used for the recording.
  static Future<bool> record({
    String? fileName,
    String? directory,
    OutputFormat outputFormat = OutputFormat.ogg,
    AudioEncoder audioEncoder = AudioEncoder.amr_nb,
    AudioSource audioSource = AudioSource.mic,
  }) async {
    return await _channel.invokeMethod(
      "recordAudio",
      {
        "fileName": fileName,
        "directory": directory,
        "outputFormat": outputFormatEnumToInt(outputFormat),
        "audioEncoder": audioEncoderEnumToInt(audioEncoder),
        "audioSource": audioSourceEnumToInt(audioSource),
      },
    );
  }

  ///Pauses an ongoing audio recording.
  ///
  ///Does nothing if `record()` is not called first.
  static Future<void> pauseRecording() async {
    return await _channel.invokeMethod("pauseRecording");
  }

  ///Resumes a paused audio recording session.
  ///
  ///Does nothing if the recording is not paused.
  static Future<void> resumeRecording() async {
    return await _channel.invokeMethod("resumeRecording");
  }

  ///Cancels an ongoing audio recording.
  ///
  ///Does nothing if `record()` is not called first
  static Future<bool> cancelRecording() async {
    return await _channel.invokeMethod("cancelRecording");
  }

  ///Stops the audio recording session and releases resources.
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
