package com.example.musicapp;

import android.graphics.Bitmap;
import android.net.Uri;

public class ModelSong {
    String songTitle;
    String songDuration;
    String songArtist;
    Uri songUri;
    Bitmap Artcover;

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(String songDuration) {
        this.songDuration = songDuration;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public Uri getSongUri() {
        return songUri;
    }

    public void setSongUri(Uri songUri) {
        this.songUri = songUri;
    }

    public Bitmap getArtcover() {
        return Artcover;
    }

    public void setArtcover(Bitmap artcover) {
        Artcover = artcover;
    }
}
