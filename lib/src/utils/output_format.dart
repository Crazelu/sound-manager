///Represents the output format for encoding an audio recording

enum OutputFormat { three_gpp, mp4, ogg, aac, amr_wb, amr_nb, webm }

int enumToInt(OutputFormat format) {
  switch (format) {
    case OutputFormat.three_gpp:
      return 1;
    case OutputFormat.mp4:
      return 2;
    case OutputFormat.ogg:
      return 3;
    case OutputFormat.aac:
      return 4;
    case OutputFormat.amr_nb:
      return 5;
    case OutputFormat.amr_wb:
      return 6;
    default:
      //webm
      return 7;
  }
}
