package tech.devcrazelu.sound_manager;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

interface AudioRecorderCallable<T>{
     Object call(@Nullable Activity activity, AudioRecorderUtil audioRecorderUtil) throws Exception;
}interface AudioPlayerCallable<T>{
     T call(AudioPlayerUtil audioPlayerUtil) throws Exception;
}

public class Callables {

//    public static class PermissionCallable implements AudioRecorderCallable<Void> {
//
//        @Override
//        public Void call(@NonNull Activity activity,@NonNull AudioRecorderUtil audioRecorderUtil) {
//
//                audioRecorderUtil.handlePermissionTask(activity);
//                return null;
//
//        }
//    }
//
//    public static class RecordAudioCallable implements AudioRecorderCallable<Boolean> {
//       private final String fileName;
//       private final int audioSource;
//       private final  int outputFormat;
//       private final  int audioEncoder;
//
//        public RecordAudioCallable(@Nullable String fileName,@NonNull int audioSource,@NonNull int outputFormat,@NonNull  int audioEncoder) {
//            this.fileName = fileName;
//            this.audioSource = audioSource;
//            this.outputFormat = outputFormat;
//            this.audioEncoder = audioEncoder;
//        }
//
//
//        @Override
//        public Boolean call(@Nullable Activity activity,@NonNull AudioRecorderUtil audioRecorderUtil) throws Exception {
//
//                if (audioRecorderUtil.doesAppHavePermission(activity)) {
//                    audioRecorderUtil.recordAudio(fileName, audioSource, outputFormat, audioEncoder);
//                    return true;
//                }
//                return false;
//
//
//        }
//    }
//
//    public static class CancelAudioRecordingCallable implements AudioRecorderCallable<Boolean> {
//
//        @Override
//        public Boolean call(@Nullable Activity activity,@NonNull AudioRecorderUtil audioRecorderUtil) {
//
//              return audioRecorderUtil.cancelRecording();
//
//        }
//    }
//
//    public static class PauseAudioRecordingCallable implements AudioRecorderCallable<Void> {
//
//        @Override
//        public Void call(@Nullable Activity activity,@NonNull AudioRecorderUtil audioRecorderUtil) {
//
//                audioRecorderUtil.pauseRecording();
//
//            return null;
//        }
//    }
//
//    public static class ResumeRecordingCallable implements AudioRecorderCallable<Void> {
//
//        @Override
//        public Void call(@Nullable Activity activity,@NonNull AudioRecorderUtil audioRecorderUtil) {
//
//                audioRecorderUtil.resumeRecordingAudio();
//
//            return null;
//        }
//    }
//
//    public static class SaveRecordingCallable implements AudioRecorderCallable<Void> {
//
//        @Override
//        public Void call(@Nullable Activity activity,@NonNull AudioRecorderUtil audioRecorderUtil){
//
//                audioRecorderUtil.saveRecording();
//            return null;
//        }
//    }

    public static class PlayAudioCallable implements AudioPlayerCallable<Void> {
        private final String filePath;

        public PlayAudioCallable(String filePath){
            this.filePath = filePath;
        }


        @Override
        public Void call(AudioPlayerUtil audioPlayerUtil) throws Exception {

                audioPlayerUtil.playAudio(filePath);

            return null;
        }
    }
    public static class PauseAudioPlaybackCallable implements AudioPlayerCallable<Void> {

        @Override
        public Void call(AudioPlayerUtil audioPlayerUtil){

                audioPlayerUtil.pauseAudio();

            return null;
        }
    }

    public static class StopPlayingAudioCallable implements AudioPlayerCallable<Void> {

        @Override
        public Void call(AudioPlayerUtil audioPlayerUtil) {
            audioPlayerUtil.stopAudio();
            return null;
        }
    }

    public static class SeekToCallable implements AudioPlayerCallable<Void> {
        private int milliSeconds;

        public  SeekToCallable(int milliSeconds){
            this.milliSeconds = milliSeconds;
        }

        @Override
        public Void call(AudioPlayerUtil audioPlayerUtil) {
            audioPlayerUtil.seek(milliSeconds);
            return null;
        }
    }

    public static class SetLoopingCallable implements AudioPlayerCallable<Void> {
        private boolean shouldLoop;

        public  SetLoopingCallable(boolean shouldLoop){
            this.shouldLoop = shouldLoop;
        }

        @Override
        public Void call(AudioPlayerUtil audioPlayerUtil) {
            audioPlayerUtil.setLooping(shouldLoop);
            return null;
        }
    }

 public static class ResumeAudioPlaybackCallable implements AudioPlayerCallable<Void> {

        @Override
        public Void call(AudioPlayerUtil audioPlayerUtil) {
            audioPlayerUtil.resumeAudioPlayback();
            return null;
        }
    }

}
