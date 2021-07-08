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
        private SoundManagerCallable callable;
        private AudioRecorderUtils audioRecorderUtils;

        public  Task(Context context, @Nullable Activity activity, Result result, SoundManagerCallable callable, @Nullable AudioRecorderUtils audioRecorderUtils){
            this.context = context;
            this.result = new MethodResultWrapper(result);
            this.callable = callable;
            this.activity = activity;
            this.audioRecorderUtils = audioRecorderUtils;
        }

        private void invoke(SoundManagerCallable callable) throws Exception {
           callable.call(context,activity,audioRecorderUtils);
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
