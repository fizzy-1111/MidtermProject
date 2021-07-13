package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.icu.text.Normalizer2;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<ModelSong> modelSongArrayList;
    RecyclerView recyclerView;
    TextView txtTitle, txtTimeCount, txtTimeTotal;
    SeekBar skbar;
    ImageButton btnPrev,btnNext,btnStop,btnPlay;
    MediaPlayer mediaPlayer;
    int audio_index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    public void AudioSetup(){
         txtTitle=(TextView) findViewById(R.id.songNameText);
         txtTimeCount=(TextView) findViewById(R.id.timecount);
         txtTimeTotal=(TextView) findViewById(R.id.timetotal);
         btnNext=(ImageButton) findViewById(R.id.nextbtn);
         btnPlay=(ImageButton) findViewById(R.id.playbtn);
         btnStop=(ImageButton) findViewById(R.id.stopbtn);
        // btnPrev=(ImageButton) findViewById(R.id.prevbtn);

    }
}