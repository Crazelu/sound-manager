package tech.devcrazelu.sound_manager;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.Nullable;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.Result;

public class MethodChannelImpl {

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

    private static Activity activity;
    private static Context context;
    private static AudioRecorderUtil audioRecorderUtil;
    private static AudioPlayerUtil audioPlayerUtil;
    private static MethodCall call;
    private static Result rawResult;

    MethodChannelImpl(Activity activity, Context context, MethodCall call, Result rawResult,
                      AudioRecorderUtil audioRecorderUtil, AudioPlayerUtil audioPlayerUtil
                      ){
        this.activity = activity;
        this.context = context;
        this.call = call;
        this.rawResult = rawResult;
        this.audioRecorderUtil = audioRecorderUtil;
        this.audioPlayerUtil =audioPlayerUtil;
    }


    public static void run(){
        final Result result = new MethodResultWrapper(rawResult);

        switch (call.method) {

            case REQUEST_PERMISSION:
                new SoundManagerPluginUtils.Task(context,activity, result, new Callables.PermissionCallable(), audioRecorderUtil).execute();
                break;

            case RECORD_AUDIO:
                @Nullable String fileName = call.argument("fileName");
                int audioSource =  call.argument("audioSource");
                int outputFormat = call.argument("outputFormat");
                int audioEncoder = call.argument("audioEncoder");
                new SoundManagerPluginUtils.Task(context,null, result, new Callables.RecordAudioCallable(fileName, audioSource, outputFormat, audioEncoder), audioRecorderUtil).execute();
                break;

            case PAUSE_RECORDING:
                new SoundManagerPluginUtils.Task(null,null, result, new Callables.PauseAudioRecordingCallable(), audioRecorderUtil).execute();
                break;

            case RESUMING_RECORDING:
                new SoundManagerPluginUtils.Task(null,null, result, new Callables.ResumeRecordingCallable(), audioRecorderUtil).execute();
                break;

            case SAVE_RECORDING:
                new SoundManagerPluginUtils.Task(null,null, result, new Callables.SaveRecordingCallable(), audioRecorderUtil).execute();
                break;

            case CANCEL_RECORDING:
                new SoundManagerPluginUtils.Task(null,null, result, new Callables.CancelAudioRecordingCallable(), audioRecorderUtil).execute();
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
}
