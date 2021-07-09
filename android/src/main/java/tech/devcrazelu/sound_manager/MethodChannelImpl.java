package tech.devcrazelu.sound_manager;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;


public class MethodChannelImpl implements MethodCallHandler, PluginRegistry.RequestPermissionsResultListener{

    private static final String REQUEST_PERMISSION = "requestPermission";
    private static final String RECORD_AUDIO = "recordAudio";
    private static final String PAUSE_RECORDING = "pauseRecording";
    private static final String RESUMING_RECORDING = "resumeRecording";
    private static final String SAVE_RECORDING = "saveRecording";
    private static final String CANCEL_RECORDING = "cancelRecording";
    private static final String PLAY_AUDIO = "playAudioFile";
    private static final String PAUSE_AUDIO = "pauseAudioPlayback";
    private static final String RESUME_AUDIO_PLAYBACK = "resumeAudioPlayback";
    private static final String STOP_PLAYING_AUDIO = "stopPlayingAudio";
    private static final String SEEK_TO = "seekTo";
    private static final String SET_LOOPING = "setLooping";
    private static final String TAG = "SoundManager";

    private static Activity activity;
    private  AudioRecorderUtil audioRecorderUtil;
    private  AudioPlayerUtil audioPlayerUtil;
    private static Result permResult;

    MethodChannelImpl(Activity activity ){
        this.activity = activity;
    }

    ///Initializes audioRecorderUtil and audioPlayerUtil
    void setupUtils(){
        if(audioRecorderUtil == null){
            audioRecorderUtil = new AudioRecorderUtil();
        }

        if(audioPlayerUtil == null){
            audioPlayerUtil = new AudioPlayerUtil();
        }
    }

    void close(){
        audioRecorderUtil.resetRecorder();
        audioPlayerUtil.resetPlayer();
        audioRecorderUtil = null;
        audioPlayerUtil = null;
    }


    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result rawResult) {

        setupUtils();
        final Result result = new MethodResultWrapper(rawResult);
        permResult = result;

        switch (call.method) {

            case REQUEST_PERMISSION:
                audioRecorderUtil.handlePermissionTask(activity);
                break;

            case RECORD_AUDIO:
                String fileName = call.argument("fileName");
                String directory = call.argument("directory");
                int audioSource =  call.argument("audioSource");
                int outputFormat = call.argument("outputFormat");
                int audioEncoder = call.argument("audioEncoder");
                audioRecorderUtil.recordAudio(fileName, directory, audioSource, outputFormat, audioEncoder, result);
                break;

            case PAUSE_RECORDING:
                audioRecorderUtil.pauseRecording(result);
                break;

            case RESUMING_RECORDING:
                audioRecorderUtil.resumeRecordingAudio(result);
                break;

            case SAVE_RECORDING:
                audioRecorderUtil.saveRecording(result);
                break;

            case CANCEL_RECORDING:
                audioRecorderUtil.cancelRecording(result);
                break;

            case PLAY_AUDIO:
                String audioFilePath = call.argument("filePath");

                new SoundManagerPluginUtils.AudioPlayerTask(result, new Callables.PlayAudioCallable(audioFilePath), audioPlayerUtil).execute();
                break;

            case PAUSE_AUDIO:
                new SoundManagerPluginUtils.AudioPlayerTask(result, new Callables.PauseAudioPlaybackCallable(), audioPlayerUtil).execute();
                break;

            case STOP_PLAYING_AUDIO:
                new SoundManagerPluginUtils.AudioPlayerTask(result, new Callables.StopPlayingAudioCallable(), audioPlayerUtil).execute();
                break;

            case SEEK_TO:
                int milliSeconds = call.argument("milliSeconds");
                new SoundManagerPluginUtils.AudioPlayerTask(result, new Callables.SeekToCallable(milliSeconds), audioPlayerUtil).execute();
                break;

            case SET_LOOPING:
                boolean shouldLoop = call.argument("looping");
                new SoundManagerPluginUtils.AudioPlayerTask(result, new Callables.SetLoopingCallable(shouldLoop), audioPlayerUtil).execute();
                break;
            case RESUME_AUDIO_PLAYBACK:
                new SoundManagerPluginUtils.AudioPlayerTask(result, new Callables.ResumeAudioPlaybackCallable(), audioPlayerUtil).execute();
                break;

            default:
                result.notImplemented();
        }
    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == AudioRecorderUtil.PERMISSION_REQUEST_CODE) {
            if (permResult != null) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permResult.success(true);
                } else {
                    permResult.error(TAG, "Permission denied", null);
                }
                permResult = null;
                return true;
            }
        }
        return false;
    }
}
