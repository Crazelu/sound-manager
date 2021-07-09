package tech.devcrazelu.sound_manager;

import io.flutter.plugin.common.MethodChannel.Result;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import androidx.annotation.Nullable;



public class SoundManagerPluginUtils {


    private static final String TAG = "SoundManager";
    private static final String ERROR_TAG = "SoundManagerPluginError";


    public static class Task extends AsyncTask<Void, Void, Void> {
        private final Activity activity;
        private MethodResultWrapper result;
        private AudioRecorderCallable callable;
        private AudioRecorderUtil audioRecorderUtil;

        public  Task(@Nullable Activity activity, Result result, AudioRecorderCallable callable, @Nullable AudioRecorderUtil audioRecorderUtil){

            this.result = new MethodResultWrapper(result);
            this.callable = callable;
            this.activity = activity;
            this.audioRecorderUtil = audioRecorderUtil;
        }

        private Object invoke(AudioRecorderCallable callable) throws Exception {
          return callable.call(activity, audioRecorderUtil);
        }

        protected Void doInBackground(Void... params) {
            try {

                //check if invocation of callable's call method returns a boolean and return that to
                //Flutter instead of the default true value
               Object invocationResult = invoke(callable);

               if(invocationResult != null && invocationResult.getClass() == Boolean.class){
                   result.success(invocationResult);
                }

                result.success(true);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, e.toString());
                result.error(TAG, ERROR_TAG, e.toString());
            }
            return null;
        }
    }

    public static class AudioPlayerTask extends AsyncTask<Void, Void, Void> {

        private MethodResultWrapper result;
        private AudioPlayerCallable callable;
        private AudioPlayerUtil audioPlayerUtil;

        public  AudioPlayerTask(Result result, AudioPlayerCallable callable, @Nullable AudioPlayerUtil audioPlayerUtil){

            this.result = new MethodResultWrapper(result);
            this.callable = callable;
            this.audioPlayerUtil = audioPlayerUtil;
        }

        private void invoke(AudioPlayerCallable callable) throws Exception {
           callable.call(audioPlayerUtil);
        }

        protected Void doInBackground(Void... params) {
            try {
                invoke(callable);
                result.success(true);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, e.toString());
                result.error(TAG, ERROR_TAG, e.toString());
            }
            return null;
        }
    }

}
