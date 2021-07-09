///Represents the output format for encoding an audio recording
///
///Defaults to [OutputFormat.three_gpp]
///
///aac -> AAC ADTS file format
///
///amr_nb -> AMR NB file format
///
///amr_wb -> AMR WB file format
///
///mp4 -> MPEG4 media file format
///
///three_gpp -> 3GPP media file format
///
///webm -> VP8/VORBIS data in a WEBM container

enum OutputFormat { three_gpp, mp4, aac, amr_wb, amr_nb, webm }

int outputFormatEnumToInt(OutputFormat format) {
  switch (format) {
    case OutputFormat.webm:
      return 9;
    case OutputFormat.mp4:
      return 2;
    case OutputFormat.aac:
      return 6;
    case OutputFormat.amr_nb:
      return 3;
    case OutputFormat.amr_wb:
      return 4;
    default:
      //OutputFormat.three_gpp
      return 1;
  }
}
