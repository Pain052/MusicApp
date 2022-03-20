package com.example.musicbasicapplication;

import android.media.MediaPlayer;

public class MyMediaPlayer {
    public static MediaPlayer instance;
    public static MediaPlayer getInstance(){
        if (instance == null){
            instance = new MediaPlayer();
        }
        return instance;
    }

    public static int currentIndex = -1;
}
