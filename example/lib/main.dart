import 'package:flutter/material.dart';
import 'dart:async';
import 'package:sound_manager/sound_manager.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
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
      home: DemoApp(),
    );
  }
}

class DemoApp extends StatefulWidget {
  @override
  _DemoAppState createState() => _DemoAppState();
}

class _DemoAppState extends State<DemoApp> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance?.addPostFrameCallback((timeStamp) {
      requestPermission();
    });
  }

  Future<void> requestPermission() async {
    try {
      await SoundManager.init();
      print("Done");
    } catch (e) {
      print(e);
    }
  }

  Future<void> recordAudio() async {
    try {
      await SoundManager.record(fileName: "testing2", directory: "/Download");
    } catch (e) {
      print(e);
    }
  }

  Future<void> pauseRecording() async {
    try {
      await SoundManager.pauseRecording();
    } catch (e) {
      print(e);
    }
  }

  Future<void> resume() async {
    try {
      await SoundManager.resumeRecording();
    } catch (e) {
      print(e);
    }
  }

  Future<void> saveRecording() async {
    try {
      var path = await SoundManager.saveRecording();
      print("Recording saved at $path");
    } catch (e) {
      print(e);
    }
  }

  Future<void> cancelRecording() async {
    try {
      await SoundManager.cancelRecording();
    } catch (e) {
      print(e);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Plugin example app'),
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          TextButton(
              onPressed: () => recordAudio(), child: Text("Start recording")),
          SizedBox(height: 20),
          TextButton(
              onPressed: () => pauseRecording(),
              child: Text("Pause recording")),
          SizedBox(height: 20),
          TextButton(
              onPressed: () => resume(), child: Text("Resume recording")),
          SizedBox(height: 20),
          TextButton(
              onPressed: () => saveRecording(), child: Text("Save recording")),
          SizedBox(height: 20),
          TextButton(
              style: ButtonStyle(
                  backgroundColor: MaterialStateProperty.resolveWith(
                      (states) => Colors.red)),
              onPressed: () => cancelRecording(),
              child: Text("Cancel recording")),
          SizedBox(height: 20),
        ],
      ),
    );
  }
}
