package com.example.musicapp;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {

    Context context;
    ArrayList<ModelSong> songList;

    public SongAdapter( Context context, ArrayList<ModelSong> songList) {
        this.context = context;
        this.songList = songList;
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.music_details,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongAdapter.MyViewHolder myViewHolder, int i) {
        ModelSong  mdSong= songList.get(i);
        myViewHolder.txttitle.setText(mdSong.getSongTitle());
        myViewHolder.txtartist.setText(mdSong.getSongArtist());
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txttitle,txtartist;
        public MyViewHolder(  View itemView) {
            super(itemView);
            //img=(ImageView) itemView.findViewById(R.id.image);
            txttitle=(TextView) itemView.findViewById(R.id.title);
            txtartist=(TextView) itemView.findViewById(R.id.artist);

        }
    }
}
