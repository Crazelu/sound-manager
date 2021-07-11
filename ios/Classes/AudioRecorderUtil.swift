//
//  AudioRecorderUtil.swift
//  sound_manager
//
//  Created by Crazelu on 7/11/21.
//

import Foundation
import AVFoundation

public class AudioRecorderUtil: NSObject, AVAudioRecorderDelegate{
    
    private var isRecording: Bool = false;
    private var recorder: AVAudioRecorder?
    private var audioFileName: String!
    
    private func setUpRecorder(audioFormat: AudioFormatID?, bitRate: Int, samplingRate: Float, url: URL){
        
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
        }catch {
        //do something with error
            print("Error encountered while setting up recorder")
            finishRecording()
        }

    }
    
   private func getTimeInMilliseconds() -> Int{

    let formatter = DateFormatter()

    formatter.dateStyle = .long
    formatter.timeStyle = .medium
    formatter.timeZone = TimeZone.current

    let current = formatter.date(from: formatter.string(from: Date()))

    return Int(current!.timeIntervalSince1970)

    }
    
    private func getFileUrl(fileName: String?, directory: String?) -> URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        let documentPath = paths[0]
        var audioFilePath = ""
        
        if let dir = directory{
            audioFilePath += dir
        }
        
        if let file = fileName{
            audioFilePath += "/\(file)"
        }else{
            audioFilePath += "VN_\(getTimeInMilliseconds())"
        }
        
        audioFilePath += ".m4a"
        
        
        
        return documentPath.appendingPathComponent(audioFilePath)
    }
    
    private func getAudioFormat(format: Int?) -> AudioFormatID{
        switch format {
        case 1:
            return kAudioFormatAMR
        default:
            return kAudioFormatAppleLossless
        }
    }
    
    public func requestPermission() -> Bool{
        var granted:Bool = false;
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
    
    private func finishRecording(){
        recorder?.stop()
        recorder = nil
        isRecording = false
    }
    
    public func recordAudio(fileName: String?, directory: String?, audioFormat: Int?, bitRate: Int, samplingRate: Float, result: @escaping FlutterResult){
        
        if !isRecording{
            
            setUpRecorder(audioFormat: getAudioFormat(format: audioFormat), bitRate: bitRate, samplingRate: samplingRate, url: getFileUrl(fileName: fileName, directory: directory))
            
            recorder?.record()
            isRecording = true
           
        }
        
        result(nil)
    }
    
    public func pauseRecording(result: @escaping FlutterResult){
        
        if isRecording{
            recorder?.pause()
            result(nil)
        }
    }
    
    public func cancelRecording(result: @escaping FlutterResult){
        
        if isRecording{
            recorder?.deleteRecording()
            result(nil)
        }
    }
    
    public func stopRecording(result: @escaping FlutterResult){
        
        if isRecording{
           finishRecording()
            result(nil)
        }
    }
    
}
