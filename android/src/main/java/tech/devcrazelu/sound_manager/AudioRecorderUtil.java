package tech.devcrazelu.sound_manager;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.nfc.Tag;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import java.io.File;
import java.io.IOException;
import io.flutter.plugin.common.MethodChannel.Result;


public class AudioRecorderUtil {

    private MediaRecorder recorder ;
    private static final String TAG = "SoundManager";
    public static final int PERMISSION_REQUEST_CODE = 3;
    private String audioRecordingFilePath = "";
    private boolean isRecording = false;

    /**
     * @param activity
     * Requests permission to record audio.
     */
    public void handlePermissionTask(Activity activity){
        if(!doesAppHavePermission(activity)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        PackageManager.PERMISSION_GRANTED);
            }
        }
    }

    /**
     * @param activity
     * @return true if Manifest.permission.RECORD_AUDIO is granted
     * otherwise, false.
     */
    public boolean doesAppHavePermission(Activity activity) {
        try{
            int audioRecordingPermissionPermissionStatus = ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
            return audioRecordingPermissionPermissionStatus == PackageManager.PERMISSION_GRANTED;
        }catch(Exception e){
            Log.d(TAG, e.toString());
            return false;
        }
    }

    /**
     * @param outputFormat output format of the file to be generated during audio recording.
     * @return file extension for given outputFormat.
     */
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

   private String getFullPath(String fileName, String dir){
        String path = Environment.getExternalStorageDirectory().toString();

       if (fileName == null) {
           fileName = "VN_" + System.currentTimeMillis();
       }

        if(dir == null){
            return path + "/" + fileName ;
        }

        if(dir.startsWith("/")){
            path = path + dir;
        }
        else{
            path = path + "/" + dir;
        }

        if(dir.endsWith("/")){
            path = path + fileName;
        }
        else{
            path = path + "/" + fileName;
        }
        return path;
   }

    /**
     * @param fileName name to save the output file as
     * @param audioSource audio source to use for recording
     * @param outputFormat output format of the output file generated during recording
     * @param audioEncoder audio encoder to use for recording
     * Sets output file name, audio source, output format and audio encoder to be used for recording.
     * Starts recording.
     */
    public void recordAudio(String fileName, String dir, int audioSource, int outputFormat, int audioEncoder, Result result) {

        audioRecordingFilePath = "";

        if (!isRecording) {
            resetRecorder();

            this.audioRecordingFilePath = getFullPath(fileName, dir) + getFileExtension(outputFormat);
            File file = new File(audioRecordingFilePath);

            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.d(TAG, e.toString());
                result.error(TAG, "Couldn't create file to store audio recording. Have you called SoundManager.init()?", null);
            }
            try {
                if (recorder == null) {
                    recorder = new MediaRecorder();
                }
                recorder.setAudioSource(audioSource);
                recorder.setOutputFormat(outputFormat);
                recorder.setAudioEncoder(audioEncoder);
                recorder.setOutputFile(file.getAbsolutePath());

                try {
                    recorder.prepare();
                } catch (IOException e) {
                    Log.d(TAG, e.toString());
                    result.error(TAG, "Couldn't start audio recording. Please report this issue", null);
                }

                isRecording = true;
                recorder.start();
                Log.d(TAG, "Recording started. Audio file to be saved at " + audioRecordingFilePath);
                result.success(null);
            } catch (Exception e) {
                resetRecorder();
                Log.d(TAG, e.toString());
                result.error(TAG, "Couldn't start audio recording. Please report this issue.", null);

            }
        }
    }

    /**
     * Resumes recording audio.
     * It does nothing if recordAudio() is not called first and if audio recording is not paused.
     */
    public void resumeRecordingAudio(Result result) {
        //if recorder is null then no recording was being made
        if (recorder != null && isRecording) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        recorder.resume();
                        Log.d(TAG, "Recording resumed");
                    }
                    else{
                        Log.d(TAG, "resumeRecording() is only available on devices running on Android API 24 and higher");
                    }

                    result.success(null);
                } catch (IllegalStateException e) {
                    Log.d(TAG, e.toString());
                    result.error(TAG, "Couldn't resume audio recording. Please report this issue.", null);
                }
        }
    }


    /**
     * Pauses current audio recording session.
     * It does nothing if recordAudio() is not called first.
     */
    public void pauseRecording(Result result) {
        //if recorder is null then no recording was being made
        if (recorder != null && isRecording) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        recorder.pause();
                        Log.d(TAG, "Recording paused");
                    }
                    else{
                        Log.d(TAG, "pauseRecording() is only available on devices running on Android API 24 and higher");
                    }
                    result.success(null);

                } catch (IllegalStateException e) {
                    Log.d(TAG, e.toString());
                    result.error(TAG, "Couldn't pause audio recording. Please report this issue.", null);
                }
        }
    }

    /**
     * Stops current audio recording session and calls resetRecorder() to release resources.
     * It does nothing if recordAudio() is not called first.
     */
    public void saveRecording(Result result) {
        try {
            //if recorder is null then no recording was being made
            if (recorder != null && isRecording) {
                recorder.stop();
                resetRecorder();
                Log.d(TAG, "Saved recording and released resources");
                result.success(audioRecordingFilePath);
            }
        } catch (IllegalStateException e) {
            Log.d(TAG, e.toString());
            result.error(TAG, "Couldn't save audio recording. Please report this issue.", null);
        }
    }

    /**
     * Cancels current audio recording and deletes output
     * file generated from current audio recording.
     *
     * Resets MediaRecorder.
     *
     * It does nothing if recordAudio() is not called first.
     */
    public void cancelRecording(Result result) {
        if (isRecording && recorder != null) {
            try {
                File file = new File(audioRecordingFilePath);

                if (file.exists()) {
                    recorder.stop();
                    resetRecorder();
                    if (file.delete()) {
                        Log.d(TAG, "Cancelled recording");
                        result.success(true);
                        return;
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        }
        result.success(false);
    }

    /**
     * Resets MediaRecorder and releases resources.
     */
    public void resetRecorder(){
        if(recorder != null){
            recorder.reset();
            recorder.release();
            recorder = null;
            isRecording = false;
        }
    }
}
