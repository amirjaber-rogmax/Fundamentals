package com.rogmax.amirjaber.fundamentals.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.rogmax.amirjaber.fundamentals.R;


public class MyService extends Service {
    private static final int MAX_VOLUME = 1000;

    private MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // continues running until stopped
        float volume = (float) (1 - (Math.log(MAX_VOLUME - 200) / Math.log(MAX_VOLUME)));

        player = MediaPlayer.create(this, R.raw.moonsong);
        player.setVolume(volume, volume);
        player.setLooping(true);
        player.start();
        showShortToast("Service Started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
        showShortToast("Service Destroyed");
    }

    public void showShortToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
