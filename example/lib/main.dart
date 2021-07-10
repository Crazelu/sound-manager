import 'package:flutter/material.dart';
import 'package:sound_manager_example/playing_audio.dart';

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
      home: PlayingAudioDemo(),
    );
  }
}
