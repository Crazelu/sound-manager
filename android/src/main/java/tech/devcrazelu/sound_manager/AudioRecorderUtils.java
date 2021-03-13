package tech.devcrazelu.sound_manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import java.io.IOException;


public class AudioRecorderUtils {

    private MediaRecorder recorder ;
    private static String TAG = "SoundManager";

    AudioRecorderUtils(){this.recorder = new MediaRecorder();}

    public void handlePermissionTask(Context context, Activity activity){
        if(!doesAppHavePermission(context)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.RECORD_AUDIO}, 3);
            }

        }
    }

    public boolean doesAppHavePermission(Context context){
        try{
            String audioRecordingPermission = Manifest.permission.RECORD_AUDIO;
            int audioRecordingPermissionPermissionStatus = context.checkCallingOrSelfPermission(audioRecordingPermission);
            return audioRecordingPermissionPermissionStatus == PackageManager.PERMISSION_GRANTED;
        }catch(Exception e){
            Log.d(TAG, e.toString());
            return false;
        }
    }

    public void recordAudio(Context context, String filePath) {
        //TODO: Provide parameter to allow customization of storage directory
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        String fileName = Environment.getExternalStorageDirectory().getPath() + cacheDir;
        if(filePath == null){

            filePath = "VN_"+System.currentTimeMillis();
        }
        fileName +=  filePath + ".3gp";


        try{
            if(recorder == null){
                recorder = new MediaRecorder();
            }
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(fileName);

            try{
                recorder.prepare();
            }catch(IOException e){
                Log.d(TAG, e.toString());
            }

            recorder.start();
        }catch(Exception e){
            Log.d(TAG, e.toString());
        }

    }




    public void resumeRecordingAudio(){
        try{
            //if recorder is null then no recording was being made
            if(recorder == null) return;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                recorder.resume();
            }
        }catch(Exception e){
            Log.d(TAG, e.toString());}

    }


    public void pauseRecording(){
        try{
            //if recorder is null then no recording was being made
            if(recorder == null) return;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                recorder.pause();
            }
        }catch(Exception e){
            Log.d(TAG, e.toString());}

    }

    public void saveRecording(){
        try{
            recorder.stop();
            recorder.release();
            recorder = null;
        }catch(Exception e){
            Log.d(TAG, e.toString());}

    }
}
