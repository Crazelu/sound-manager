//
//  AudioPlayerUtil.swift
//  sound_manager
//
//  Created by Crazelu on 7/11/21.
//

import AVFoundation
import Flutter

public class AudioPlayerUtil: NSObject, AVAudioPlayerDelegate{
    var audioPlayer: AVAudioPlayer?
    var isPlaying: Bool = false
    
    private func setUpPlayer(path: String) -> Bool{
        var isPlayerSet = false
        
        do {
            print(path)
            
            try audioPlayer = AVAudioPlayer(contentsOf:URL(fileURLWithPath: path))
            
            print("Reached here")
            try AVAudioSession.sharedInstance().setCategory(.playback)
            try AVAudioSession.sharedInstance().setActive(true)
            audioPlayer?.delegate = self
            
            isPlayerSet = true
        } catch  {
            print("Error encountered while setting up player")
        }
        
        return isPlayerSet
    }
    
    public func finishPlaying(){
        audioPlayer?.stop()
        audioPlayer = nil
        isPlaying = false
        do {
            try AVAudioSession.sharedInstance().setActive(true)
        } catch  {
            print("Error setting AVAudioSession as inactive")
        }
    }
    
    
    public func play(url: String, result: @escaping FlutterResult){
        
        finishPlaying()
        
        if setUpPlayer(path: url){
            audioPlayer?.play()
            isPlaying = true
            result(nil)
        }
        else{
            result(FlutterError(code: "-1", message: "Failed to play audio", details: nil))
        }
        
    }
    
    public func pause(result: @escaping FlutterResult){
        
        if isPlaying{
            audioPlayer?.pause()
        }
        result(nil)
        
    }
    
    public func resume(result: @escaping FlutterResult){
        
        if isPlaying{
            audioPlayer?.play()
        }
        result(nil)
        
    }
    
    public func stop(result: @escaping FlutterResult){
        finishPlaying()
        result(nil)
    }
    
    public func loop(shouldLoop:Bool, result: @escaping FlutterResult){
        if(shouldLoop){
            audioPlayer?.numberOfLoops = -1
        }
        else{
            audioPlayer?.numberOfLoops = 0
        }
       
        result(nil)
    }
    
}
