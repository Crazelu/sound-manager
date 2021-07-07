package tech.devcrazelu.sound_manager;

import android.app.Activity;
import android.content.Context;
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
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** SoundManagerPlugin */
public class SoundManagerPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {

  private MethodChannel channel;
  private Activity activity;
  private Context context;
  private static String TAG = "SoundManager";
  private static String CHANNEL = "sound_manager";


  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), CHANNEL);
    channel.setMethodCallHandler(this);
    context = flutterPluginBinding.getApplicationContext();
  }


  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL);
    channel.setMethodCallHandler(new SoundManagerPlugin());
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result rawResult) {

    final Result result = new MethodResultWrapper(rawResult);

    AudioRecorderUtils audioRecorderUtils = new AudioRecorderUtils();
    AudioPlayerUtils audioPlayerUtils = new AudioPlayerUtils();

    switch (call.method) {

      case "requestPermission":
        runPermissionTask(context, result, activity, audioRecorderUtils);
        break;

      case "recordAudio":
        runRecordAudioTask(context, result, audioRecorderUtils);
        break;

      case "pauseRecording":
        runPauseRecordingTask(result, audioRecorderUtils);
        break;

      case "resumeRecording":
        runResumeAudioRecordingTask(result, audioRecorderUtils);
        break;

      case "saveRecording":
        runSaveRecordingTask(result, audioRecorderUtils);
        break;

      case "playAudioFile":
        boolean isFullPath = call.argument("isFullPath");
        String filePath = call.argument("filePath");

        runPlayAudioFileTask( result, audioPlayerUtils, filePath, context, isFullPath);
        break;

      case "pauseAudioFile":
        runPauseAudioFileTask(result, audioPlayerUtils);
        break;

      case "stopPlayingAudioFile":
         runStopPlayingAudioFileTask(result, audioPlayerUtils);
        break;

      case "seekTo":
        int milliseconds = call.argument("time");
        runSeekToTask(result, audioPlayerUtils, milliseconds);
        break;


      case "setLooping":
        boolean shouldLoop = call.argument("repeatSong");
        runSetLoopingTask(result, audioPlayerUtils, shouldLoop);
        break;


      default:
        result.notImplemented();
    }
  }

  private static class SeekToCallable implements Callable<Void> {

    private AudioPlayerUtils audioPlayerUtils;
    private int milliseconds;

    public  SeekToCallable(AudioPlayerUtils audioPlayerUtils, int milliseconds){
      this.audioPlayerUtils = audioPlayerUtils;
      this.milliseconds = milliseconds;
    }
    @Override
    public Void call() {
      try {
        audioPlayerUtils.seek(milliseconds);
      }catch (Exception e){
     throw e;

      }
      return null;
    }
  }

  private void  runSeekToTask(Result result, AudioPlayerUtils audioPlayerUtils, int milliseconds) {
    try{
      SeekToCallable seekToTask = new SeekToCallable(audioPlayerUtils, milliseconds);
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
    private AudioPlayerUtils audioPlayerUtils;
    private boolean shouldLoop;

    public  SetLoopingCallable(AudioPlayerUtils audioPlayerUtils, boolean shouldLoop){
      this.audioPlayerUtils = audioPlayerUtils;
      this.shouldLoop = shouldLoop;
    }
    @Override
    public Void call() {
      try {
        audioPlayerUtils.setLooping(shouldLoop);
      }catch (Exception e){
       throw e;
      }
      return null;
    }
  }

  private void  runSetLoopingTask(Result result, AudioPlayerUtils audioPlayerUtils, boolean shouldLoop) {
    try{
      SetLoopingCallable setLoopingTask = new SetLoopingCallable(audioPlayerUtils, shouldLoop);
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

    private AudioPlayerUtils audioPlayerUtils;

    public  PauseAudioCallable(AudioPlayerUtils audioPlayerUtils){
      this.audioPlayerUtils = audioPlayerUtils;
    }
    @Override
    public Void call() {
      try {
        audioPlayerUtils.pauseAudio();
      }catch (Exception e){
       throw e;
      }
      return null;
    }
  }

  private void  runPauseAudioFileTask(Result result, AudioPlayerUtils audioPlayerUtils) {
    try{
      PauseAudioCallable pauseAudioFileTask = new PauseAudioCallable(audioPlayerUtils);
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

    private AudioPlayerUtils audioPlayerUtils;

    public  StopPlayingAudioCallable(AudioPlayerUtils audioPlayerUtils){
      this.audioPlayerUtils = audioPlayerUtils;
    }
    @Override
    public Void call() {
      try {
        audioPlayerUtils.stopAudio();
      }catch (Exception e){
        throw e;
      }
      return null;
    }
  }

  private void  runStopPlayingAudioFileTask(Result result, AudioPlayerUtils audioPlayerUtils) {
    try{
      StopPlayingAudioCallable stopPlayingAudioFileTask = new StopPlayingAudioCallable(audioPlayerUtils);
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


  private static class PlayAudioCallable implements Callable<Void> {

    private AudioPlayerUtils audioPlayerUtils;
    private String filePath;
    private boolean isFullPath;
    private Context context;

    public  PlayAudioCallable(AudioPlayerUtils audioPlayerUtils, String filePath, Context context, boolean isFullPath){
      this.audioPlayerUtils = audioPlayerUtils;
      this.filePath = filePath;
      this.context = context;
      this.isFullPath = isFullPath;
    }
    @Override
    public Void call() {
      try {
        audioPlayerUtils.playAudio(filePath, context, isFullPath);
      }catch (Exception e){
       throw e;
      }
      return null;
    }
  }

  private void  runPlayAudioFileTask(Result result, AudioPlayerUtils audioPlayerUtils, String filePath, Context context, boolean isFullPath) {
    try{
      PlayAudioCallable playAudioFileTask = new PlayAudioCallable(audioPlayerUtils, filePath, context, isFullPath);
      FutureTask<Void> futureTask = new FutureTask<>(playAudioFileTask);
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
          result.error(TAG, "AudioPlaybackError", e.toString());
        }
      }}catch(Exception e){
      Log.d(TAG, e.toString());
      result.error(TAG, "AudioPlaybackError error", e.toString());
    }
  }

  private static class SaveRecordingCallable implements Callable<Void> {

   private AudioRecorderUtils audioRecorderUtils;

    public  SaveRecordingCallable( AudioRecorderUtils recorderUtils){
      this.audioRecorderUtils = recorderUtils;
    }
    @Override
    public Void call() {
      try {
        audioRecorderUtils.saveRecording();
      }catch (Exception e){
        throw e;
      }
      return null;
    }
  }

  private void  runSaveRecordingTask(Result result,AudioRecorderUtils audioRecorderUtils) {
    try{
      SaveRecordingCallable saveAudioRecordingTask = new SaveRecordingCallable(audioRecorderUtils);
      FutureTask<Void> futureTask = new FutureTask<>(saveAudioRecordingTask);
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
          result.error(TAG, "SaveAudioRecording error", e.toString());
        }
      }}catch(Exception e){
      Log.d(TAG, e.toString());
      result.error(TAG, "SaveAudioRecording error", e.toString());
    }
  }

  private static class ResumeRecordingCallable implements Callable<Void> {

   private AudioRecorderUtils audioRecorderUtils;

    public  ResumeRecordingCallable(AudioRecorderUtils recorderUtils){
      this.audioRecorderUtils = recorderUtils;
    }
    @Override
    public Void call() {
      try {
        audioRecorderUtils.resumeRecordingAudio();
      }catch (Exception e){

        throw e;
      }
      return null;
    }
  }

  private void  runResumeAudioRecordingTask(Result result, AudioRecorderUtils audioRecorderUtils) {
    try{
      ResumeRecordingCallable resumeAudioRecordingTask = new ResumeRecordingCallable(audioRecorderUtils);
      FutureTask<Void> futureTask = new FutureTask<>(resumeAudioRecordingTask);
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
          result.error(TAG, "ResumeRecordingAudio error", e.toString());
        }
      }}catch(Exception e){
      Log.d(TAG, e.toString());
      result.error(TAG, "ResumeRecordingAudio error", e.toString());
    }
  }

  private static class PauseAudioRecordingCallable implements Callable<Void> {
   private AudioRecorderUtils audioRecorderUtils;

    public  PauseAudioRecordingCallable(AudioRecorderUtils recorderUtils){
      this.audioRecorderUtils = recorderUtils;
    }
    @Override
    public Void call() {
      try {
        audioRecorderUtils.pauseRecording();
      }catch (Exception e){
        throw e;
      }
      return null;
    }
  }

  private void  runPauseRecordingTask(Result result, AudioRecorderUtils audioRecorderUtils) {
    try{
      PauseAudioRecordingCallable pauseAudioTask = new PauseAudioRecordingCallable(audioRecorderUtils);
      FutureTask<Void> futureTask = new FutureTask<>(pauseAudioTask);
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
          result.error(TAG, "PauseRecordingAudio error", e.toString());
        }
      }}catch(Exception e){
      Log.d(TAG, e.toString());
      result.error(TAG, "PauseRecordingAudio error", e.toString());
    }
  }


  private static class RecordAudioCallable implements Callable<Boolean> {
    private Context context;
    private  AudioRecorderUtils audioRecorderUtils;

    public  RecordAudioCallable(Context context,  AudioRecorderUtils recorderUtils){
      this.context = context;
      this.audioRecorderUtils = recorderUtils;
    }
    @Override
    public Boolean call() {

      try {
        if (audioRecorderUtils.doesAppHavePermission(context)) {
          audioRecorderUtils.recordAudio(context, null);
          return true;
        }
        return false;
      }catch(Exception e){
        throw e;
      }

    }
  }




  private void  runRecordAudioTask(Context context, Result result, AudioRecorderUtils audioRecorderUtils) {
    try{
      RecordAudioCallable recordAudioTask = new RecordAudioCallable(context, audioRecorderUtils);
      FutureTask<Boolean> futureTask = new FutureTask<>(recordAudioTask);
      ExecutorService executor = Executors.newSingleThreadExecutor();
      executor.submit(futureTask);

      while(true){
        try{
          if(futureTask.isDone()){
            if(futureTask.get() == true) result.success(true);
            else result.error(TAG,"An error occurred. Verify that you have requested permission", null);
          }
        }catch (Exception e){
          Log.d(TAG, e.toString());
          result.error(TAG, "RecordAudio error", e.toString());
        }
      }}catch(Exception e){
      Log.d(TAG, e.toString());
      result.error(TAG, "RecordAudio error", e.toString());
    }
  }

  private static class PermissionCallable implements Callable<Void> {
    private Context context;
    private Activity activity;
    private AudioRecorderUtils audioRecorderUtils;

    public  PermissionCallable(Context context, Activity activity, AudioRecorderUtils audioRecorderUtils){
      this.context = context;
      this.activity = activity;
      this.audioRecorderUtils = audioRecorderUtils;
    }
    @Override
    public Void call() {
        try {
          audioRecorderUtils.handlePermissionTask(context, activity);
          return null;
        }catch(Exception e){
          throw e;
        }

    }
  }




  private void  runPermissionTask(Context context,Result result,Activity activity, AudioRecorderUtils audioRecorderUtils) {
    try{
      PermissionCallable permissionTask = new PermissionCallable(context, activity, audioRecorderUtils);

      FutureTask<Void> futureTask = new FutureTask<>(permissionTask);

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
          result.error(TAG, "Permission error", e.toString());
        }
      }}catch(Exception e){
      Log.d(TAG, e.toString());
      result.error(TAG, "Permission error", e.toString());
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
    activity = binding.getActivity();
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    // TODO("Not yet implemented")
  }
}
