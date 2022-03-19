# sound_manager

A Flutter plugin for recording and playing audio files on Android and iOS

## Install

Import the package in your project:

```yaml
  git:
      url: https://github.com/Crazelu/sound-manager.git
  ```
  
## Usage

Initialize plugin and request permissions

```dart
   //record is set to true to request permissions for recording audio
   //for playing audio, this parameter can be omitted
   await SoundManager.init(record: true);
 ```
 
Record Audio

```dart
   await SoundManager.record(
        fileName: "test",
        directory: "Download",
        outputFormat: OutputFormat.amr_wb,
        audioEncoder: AudioEncoder.amr_wb,
        bitRate: 128000,
      );
```

Play Audio 

```dart
   await SoundManager.play(filePath: "/audio-file-path");
```

## What's in the box
- Record audio
- Pause and resume recording (Android API level 24+)
- Cancel audio recording
- Save recordings in amr, mp4, aac and 3gp formats
- Encode audio using AAC, AMR_NB, AMR_WB, etc encoders. [Learn more](https://developer.android.com/reference/android/media/MediaRecorder.AudioEncoder)
- Play audio files from device and the internet (https/rstp streams)
- Pause and resume playing audio files
- Seek to position
- Audio looping

## TODO
- Add support for ogg audio format
- Include bit rate and sampling rate customizations
- Add support for playing asset files
