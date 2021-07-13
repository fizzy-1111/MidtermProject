package com.example.musicapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.Normalizer2;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import static android.bluetooth.BluetoothGattCharacteristic.PERMISSION_READ;

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
        if (checkPermission()) {
            AudioSetup();
        }

    }
    public void AudioSetup(){
         recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
         txtTitle=(TextView) findViewById(R.id.songNameText);
         txtTimeCount=(TextView) findViewById(R.id.timecount);
         txtTimeTotal=(TextView) findViewById(R.id.timetotal);
         btnNext=(ImageButton) findViewById(R.id.nextbtn);
         btnPlay=(ImageButton) findViewById(R.id.playbtn);
         btnStop=(ImageButton) findViewById(R.id.stopbtn);
         btnPrev=(ImageButton) findViewById(R.id.prevbtn);
         modelSongArrayList= new ArrayList<>();
         mediaPlayer =new MediaPlayer();
         getAudioFiles();
        if(modelSongArrayList.size()>=1) {
            prepAudio(audio_index);

            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        btnPlay.setImageResource(R.drawable.pausebtn);
                    } else {
                        mediaPlayer.start();
                        btnPlay.setImageResource(R.drawable.playbtn);
                    }
                }
            });
            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.stop();
                    btnPlay.setImageResource(R.drawable.pausebtn);
                    prepAudio(audio_index);

                }
            });
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    audio_index++;
                    if (audio_index > modelSongArrayList.size() - 1) {
                        audio_index = 0;
                    }
                    prepAudio(audio_index);
                    btnPlay.setImageResource(R.drawable.playbtn);
                    mediaPlayer.start();
                }
            });
            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    audio_index--;
                    if (audio_index < 0) {
                        audio_index = 0;
                    }
                    prepAudio(audio_index);
                    btnPlay.setImageResource(R.drawable.playbtn);
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    audio_index++;

                }
            });
        }
    }
    private void setTime(){

    }
    public void prepAudio(int pos)  {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(this, modelSongArrayList.get(pos).getSongUri());
            mediaPlayer.prepare();
            txtTitle.setText(modelSongArrayList.get(pos).getSongTitle());
            audio_index=pos;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void getAudioFiles(){
        ContentResolver contentResolver=getContentResolver();
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor= contentResolver.query(uri,null,null,null,null);
        if(cursor!=null && cursor.moveToFirst()){
            do {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                String pathId = cursor.getString(column_index);
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                ModelSong modelSong = new ModelSong();
                modelSong.setSongTitle(title);
                modelSong.setSongArtist(artist);
                modelSong.setSongUri(Uri.parse(url));
                modelSong.setSongDuration(duration);
                try {
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(pathId);
                    byte [] data = mmr.getEmbeddedPicture();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    modelSong.setArtcover(bitmap);
                }
                catch (Exception e)
                {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.music_symbol );
                    modelSong.setArtcover(bitmap);
                }
                modelSongArrayList.add(modelSong);

            } while (cursor.moveToNext());
        }
        SongAdapter songAdapter= new SongAdapter(this,modelSongArrayList);
        recyclerView.setAdapter(songAdapter);
        songAdapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View v) {
             prepAudio(pos);
             mediaPlayer.start();
             btnPlay.setImageResource(R.drawable.playbtn);
            }
        });
    }
    //runtime storage permission
    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case  PERMISSION_READ: {
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(getApplicationContext(), "Please allow storage permission", Toast.LENGTH_LONG).show();
                    } else {
                        AudioSetup();
                    }
                }
            }
        }
    }
}