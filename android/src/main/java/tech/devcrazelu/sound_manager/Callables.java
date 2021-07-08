package tech.devcrazelu.sound_manager;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.Nullable;
import java.io.IOException;

interface SoundManagerCallable<T>{
    public T call(Context context, @Nullable Activity activity, AudioRecorderUtils audioRecorderUtils) throws Exception;
}

public class Callables {

    public static class PermissionCallable implements SoundManagerCallable<Void> {

        @Override
        public Void call(Context context, Activity activity, AudioRecorderUtils audioRecorderUtils) {
            try {
                audioRecorderUtils.handlePermissionTask(context, activity);
                return null;
            }catch(Exception e){
                throw e;
            }
        }
    }

    public static class RecordAudioCallable implements SoundManagerCallable<Boolean> {

        @Override
        public Boolean call(Context context, @Nullable Activity activity, AudioRecorderUtils audioRecorderUtils) throws IOException {
            try {
                if (audioRecorderUtils.doesAppHavePermission(context)) {
                    audioRecorderUtils.recordAudio(context, null);
                    return true;
                }
                return false;
            }catch(Exception e){

                throw e;
            }

        }
    }
}
