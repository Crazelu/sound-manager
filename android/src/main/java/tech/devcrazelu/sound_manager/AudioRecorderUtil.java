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

    /**
     * @param context
     * @param activity
     * Requests permission to record audio.
     */
    public void handlePermissionTask(Context context, Activity activity){
        if(!doesAppHavePermission(context)){
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
     * @param context
     * @return true if Manifest.permission.RECORD_AUDIO is granted
     * otherwise, false.
     */
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

    /**
     * Resets MediaRecorder and releases resources.
     */
    private void resetRecorder(){
        if(recorder != null){
            recorder.reset();
            recorder.release();
            recorder = null;
            isRecording = false;
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
    public boolean cancelRecording() {
        boolean result = false;
        if (isRecording && recorder != null) {
            try {
                File file = new File(audioRecordingFilePath);

                if (file.exists()) {
                    recorder.stop();
                    resetRecorder();
                    if (file.delete()) {
                        result = true;
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        }
        return result;
    }

    /**
     * @param fileName name to save the output file as
     * @param audioSource audio source to use for recording
     * @param outputFormat output format of the output file generated during recording
     * @param audioEncoder audio encoder to use for recording
     * Sets output file name, audio source, output format and audio encoder to be used for recording.
     * Starts recording.
     */
    public void recordAudio(String fileName, int audioSource, int outputFormat, int audioEncoder) throws Exception {

        if (!isRecording) {
            resetRecorder();//TODO: Provide parameter to allow customization of storage directory
            if (fileName == null) {

                fileName = "VN_" + System.currentTimeMillis();
            }
            this.audioRecordingFilePath = Environment.getExternalStorageDirectory() + "/" + fileName + getFileExtension(outputFormat);
            File file = new File(audioRecordingFilePath);
            file.createNewFile();
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
                    throw e;
                }

                isRecording = true;
                recorder.start();
                Log.d(TAG, "Recording started. Audio file to be saved at " + audioRecordingFilePath);
            } catch (Exception e) {
                isRecording = false;
                Log.d(TAG, e.toString());
                throw e;
            }
        }
    }

    /**
     * Resumes recording audio.
     * It does nothing if recordAudio() is not called first and if audio recording is not paused.
     */
    public void resumeRecordingAudio() {
        //if recorder is null then no recording was being made
        if (recorder != null && isRecording) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    recorder.resume();
                    Log.d(TAG, "Recording resumed");
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                    throw e;
                }
            }
        }
    }


    /**
     * Pauses current audio recording session.
     * It does nothing if recordAudio() is not called first.
     */
    public void pauseRecording() {
        //if recorder is null then no recording was being made
        if (recorder != null && isRecording) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    recorder.pause();
                    Log.d(TAG, "Recording paused");
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                    throw e;
                }
            }
        }
    }

    /**
     * Stops current audio recording session and calls resetRecorder() to release resources.
     * It does nothing if recordAudio() is not called first.
     */
    public void saveRecording() {
        try {
            //if recorder is null then no recording was being made
            if (recorder != null && isRecording) {
                recorder.stop();
                resetRecorder();
                this.audioRecordingFilePath = "";
                Log.d(TAG, "Saved recording and released resources");
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            throw e;
        }
    }
}
