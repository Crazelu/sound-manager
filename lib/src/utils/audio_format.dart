///Represents the audio format for encoding an audio recording on iOS
///
///Defaults to `AudioFormat.appleLossless`
enum AudioFormat {
  appleLossless,
  flac,
}

int audioFormatEnumToInt(AudioFormat format) {
  switch (format) {
    case AudioFormat.flac:
      return 1;
    default:
      //AudioFormat.appleLossless
      return 0;
  }
}
