///Audio encoder for audio recording
///
///Defaults to [AudioEncoder.amr_nb]
///
///aac -> AAC Low Complexity (AAC-LC) audio codec
///
///aac_eld -> Enhanced Low Delay AAC (AAC-ELD) audio codec
///
///amr_nb -> AMR (Narrowband) audio codec
///
///amr_wb -> AMR (Wideband) audio codec
///
///he_aac -> High Efficiency AAC (HE-AAC) audio codec
///
///opus -> Opus audio codec
///
///vorbis -> Ogg Vorbis audio codec (Support is optional)
enum AudioEncoder { aac, aac_eld, amr_nb, amr_wb, he_aac, opus, vorbis }

int audioEncoderEnumToInt(AudioEncoder encoder) {
  switch (encoder) {
    case AudioEncoder.aac:
      return 3;
    case AudioEncoder.aac_eld:
      return 5;
    case AudioEncoder.vorbis:
      return 6;
    case AudioEncoder.amr_wb:
      return 2;
    case AudioEncoder.he_aac:
      return 4;
    case AudioEncoder.opus:
      return 7;
    default:
      //AudioEncoder.amr_nb
      return 1;
  }
}
