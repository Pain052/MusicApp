package com.example.musicbasicapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPalyerActivity extends AppCompatActivity {
    private TextView tvTitle, tvCurrentTime, tvTotalTime;
    private SeekBar seekBar;
    private ImageView ivPausePlay, btnNext, btnPrev, iconMusic;
    ArrayList<AudioModel> musicsList;
    AudioModel currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();

    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_palyer);

        initView();

        //get Data from MusicPlayerAdapter
        musicsList = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");

        setResourcesWithMusic();

        MusicPalyerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    tvCurrentTime.setText(convertToMMSS(mediaPlayer.getCurrentPosition() + ""));
                    if (mediaPlayer.isPlaying()){
                        ivPausePlay.setImageResource(R.drawable.ic_baseline_pause_24);
                        iconMusic.setRotation(x++);
                    }else{
                        ivPausePlay.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                        iconMusic.setRotation(0);
                    }
                }
                new Handler().postDelayed(this, 100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mediaPlayer != null && b){
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initView(){
        tvTitle = findViewById(R.id.tvSongTitle);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tvTotalTime = findViewById(R.id.tvTotalTimeMusic);
        seekBar = findViewById(R.id.seek_bar);
        ivPausePlay = findViewById(R.id.ivPausePlay);
        btnNext = findViewById(R.id.ivNext);
        btnPrev = findViewById(R.id.ivPrev);
        iconMusic = findViewById(R.id.ivMusicIconBig);

        tvTitle.setSelected(true);
    }

    void setResourcesWithMusic(){
        currentSong = musicsList.get(MyMediaPlayer.currentIndex);
        tvTitle.setText(currentSong.getTitle());
        tvTotalTime.setText(convertToMMSS(currentSong.getDuration()));

        ivPausePlay.setOnClickListener(v -> pausePlayMusic());
        btnNext.setOnClickListener(v -> nextMusic());
        btnPrev.setOnClickListener(v -> prevMusic());

        playMusic();
    }

    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    private void playMusic(){
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void nextMusic(){
        if (MyMediaPlayer.currentIndex == musicsList.size() - 1) {
            Toast.makeText(MusicPalyerActivity.this, "No more songs!", Toast.LENGTH_SHORT).show();
            return;
        }
        MyMediaPlayer.currentIndex += 1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void prevMusic(){
        if (MyMediaPlayer.currentIndex == 0) {
            Toast.makeText(MusicPalyerActivity.this, "No more songs!", Toast.LENGTH_SHORT).show();
            return;
        }
        MyMediaPlayer.currentIndex -= 1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void pausePlayMusic(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }else{
            mediaPlayer.start();
        }
    }
}