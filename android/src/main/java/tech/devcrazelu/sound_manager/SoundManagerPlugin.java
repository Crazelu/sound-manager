package tech.devcrazelu.sound_manager;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

/** SoundManagerPlugin */
public class SoundManagerPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {

  private MethodChannel channel;
  private Activity activity;
  private Context context;
  private AudioRecorderUtil audioRecorderUtil;
  private AudioPlayerUtil audioPlayerUtil;


  private static final String TAG = "SoundManager";
  private static final String CHANNEL = "sound_manager";
  private static final String REQUEST_PERMISSION = "requestPermission";
  private static final String RECORD_AUDIO = "recordAudio";
  private static final String PAUSE_RECORDING = "pauseRecording";
  private static final String RESUMING_RECORDING = "resumeRecording";
  private static final String SAVE_RECORDING = "saveRecording";
  private static final String CANCEL_RECORDING = "cancelRecording";
  private static final String PLAY_AUDIO = "playAudioFile";
  private static final String PAUSE_AUDIO = "pauseAudioPlayback";
  private static final String STOP_PLAYING_AUDIO = "stopPlayingAudio";
  private static final String SEEK_TO = "seekTo";
  private static final String SET_LOOPING = "setLooping";


  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), CHANNEL);
    channel.setMethodCallHandler(this);
    context = flutterPluginBinding.getApplicationContext();
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


  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result rawResult) {

    final Result result = new MethodResultWrapper(rawResult);

    setupUtils();

    switch (call.method) {

      case REQUEST_PERMISSION:
        new SoundManagerPluginUtils.Task(context,activity, result, new Callables.PermissionCallable(), audioRecorderUtil).execute();
        break;

      case RECORD_AUDIO:
        new SoundManagerPluginUtils.Task(context,null, result, new Callables.RecordAudioCallable(), audioRecorderUtil).execute();
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

      case PLAY_AUDIO:
        boolean isFullPath = call.argument("isFullPath");
        String filePath = call.argument("filePath");

        new SoundManagerPluginUtils.AudioPlayerTask(context,null, result, new Callables.PlayAudioCallable(filePath, isFullPath), audioPlayerUtil).execute();
        break;

      case PAUSE_AUDIO:
        runPauseAudioFileTask(result, audioPlayerUtil);
        break;

      case STOP_PLAYING_AUDIO:
         runStopPlayingAudioFileTask(result, audioPlayerUtil);
        break;

      case SEEK_TO:
        int milliseconds = call.argument("time");
        runSeekToTask(result, audioPlayerUtil, milliseconds);
        break;


      case SET_LOOPING:
        boolean shouldLoop = call.argument("repeatSong");
        runSetLoopingTask(result, audioPlayerUtil, shouldLoop);
        break;


      default:
        result.notImplemented();
    }
  }

  private static class SeekToCallable implements Callable<Void> {

    private AudioPlayerUtil audioPlayerUtil;
    private int milliseconds;

    public  SeekToCallable(AudioPlayerUtil audioPlayerUtil, int milliseconds){
      this.audioPlayerUtil = audioPlayerUtil;
      this.milliseconds = milliseconds;
    }
    @Override
    public Void call() {
      try {
        audioPlayerUtil.seek(milliseconds);
      }catch (Exception e){
     throw e;

      }
      return null;
    }
  }

  private void  runSeekToTask(Result result, AudioPlayerUtil audioPlayerUtil, int milliseconds) {
    try{
      SeekToCallable seekToTask = new SeekToCallable(audioPlayerUtil, milliseconds);
      FutureTask<Void> futureTask = new FutureTask<>(seekToTask);
      ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.submit(futureTask);

      while(true){
        try{
          if(futureTask.isDone()){
            futureTask.get();
            result.success(true);
          }
        }catch (Exception e){
          Log.d(TAG, e.toString());
          result.error(TAG, "AudioPlayerSeekError", e.toString());
        }
      }}catch(Exception e){
      Log.d(TAG, e.toString());
      result.error(TAG, "AudioPlayerSeekError", e.toString());
    }
  }


  private static class SetLoopingCallable implements Callable<Void> {
    private AudioPlayerUtil audioPlayerUtil;
    private boolean shouldLoop;

    public  SetLoopingCallable(AudioPlayerUtil audioPlayerUtil, boolean shouldLoop){
      this.audioPlayerUtil = audioPlayerUtil;
      this.shouldLoop = shouldLoop;
    }
    @Override
    public Void call() {
      try {
        audioPlayerUtil.setLooping(shouldLoop);
      }catch (Exception e){
       throw e;
      }
      return null;
    }
  }

  private void  runSetLoopingTask(Result result, AudioPlayerUtil audioPlayerUtil, boolean shouldLoop) {
    try{
      SetLoopingCallable setLoopingTask = new SetLoopingCallable(audioPlayerUtil, shouldLoop);
      FutureTask<Void> futureTask = new FutureTask<>(setLoopingTask);
      ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.submit(futureTask);

      while(true){
        try{
          if(futureTask.isDone()){
            futureTask.get();
            result.success(true);
          }
        }catch (Exception e){
          Log.d(TAG, e.toString());
          result.error(TAG, "AudioPlayerLoopingError", e.toString());
        }
      }}catch(Exception e){
      Log.d(TAG, e.toString());
      result.error(TAG, "AudioPlayerLoopingError", e.toString());
    }
  }


  private static class PauseAudioCallable implements Callable<Void> {

    private AudioPlayerUtil audioPlayerUtil;

    public  PauseAudioCallable(AudioPlayerUtil audioPlayerUtil){
      this.audioPlayerUtil = audioPlayerUtil;
    }
    @Override
    public Void call() {
      try {
        audioPlayerUtil.pauseAudio();
      }catch (Exception e){
       throw e;
      }
      return null;
    }
  }

  private void  runPauseAudioFileTask(Result result, AudioPlayerUtil audioPlayerUtil) {
    try{
      PauseAudioCallable pauseAudioFileTask = new PauseAudioCallable(audioPlayerUtil);
      FutureTask<Void> futureTask = new FutureTask<>(pauseAudioFileTask);
      ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.submit(futureTask);

      while(true){
        try{
          if(futureTask.isDone()){
            futureTask.get();
            result.success(true);
          }
        }catch (Exception e){
          Log.d(TAG, e.toString());
          result.error(TAG, "AudioPlayerPauseError", e.toString());
        }
      }}catch(Exception e){
      Log.d(TAG, e.toString());
      result.error(TAG, "AudioPlayerPauseError", e.toString());
    }
  }


  private static class StopPlayingAudioCallable implements Callable<Void> {

    private AudioPlayerUtil audioPlayerUtil;

    public  StopPlayingAudioCallable(AudioPlayerUtil audioPlayerUtil){
      this.audioPlayerUtil = audioPlayerUtil;
    }
    @Override
    public Void call() {
      try {
        audioPlayerUtil.stopAudio();
      }catch (Exception e){
        throw e;
      }
      return null;
    }
  }

  private void  runStopPlayingAudioFileTask(Result result, AudioPlayerUtil audioPlayerUtil) {
    try{
      StopPlayingAudioCallable stopPlayingAudioFileTask = new StopPlayingAudioCallable(audioPlayerUtil);
      FutureTask<Void> futureTask = new FutureTask<>(stopPlayingAudioFileTask);
      ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.submit(futureTask);

      while(true){
        try{
          if(futureTask.isDone()){
            futureTask.get();
            result.success(true);
          }
        }catch (Exception e){
          Log.d(TAG, e.toString());
          result.error(TAG, "AudioPlayerStopError", e.toString());

        }
      }}catch(Exception e){
      Log.d(TAG, e.toString());
      result.error(TAG, "AudioPlayerStopError", e.toString());
    }
  }





  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
    context = null;
    activity = null;
  }

  @Override
  public void onDetachedFromActivity() {
    //TODO("Not yet implemented")
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    binding.addRequestPermissionsResultListener(
            new PluginRegistry.RequestPermissionsResultListener() {
              @Override
              public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

                switch (requestCode) {
                  case AudioRecorderUtil.PERMISSION_REQUEST_CODE:
                    for (int i = 0; i < grantResults.length; i++) {
                      if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "Permission granted");
                      }
                    }

                }
                return false;
              }
            });
    activity = binding.getActivity();
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    binding.addRequestPermissionsResultListener(
            new PluginRegistry.RequestPermissionsResultListener() {
      @Override
      public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
          case AudioRecorderUtil.PERMISSION_REQUEST_CODE:
            for (int i = 0; i < grantResults.length; i++) {
              if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission granted");
              }
            }

        }
        return false;
      }
    });
    activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    // TODO("Not yet implemented")
  }
}
