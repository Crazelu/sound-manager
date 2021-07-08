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

import java.io.File;
import java.io.IOException;


public class AudioRecorderUtil {

    private MediaRecorder recorder ;
    private static final String TAG = "SoundManager";
    public static final int PERMISSION_REQUEST_CODE = 3;
    private String audioRecordingFilePath = "";
    private boolean isRecording = false;

    public void handlePermissionTask(Context context, Activity activity){
        if(!doesAppHavePermission(context)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
            }

        }
    }

    public boolean doesAppHavePermission(Context context) {
        try{
            String audioRecordingPermission = Manifest.permission.RECORD_AUDIO;
            int audioRecordingPermissionPermissionStatus = context.checkCallingOrSelfPermission(audioRecordingPermission);
            return audioRecordingPermissionPermissionStatus == PackageManager.PERMISSION_GRANTED;
        }catch(Exception e){
            Log.d(TAG, e.toString());
            return false;
        }
    }

    private String getFileExtension(int outputFormat){
        switch (outputFormat){
            case 2:
                return ".mp4";
            case 3:
            case 4:
                return ".amr";
            case 6:
                return ".aac";
            case 9:
                return ".webm";
            case 11:
                return ".ogg";
            default:
                return ".3gp";
        }
    }

    public void recordAudio(String filePath, int audioSource, int outputFormat, int audioEncoder) throws Exception {

        //TODO: Provide parameter to allow customization of storage directory


        if(filePath == null){

            filePath = "VN_"+System.currentTimeMillis();
        }
        this.audioRecordingFilePath = Environment.getExternalStorageDirectory() + "/" + filePath + getFileExtension(outputFormat);

        File file = new File(audioRecordingFilePath);
        file.createNewFile();


        try{
            if(recorder == null){
                this.recorder = new MediaRecorder();
            }
            recorder.setAudioSource(audioSource);
            recorder.setOutputFormat(outputFormat);
            recorder.setAudioEncoder(audioEncoder);
            recorder.setOutputFile(file.getAbsolutePath());

            try{
                recorder.prepare();
            }catch(IOException e){
                Log.d(TAG, e.toString());
                throw e;
            }

            isRecording = true;
            recorder.start();
            Log.d(TAG, "Recording started. Audio file to be saved at "+audioRecordingFilePath);
        }catch(Exception e){
            isRecording = false;
            Log.d(TAG, e.toString());
            throw e;
        }

    }




    public void resumeRecordingAudio(){

            //if recorder is null then no recording was being made
            if(recorder == null) return;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try{
                    recorder.resume();

                }catch(Exception e){
                    Log.d(TAG, e.toString());
                    throw e;
                }
            }


    }


    public void pauseRecording(){

            //if recorder is null then no recording was being made
            if(recorder == null) return;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try{
                    recorder.pause();
                Log.d(TAG, "Recording paused");
                }catch(Exception e){
                    Log.d(TAG, e.toString());
                    throw e;
                }
            }


    }

    public void saveRecording(){
        try{
            //if recorder is null then no recording was being made
            if(recorder == null) return;

            recorder.stop();
            recorder.release();
            recorder = null;
            isRecording = false;
            this.audioRecordingFilePath = "";
            Log.d(TAG, "Saved recording and released resources");
        }catch(Exception e){
            Log.d(TAG, e.toString());
            throw e;
        }

    }
}
