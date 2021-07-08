///Audio source for audio recording
///
///Defaults to [AudioSource.mic]
///
///mic -> Microphone audio source
///
///cam -> Microphone audio source tuned for video recording, with the same orientation as the camera if available.
enum AudioSource { mic, cam }

int audioSourceEnumToInt(AudioSource source) {
  switch (source) {
    case AudioSource.cam:
      return 1;

    default:
      //AudioSource.mic
      return 0;
  }
}
