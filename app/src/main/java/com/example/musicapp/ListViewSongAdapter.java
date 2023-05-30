package com.example.musicapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListViewSongAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Song> listSong;
    private ArrayList<String> playlistList;
    private DatabaseReference mDatabase;
    private ArrayAdapter<String> playlistAdapter;

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
                showPlaylistDialog(song);
            }
        });


        return view;
    }


private void showPlaylistDialog(Song song) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("Select Playlist");

    // Tạo một danh sách playlist
    playlistList=new ArrayList<String>();
    loadPlaylist();

    playlistAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, playlistList);

    builder.setAdapter(playlistAdapter, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String selectedPlaylist = playlistList.get(which);
            // Xử lý khi người dùng chọn một playlist
            mDatabase.child(selectedPlaylist).push().setValue(song);


        }
    });

    builder.show();
}
    private void loadPlaylist(){
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("playLists").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            String namePlayList=snapshot.getValue().toString();
            playlistList.add(namePlayList);
            playlistAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
