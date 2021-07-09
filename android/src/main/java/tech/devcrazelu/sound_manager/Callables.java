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
