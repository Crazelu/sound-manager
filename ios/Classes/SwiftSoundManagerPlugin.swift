import Flutter
import UIKit

public class SwiftSoundManagerPlugin: NSObject, FlutterPlugin {
    
    var audioRecorderUtil: AudioRecorderUtil?
    var audioPlayerUtil: AudioPlayerUtil?
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "tech.devcrazelu.sound_manager", binaryMessenger: registrar.messenger())
        let instance = SwiftSoundManagerPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
   private func setUpUtils(){
        if audioRecorderUtil == nil{
            audioRecorderUtil = AudioRecorderUtil()
        }
        if audioPlayerUtil == nil{
            audioPlayerUtil = AudioPlayerUtil()
        }
    }
    
    private func releaseResources(){
        audioPlayerUtil?.finishPlaying()
        audioRecorderUtil?.finishRecording()
        audioRecorderUtil = nil
        audioPlayerUtil = nil
    }
    
    public func applicationWillTerminate(_ application: UIApplication) {
        releaseResources()
    }
    
    public func applicationDidEnterBackground(_ application: UIApplication) {
        releaseResources()
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        
        setUpUtils()
        
        switch call.method {
        
        case "requestPermission":
            result(audioRecorderUtil?.requestPermission())
            
        case "recordAudio":
            let args = call.arguments as! [String : Any]
            
            audioRecorderUtil?.recordAudio(
                fileName: args["fileName"] as? String,
                audioFormat: args["audioFormat"] as? Int,
                bitRate: args["bitRate"] as? Int ?? 320000,
                samplingRate: args["samplingRate"] as? Float ?? 41000.0,
                result: result
            )
            break
            
        case "pauseRecording":
            audioRecorderUtil?.pauseRecording(result: result)
            break;
            
        case "resumeRecording":
            audioRecorderUtil?.resumeRecording(result: result)
            
        case "cancelRecording":
            audioRecorderUtil?.cancelRecording(result: result)
            
        case "saveRecording":
            audioRecorderUtil?.saveRecording(result: result)
            
        case "playAudioFile":
            let args = call.arguments as! [String: String]
            audioPlayerUtil?.play(url: args["filePath"]!, result: result)
            
        case "pauseAudioPlayback":
            audioPlayerUtil?.pause(result: result)
            
        case "resumeAudioPlayback":
            audioPlayerUtil?.resume(result: result)
            
        case "stopPlayingAudio":
            audioPlayerUtil?.stop(result: result)
            
        case "setLooping":
            let args = call.arguments as! [String: Bool]
            audioPlayerUtil?.loop(shouldLoop: args["looping"] ?? false, result: result)
            
        default:
            result(FlutterMethodNotImplemented)
        }
    }
}
