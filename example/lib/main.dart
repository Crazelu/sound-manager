import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:sound_manager_example/playing_audio.dart';

import 'recording_audio_demo.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(
          primarySwatch: Colors.blue,
          textButtonTheme: TextButtonThemeData(
            style: ButtonStyle(
              backgroundColor: MaterialStateProperty.resolveWith(
                (states) => Colors.blue,
              ),
              foregroundColor: MaterialStateProperty.resolveWith(
                (states) => Colors.white,
              ),
              minimumSize: MaterialStateProperty.resolveWith(
                (states) => Size(150, 60),
              ),
            ),
          )),
      home: Demo(),
    );
  }
}

class Demo extends StatelessWidget {
  const Demo({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Sound Manager Plugin Example"),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            TextButton(
              onPressed: () {
                Navigator.of(context).push(CupertinoPageRoute(
                    builder: (_) => RecordingAudioDemoApp()));
              },
              child: Text("Recording Audio Demo"),
            ),
            SizedBox(height: 20),
            TextButton(
              onPressed: () {
                Navigator.of(context).push(
                    CupertinoPageRoute(builder: (_) => PlayingAudioDemo()));
              },
              child: Text("Playing Audio Demo"),
            ),
            SizedBox(height: 20),
          ],
        ),
      ),
    );
  }
}
