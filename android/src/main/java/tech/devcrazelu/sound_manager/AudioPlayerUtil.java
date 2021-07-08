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


    public void playAudio(@NonNull String filePath, @NonNull Context context, boolean isFullPath ) throws Exception {
        try{
            if(player == null) player = new MediaPlayer();

            String fileName = filePath;

            if((isFullPath == Boolean.parseBoolean("null")) || !isFullPath ){
                final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
                fileName = Environment.getExternalStorageDirectory().getPath() + cacheDir + "/"+filePath;

            }

            try {
                player.setDataSource(fileName);
                player.prepare();
                player.start();
            } catch (IOException e) {
                Log.d(TAG, e.toString());
                throw e;
            }

        }catch (Exception e){
            Log.d(TAG, e.toString());
            throw e;
        }




    }

    public void pauseAudio() {
        try{
            if(player == null) return;
            player.pause();
        }catch (Exception e){
            Log.d(TAG, e.toString());
            throw  e;
        }

    }

    public void stopAudio()  {
        try{
            if(player == null) return;

            player.stop();
            player.release();
            player = null;
        }catch (Exception e){
            Log.d(TAG, e.toString());
            throw  e;
        }
    }

    public void seek(int milliseconds)  {
        try{
            if(player == null ) return;

            player.seekTo(milliseconds);
        }catch (Exception e){
            Log.d(TAG, e.toString());
            throw  e;
        }
    }

    public void setLooping(boolean shouldLoop) {
        try{
            if(player == null) return;

            player.setLooping(shouldLoop);
        }catch (Exception e){
            Log.d(TAG, e.toString());
            throw  e;
        }
    }

}
