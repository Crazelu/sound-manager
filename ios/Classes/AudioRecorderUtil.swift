//
//  AudioRecorderUtil.swift
//  sound_manager
//
//  Created by Crazelu on 7/11/21.
//

import AVFoundation
import Flutter

public class AudioRecorderUtil: NSObject, AVAudioRecorderDelegate{
    
    private var isRecording: Bool = false;
    private var recorder: AVAudioRecorder?
    private var audioFileName: String!
    
    private func setUpRecorder(audioFormat: AudioFormatID?, bitRate: Int, samplingRate: Float, url: URL) -> Bool{
        
        var isRecoderSet = false
        
        let settings = [
            AVFormatIDKey: audioFormat == nil ? kAudioFormatAppleLossless : audioFormat!,
            AVEncoderAudioQualityKey: AVAudioQuality.max.rawValue,
            AVEncoderBitRateKey: bitRate,
            AVNumberOfChannelsKey: 2,
            AVSampleRateKey: samplingRate
        ] as [String : Any]
        
        do{
            recorder = try AVAudioRecorder(url: url, settings: settings )
            recorder?.delegate = self
            isRecoderSet = true
        }catch {
            //do something with error
            print("Error encountered while setting up recorder")
            finishRecording()
        }
        return isRecoderSet
        
    }
    
    private func getTimeInMilliseconds() -> Int{
        
        let formatter = DateFormatter()
        
        formatter.dateStyle = .long
        formatter.timeStyle = .medium
        formatter.timeZone = TimeZone.current
        
        let current = formatter.date(from: formatter.string(from: Date()))
        
        return Int(current!.timeIntervalSince1970)
        
    }
    
    private func getFileUrl(fileName: String?) -> URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        let documentPath = paths[0]
        var audioFilePath = ""
        
        if let file = fileName{
            audioFilePath += file
        }else{
            audioFilePath += "VN_\(getTimeInMilliseconds())"
        }
        
        let url: URL = documentPath.appendingPathComponent(audioFilePath + ".m4a")
        
        print(url.path)
        
        audioFileName = url.absoluteString
        
        return url
    }
    
    private func getAudioFormat(format: Int?) -> AudioFormatID{
        switch format {
        case 1:
            return kAudioFormatFLAC
        case 2:
            return kAudioFormatAMR
        case 3:
            return kAudioFormatOpus
        case 4:
            return kAudioFormatAudible
        case 5:
            return kAudioFormatiLBC
        case 6:
            return kAudioFormatQUALCOMM
        case 7:
            return kAudioFormatAC3
        case 8:
            return kAudioFormatAES3
        case 9:
            return kAudioFormatAMR_WB
        case 10:
            return kAudioFormatMPEG4AAC
        default:
            return kAudioFormatAppleLossless
        }
    }
    
    public func requestPermission() -> Bool{
        var granted: Bool = false;
        // Asking user permission for accessing Microphone
        AVAudioSession.sharedInstance().requestRecordPermission () {
            allowed in
            if allowed {
                // Microphone allowed
                print("Allowed")
                granted = true
            } else {
                // User denied microphone.
                print("Permission denied")
            }
        }
        return granted
    }
    
    public func finishRecording(){
        recorder?.stop()
        recorder = nil
        isRecording = false
        audioFileName = ""
        do {
            try AVAudioSession.sharedInstance().setActive(false)
        } catch  {
            print("Error setting AVAudioSession as inactive")
        }
       
    }
    
    public func recordAudio(fileName: String?, audioFormat: Int?, bitRate: Int, samplingRate: Float, result: @escaping FlutterResult){
        
        finishRecording()
        
        do {
            
            let options: AVAudioSession.CategoryOptions = [.defaultToSpeaker, .allowBluetooth]
            
            try AVAudioSession.sharedInstance().setCategory(AVAudioSession.Category.playAndRecord, options: options)
            try AVAudioSession.sharedInstance().setActive(true)
            
            if !isRecording{
                
              if  setUpRecorder(audioFormat: getAudioFormat(format: audioFormat), bitRate: bitRate, samplingRate: samplingRate, url: getFileUrl(fileName: fileName))
              {
                recorder?.record()
                isRecording = true
                print("Started recording")
                result(nil)
              }
              
            }
            
            result(FlutterError(code: "-1", message: "Couldn't start audio recording", details: nil))
            
           
        } catch  {
            result(FlutterError(code: "-2", message: "Couldn't start audio recording", details: nil))
        }
        
        
    }
    
    public func pauseRecording(result: @escaping FlutterResult){
        
        if isRecording{
            recorder?.pause()
            print("Paused recording")
        }
        result(nil)
    }
    
    public func resumeRecording(result: @escaping FlutterResult){
        
        if isRecording{
            recorder?.record()
            print("Resumed recording")
        }
        result(nil)
    }
    
    public func cancelRecording(result: @escaping FlutterResult){
        var deleted: Bool = false;
        if isRecording{
            recorder?.stop()
            deleted = recorder!.deleteRecording()
            finishRecording()
            print("Cancelled recording")
        }
        result(deleted)
    }
    
    public func saveRecording(result: @escaping FlutterResult){
        
        if isRecording{
            let savedFile = audioFileName
            finishRecording()
            print("Saved recording")
            result(savedFile)
        }
        result("")
    }
    
}
