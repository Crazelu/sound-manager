///Represents the audio format for encoding an audio recording on iOS
///
///Defaults to `AudioFormat.appleLossless`
enum AudioFormat {
  amr,
  appleLossless,
  flac,
  opus,
  audible,
  iLBC,
  qualcomm,
  ac3,
  aes3,
  amr_wb,
  mp4,
}

int audioFormatEnumToInt(AudioFormat format) {
  switch (format) {
    case AudioFormat.amr:
      return 1;
    case AudioFormat.flac:
      return 2;
    case AudioFormat.opus:
      return 3;
    case AudioFormat.audible:
      return 4;
    case AudioFormat.iLBC:
      return 5;
    case AudioFormat.qualcomm:
      return 6;
    case AudioFormat.ac3:
      return 7;
    case AudioFormat.aes3:
      return 8;
    case AudioFormat.amr_wb:
      return 9;
    case AudioFormat.mp4:
      return 10;
    default:
      //AudioFormat.appleLossless
      return 0;
  }
}
