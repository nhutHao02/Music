package com.example.musicapp;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ListViewSongAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Song> listSong;

    public ListViewSongAdapter(Context context, int layout, List<Song> listSong) {
        this.context = context;
        this.layout = layout;
        this.listSong = listSong;
    }

    public int getCount() {
        return listSong.size();
    }

    @Override
    public Object getItem(int i) {
        return listSong.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private class ViewHolder{
        ImageView img,btn;
        TextView nameSong;
        TextView author;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder=new ViewHolder();;
        if(view==null){

            view=inflater.inflate(layout,null);

            //anh xa
            holder.img      =(ImageView) view.findViewById(R.id.imgSong);
            holder.nameSong =(TextView) view.findViewById(R.id.nameSong);
            holder.author   =(TextView) view.findViewById(R.id.nameAuthor);
            holder.btn      = (ImageView) view.findViewById(R.id.icAdd);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }

        //gan gia tri
        Song song =listSong.get(i);
        holder.nameSong.setSelected(true);
        holder.nameSong.setText(song.getNameSong());
        holder.author.setText(song.getAuthor());
        Picasso.get().load(song.getImg()).into(holder.img);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAddPlayList();
            }
        });


        return view;
    }

    private void DialogAddPlayList() {
    }
}
