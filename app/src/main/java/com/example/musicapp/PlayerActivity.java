package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayerActivity extends AppCompatActivity {
    TextView nameSong, author, sStart, sStop;
    CircleImageView imgSong,imgDisc;
    SeekBar seekBar;
    Button btnPlay, btnPrev, btnNext, btnRepeat, btnShuffle;
    ImageView btnBack;
    private static MediaPlayer mediaPlayer;
    List<Song> myListSong;
    int pos;
    Animation animation;
    boolean repeat,shuffle;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        //anh xa
        anhXa();
        repeat=false;
        shuffle=false;
        //get song from MainActivity
        Intent intent =getIntent();
        myListSong= (List<Song>) intent.getSerializableExtra("MyListSong");
        pos=intent.getIntExtra("pos",0);
        String nameActivityBack=intent.getStringExtra("Activity");
        //set thong tin bai hat
        setInfo();
        animation= AnimationUtils.loadAnimation(PlayerActivity.this,R.anim.disc_rotate);
        // khoi tao mediaPlayer
        initMediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                setTimeTotal();
                updateTime();
                imgSong.startAnimation(animation);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    btnPlay.setBackgroundResource(R.drawable.baseline_play_circle_24);
                    mediaPlayer.pause();
                    imgSong.clearAnimation();
                }else {
                    btnPlay.setBackgroundResource(R.drawable.baseline_pause_circle_24);
                    mediaPlayer.start();
                    imgSong.startAnimation(animation);

                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                if(shuffle){
                    pos=new Random().nextInt(myListSong.size());
                }else {
                    pos++;
                    if(pos>(myListSong.size()-1)){
                        pos=0;
                    }
                }
                initMediaPlayer();
                setInfo();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                        setTimeTotal();
                        updateTime();
                        imgSong.startAnimation(animation);
                    }
                });
                btnPlay.setBackgroundResource(R.drawable.baseline_pause_circle_24);

            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                if(shuffle){
                    pos=new Random().nextInt(myListSong.size());
                }else {
                    pos--;
                    if(pos<0){
                        pos=myListSong.size()-1;
                    }
                }
                initMediaPlayer();
                setInfo();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                        setTimeTotal();
                        updateTime();
                        imgSong.startAnimation(animation);
                    }
                });
                btnPlay.setBackgroundResource(R.drawable.baseline_pause_circle_24);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (repeat==false){
                    btnRepeat.setBackgroundResource(R.drawable.baseline_repeat_one_24);
                    repeat=true;
                }else {
                    btnRepeat.setBackgroundResource(R.drawable.baseline_repeat_24);
                    repeat=false;
                }
            }
        });
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shuffle==false){
                    btnShuffle.setBackgroundResource(R.drawable.baseline_shuffle_24);
                    shuffle=true;
                }else {
                    btnShuffle.setBackgroundResource(R.drawable.baseline_sync_alt_24);
                    shuffle=false;
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(PlayerActivity.this, Class.forName(nameActivityBack)));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        });

    }
    private void updateTime(){
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat timeFormat=new SimpleDateFormat("mm:ss");
                sStart.setText(timeFormat.format(mediaPlayer.getCurrentPosition()));
                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                // kiem tra het bai thif chuyen bai
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }
                        if(repeat==false){
                            pos++;
                            if(pos>(myListSong.size()-1)){
                                pos=0;
                            }
                        }
                        if (repeat==false && shuffle==true) {
                            pos=new Random().nextInt(myListSong.size());
                        }
                        initMediaPlayer();
                        setInfo();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                                setTimeTotal();
                                updateTime();
                            }
                        });
                        btnPlay.setBackgroundResource(R.drawable.baseline_pause_circle_24);
                    }
                });
                handler.postDelayed(this,500);
            }
        },100);
    }
    private void setTimeTotal(){
        SimpleDateFormat timeFormat=new SimpleDateFormat("mm:ss");
        sStop.setText(timeFormat.format(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
    }
    private void setInfo(){
       Song song = myListSong.get(pos);
       nameSong.setSelected(true);
       nameSong.setText(song.getNameSong());
       author.setText(song.getAuthor());
       Picasso.get().load(song.getImg()).into(imgSong);
    }

    private void initMediaPlayer() {
        String url=myListSong.get(pos).getLink();
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes
                        .Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void anhXa() {
        nameSong= (TextView) findViewById(R.id.playerNameSong);
        author= (TextView) findViewById(R.id.playerAuthor);
        sStart= (TextView) findViewById(R.id.playerSStart);
        sStop= (TextView) findViewById(R.id.playerSStop);

        imgSong=(CircleImageView) findViewById(R.id.playerIMG);
        imgDisc=(CircleImageView) findViewById(R.id.playerDisc);

        seekBar=(SeekBar) findViewById(R.id.playerSeekbar);

        btnBack=(ImageView) findViewById(R.id.playerBack);

        btnPlay=(Button) findViewById(R.id.playerBtnPlay);
        btnNext=(Button) findViewById(R.id.playerBtnNext);
        btnPrev=(Button) findViewById(R.id.playerBtnPrev);
        btnRepeat=(Button) findViewById(R.id.playerBtnRepeat);
        btnShuffle=(Button) findViewById(R.id.playerBtnShuffle);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            handler.removeCallbacksAndMessages(null);
        }
    }

}