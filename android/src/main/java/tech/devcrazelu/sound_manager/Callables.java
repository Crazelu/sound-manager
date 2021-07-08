package tech.devcrazelu.sound_manager;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.IOException;

interface AudioRecorderCallable<T>{
     T call(Context context, @Nullable Activity activity, AudioRecorderUtils audioRecorderUtils) throws Exception;
}interface AudioPlayerCallable<T>{
     T call(Context context, @Nullable Activity activity,AudioPlayerUtils audioPlayerUtils) throws Exception;
}

public class Callables {

    public static class PermissionCallable implements AudioRecorderCallable<Void> {

        @Override
        public Void call(@NonNull Context context,@NonNull Activity activity,@NonNull AudioRecorderUtils audioRecorderUtils) {

                audioRecorderUtils.handlePermissionTask(context, activity);
                return null;

        }
    }

    public static class RecordAudioCallable implements AudioRecorderCallable<Boolean> {

        @Override
        public Boolean call(@NonNull Context context, @Nullable Activity activity,@NonNull AudioRecorderUtils audioRecorderUtils) throws Exception {

                if (audioRecorderUtils.doesAppHavePermission(context)) {
                    audioRecorderUtils.recordAudio(context, null);
                    return true;
                }
                return false;


        }
    }

    public static class PauseAudioRecordingCallable implements AudioRecorderCallable<Void> {

        @Override
        public Void call(@Nullable Context context, @Nullable Activity activity,@NonNull AudioRecorderUtils audioRecorderUtils) {

                audioRecorderUtils.pauseRecording();

            return null;
        }
    }

    public static class ResumeRecordingCallable implements AudioRecorderCallable<Void> {

        @Override
        public Void call(@Nullable Context context, @Nullable Activity activity, AudioRecorderUtils audioRecorderUtils) {

                audioRecorderUtils.resumeRecordingAudio();

            return null;
        }
    }

    public static class SaveRecordingCallable implements AudioRecorderCallable<Void> {

        @Override
        public Void call(@Nullable Context context, @Nullable Activity activity, AudioRecorderUtils audioRecorderUtils){

                audioRecorderUtils.saveRecording();
            return null;
        }
    }

    public static class PlayAudioCallable implements AudioPlayerCallable<Void> {
        private final String filePath;
        private final Boolean isFullPath;

        public PlayAudioCallable(String filePath, Boolean isFullPath){
            this.filePath = filePath;
            this.isFullPath = isFullPath != null && isFullPath;
        }


        @Override
        public Void call(Context context, @Nullable Activity activity, AudioPlayerUtils audioPlayerUtils) throws Exception {

                audioPlayerUtils.playAudio(filePath, context, isFullPath);

            return null;
        }
    }
}
