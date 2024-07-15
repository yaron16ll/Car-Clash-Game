package com.example.carclash.utilities;

import android.content.Context;
import android.media.MediaPlayer;



public class SoundPlayer {

    private Context context;
    private MediaPlayer mediaPlayer;

    public SoundPlayer(Context context) {
        this.context = context.getApplicationContext();
    }

    public void play(int resID, boolean isLooped, float leftVol, float rightVol) {
            mediaPlayer = MediaPlayer.create(context, resID);
              mediaPlayer.setLooping(isLooped);
                mediaPlayer.setVolume(leftVol, rightVol);
                mediaPlayer.setOnCompletionListener(mp -> stop()); // Ensure resources are released on completion
                mediaPlayer.start();
            }

    public void stop() {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
    }
}
