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
  private static final String CHANNEL = "tech.devcrazelu.sound_manager";

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
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    setupUtils();
    new MethodChannelImpl(activity, context, call, result, audioRecorderUtil, audioPlayerUtil).run();

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
