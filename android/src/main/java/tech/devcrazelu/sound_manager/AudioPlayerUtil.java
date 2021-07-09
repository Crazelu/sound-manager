package tech.devcrazelu.sound_manager;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import androidx.annotation.NonNull;
import java.io.IOException;

public class AudioPlayerUtil {
    private static String TAG = "SoundManager";
    private MediaPlayer player;
    private boolean isPlaying = false;


    /**
     * @param filePath
     * Initiates an audio playback session.
     * @throws Exception
     */
    public void playAudio(@NonNull String filePath) throws Exception {
        try{

            //if an audio is currently playing, stop it and reset MediaPlayer
            if(isPlaying){
                stopAudio();
            }

            if(player == null) player = new MediaPlayer();

            try {
                player.setDataSource(filePath);
                player.prepare();
                player.start();
                isPlaying = true;
            } catch (IOException e) {
                Log.d(TAG, e.toString());
                throw e;
            }

        }catch (Exception e){
            Log.d(TAG, e.toString());
            throw e;
        }
    }

    /**
     * Resumes a paused audio playback session.
     *
     * It does nothing if playAudio() is not called first.
     */
    public void resumeAudioPlayback(){
        try{
            if(player == null && !isPlaying) return;

            player.start();

        }catch (Exception e){
            Log.d(TAG, e.toString());
            throw  e;
        }
    }


    /**
     * Pauses an audio playback session.
     *
     * It does nothing if playAudio() is not called first.
     */
    public void pauseAudio() {
        try{
            if(player == null && !isPlaying) return;
            player.pause();
        }catch (Exception e){
            Log.d(TAG, e.toString());
            throw  e;
        }
    }

    /**
     * Stops an audio playback session.
     *
     * It does nothing if playAudio() is not called first.
     */
    public void stopAudio() {
        try {
            if (player != null && isPlaying) {
                player.stop();
                resetPlayer();
            }

        } catch (Exception e) {
            Log.d(TAG, e.toString());
            throw e;
        }
    }

    /**
     * @param milliseconds
     * Seeks to @param milliseconds which is the offset in milliseconds from start of the audio playback.
     *
     * It does nothing if playAudio() is not called first.
     */
    public void seek(int milliseconds) {
        try {
            if (player != null && isPlaying) {
                player.seekTo(milliseconds);
            }

        } catch (Exception e) {
            Log.d(TAG, e.toString());
            throw e;
        }
    }

    /**
     * @param shouldLoop sets whether to loop audio or not.
     * Sets the MediaPlayer to be looping if @param shouldLoop = true.
     * Otherwise, sets MediaPlayer to be non-looping.
     */
    public void setLooping(boolean shouldLoop) {
        try {
            if (player != null) {
                player.setLooping(shouldLoop);
            }

        } catch (Exception e) {
            Log.d(TAG, e.toString());
            throw e;
        }
    }

    /**
     * Resets MediaPlayer and releases resources.
     */
    public void resetPlayer(){
        if(player != null){
            player.reset();
            player.release();
            player = null;
            isPlaying = false;
        }
    }

}
