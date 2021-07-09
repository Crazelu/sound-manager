package tech.devcrazelu.sound_manager;

import android.media.MediaPlayer;
import io.flutter.plugin.common.MethodChannel.Result;
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
    public void playAudio(@NonNull String filePath, Result result) {
        try{

            //if an audio is currently playing, stop it and reset MediaPlayer
            if(isPlaying){
                stopAudio(result, false);
            }

            if(player == null) player = new MediaPlayer();

            try {
                player.setDataSource(filePath);
                player.prepare();
                player.start();
                isPlaying = true;
                result.success(null);
            } catch (IOException e) {
                Log.d(TAG, "Couldn't play " + filePath + ". Did you call SoundManager.init()?");
                Log.d(TAG, e.toString());
                result.error(TAG, "Couldn't play " + filePath + ". Did you call SoundManager.init()?", null);
                return;
            }

        }catch (Exception e){
            Log.d(TAG, "Couldn't play " + filePath + ". Did you call SoundManager.init()?");
            Log.d(TAG, e.toString());
            result.error(TAG, "Couldn't play " + filePath + ". Did you call SoundManager.init()?", null);
        }
    }

    /**
     * Resumes a paused audio playback session.
     *
     * It does nothing if playAudio() is not called first.
     */
    public void resumeAudioPlayback(Result result) {
        try {
            if (player != null && isPlaying) {
                player.start();
                result.success(null);
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            result.error(TAG, "Couldn't resume audio playback. Please report this issue.", null);
        }
    }


    /**
     * Pauses an audio playback session.
     *
     * It does nothing if playAudio() is not called first.
     */
    public void pauseAudio(Result result) {
        try {
            if (player != null && isPlaying) {
                player.pause();
                result.success(null);
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            result.error(TAG, "Couldn't pause audio playback. Please report this issue.", null);
        }
    }

    /**
     * Stops an audio playback session.
     *
     * It does nothing if playAudio() is not called first.
     */
    public void stopAudio(Result result, boolean returnResult) {
        try {
            if (player != null && isPlaying) {
                player.stop();
                resetPlayer();
                if(returnResult){
                    result.success(null);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            if(returnResult){
                result.error(TAG, "Couldn't stop audio playback. Please report this issue.", null);
            }
        }
    }

    /**
     * @param milliseconds
     * Seeks to @param milliseconds which is the offset in milliseconds from start of the audio playback.
     *
     * It does nothing if playAudio() is not called first.
     */
    public void seek(int milliseconds, Result result) {
        try {
            if (player != null && isPlaying) {
                player.seekTo(milliseconds);
                result.success(null);
            }

        } catch (Exception e) {
            Log.d(TAG, e.toString());
            result.error(TAG, "Couldn't seek to " + milliseconds + ". Please report this issue.", null);
        }
    }

    /**
     * @param shouldLoop sets whether to loop audio or not.
     * Sets the MediaPlayer to be looping if @param shouldLoop = true.
     * Otherwise, sets MediaPlayer to be non-looping.
     */
    public void setLooping(boolean shouldLoop, Result result) {
        try {
            if (player != null) {
                player.setLooping(shouldLoop);
                result.success(null);
            }

        } catch (Exception e) {
            Log.d(TAG, e.toString());
            result.error(TAG, "Couldn't set looping. Please report this issue.", null);
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
