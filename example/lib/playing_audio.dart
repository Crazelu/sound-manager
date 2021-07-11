import 'package:flutter/material.dart';
import 'package:sound_manager/sound_manager.dart';

class PlayingAudioDemo extends StatefulWidget {
  const PlayingAudioDemo({Key? key}) : super(key: key);

  @override
  _PlayingAudioDemoState createState() => _PlayingAudioDemoState();
}

class _PlayingAudioDemoState extends State<PlayingAudioDemo> {
  Future<void> play() async {
    await SoundManager.play(
        filePath:
            "/Users/crazelu/Library/Developer/CoreSimulator/Devices/A3B326B6-52A1-4666-898C-E580AD1F8601/data/Containers/Data/Application/71179A42-3E5A-4852-A695-A6DA7256994E/Documents/sept.m4a");
  }

  Future<void> pause() async {
    await SoundManager.pauseAudio();
  }

  Future<void> resume() async {
    await SoundManager.resumeAudio();
  }

  Future<void> seekTo() async {
    await SoundManager.seekTo(1000);
  }

  Future<void> stop() async {
    await SoundManager.stopAudio();
  }

  Future<void> loop() async {
    await SoundManager.loop();
  }

  Future<void> init() async {
    await SoundManager.init();
  }

  @override
  void initState() {
    super.initState();
    init();
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () {
        Navigator.of(context).pop();
        return Future.value(false);
      },
      child: Scaffold(
        appBar: AppBar(
          title: const Text('Playing Audio Demo'),
        ),
        body: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            TextButton(onPressed: () => play(), child: Text("Play")),
            SizedBox(height: 20),
            TextButton(onPressed: () => pause(), child: Text("Pause")),
            SizedBox(height: 20),
            TextButton(onPressed: () => resume(), child: Text("Resume")),
            SizedBox(height: 20),
            TextButton(
                onPressed: () => seekTo(),
                child: Text("Seek to (1000 milliseconds)")),
            SizedBox(height: 20),
            TextButton(
                style: ButtonStyle(
                    backgroundColor: MaterialStateProperty.resolveWith(
                        (states) => Colors.red)),
                onPressed: () => stop(),
                child: Text("Stop")),
            SizedBox(height: 20),
          ],
        ),
      ),
    );
  }
}
