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
///ogg -> Opus data in a Ogg container
///
///three_gpp -> 3GPP media file format
///
///webm -> VP8/VORBIS data in a WEBM container

enum OutputFormat { three_gpp, mp4, ogg, aac, amr_wb, amr_nb, webm }

int outputFormatEnumToInt(OutputFormat format) {
  switch (format) {
    case OutputFormat.webm:
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
      //OutputFormat.three_gpp
      return 0;
  }
}
