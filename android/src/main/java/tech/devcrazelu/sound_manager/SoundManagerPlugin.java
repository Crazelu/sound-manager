package tech.devcrazelu.sound_manager;

import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;

/** SoundManagerPlugin */
public class SoundManagerPlugin implements FlutterPlugin, ActivityAware {

  private MethodChannel channel;
  private MethodChannelImpl handler;
  private FlutterPluginBinding pluginBinding;
  private ActivityPluginBinding activityBinding;

  private static final String CHANNEL = "tech.devcrazelu.sound_manager";

  private void startPlugin(BinaryMessenger messenger, ActivityPluginBinding binding) {

    handler = new MethodChannelImpl(binding.getActivity());
    channel = new MethodChannel(messenger, CHANNEL);
    channel.setMethodCallHandler(handler);

    binding.addRequestPermissionsResultListener(handler);
  }

  private void stopPlugin() {
    activityBinding.removeRequestPermissionsResultListener(handler);
    activityBinding = null;
    channel.setMethodCallHandler(null);
    handler.close();
    handler = null;
    channel = null;
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    pluginBinding = flutterPluginBinding;
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
   pluginBinding = null;
  }

  @Override
  public void onDetachedFromActivity() {
   stopPlugin();
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
   onAttachedToActivity(binding);
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    activityBinding = binding;

    startPlugin(pluginBinding.getBinaryMessenger(), binding);
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
  onDetachedFromActivity();
  }
}
