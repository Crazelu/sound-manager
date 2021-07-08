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
        private Context context;
        private Activity activity;
        private Result result;
        private AudioRecorderCallable callable;
        private AudioRecorderUtil audioRecorderUtil;

        public  Task(Context context, @Nullable Activity activity, Result result, AudioRecorderCallable callable, @Nullable AudioRecorderUtil audioRecorderUtil){
            this.context = context;
            this.result = new MethodResultWrapper(result);
            this.callable = callable;
            this.activity = activity;
            this.audioRecorderUtil = audioRecorderUtil;
        }

        private void invoke(AudioRecorderCallable callable) throws Exception {
           callable.call(context,activity, audioRecorderUtil);
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

    public static class AudioPlayerTask extends AsyncTask<Void, Void, Void> {
        private Context context;
        private Activity activity;
        private Result result;
        private AudioPlayerCallable callable;
        private AudioPlayerUtil audioPlayerUtil;

        public  AudioPlayerTask(Context context, @Nullable Activity activity, Result result, AudioPlayerCallable callable, @Nullable AudioPlayerUtil audioPlayerUtil){
            this.context = context;
            this.result = new MethodResultWrapper(result);
            this.callable = callable;
            this.activity = activity;
            this.audioPlayerUtil = audioPlayerUtil;
        }

        private void invoke(AudioPlayerCallable callable) throws Exception {
           callable.call(context,activity, audioPlayerUtil);
        }

        protected Void doInBackground(Void... params) {
            try {
                invoke(callable);
                result.success(true);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                result.error(TAG, ERROR_TAG, e.toString());
            }
            return null;
        }
    }

}
