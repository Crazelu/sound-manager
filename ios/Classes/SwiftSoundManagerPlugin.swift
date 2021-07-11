import Flutter
import UIKit

public class SwiftSoundManagerPlugin: NSObject, FlutterPlugin {
    
    var audioRecorderUtil: AudioRecorderUtil?;
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "tech.devcrazelu.sound_manager", binaryMessenger: registrar.messenger())
        let instance = SwiftSoundManagerPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    func setUpUtils(){
        if audioRecorderUtil == nil{
            audioRecorderUtil = AudioRecorderUtil()
        }
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
            
        default:
            result(FlutterMethodNotImplemented)
        }
    }
}
