package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListSongActivity extends AppCompatActivity {
    ListView lv;
    List<Song> listSong=new ArrayList<Song>();
    List<Song> listSongSearch=new ArrayList<Song>();
    LinearLayout lyFavorites, lyPlaylist,lyMore,lySearch;
    ListViewSongAdapter adapter=null;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        anhXa();
        Intent i=getIntent();
        String nameList=i.getStringExtra("NAMELIST");
        String nameMenu=i.getStringExtra("NAMEMENU");
        String nameLy=i.getStringExtra("ly");
        listSongSearch= (List<Song>) i.getSerializableExtra("ListSearch");
        setTitle(nameMenu);
        if(listSongSearch==null) {
            loadSongs(nameList);
            adapter = new ListViewSongAdapter(ListSongActivity.this, R.layout.list_view_item, listSong);

        }
        if (listSongSearch!=null){
            adapter = new ListViewSongAdapter(ListSongActivity.this, R.layout.list_view_item, listSongSearch);
        }
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song song = (Song) lv.getItemAtPosition(i);
                if (listSongSearch!=null){
                    listSong=listSongSearch;
                }
                startActivity(new Intent(ListSongActivity.this,PlayerActivity.class)
                        .putExtra("MyListSong", (Serializable) listSong)
                        .putExtra("pos",i).putExtra("Activity","com.example.musicapp."+nameLy));

            }
        });
        lyMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListSongActivity.this, MoreActivity.class));
            }
        });
        lyPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListSongActivity.this, PlayListActivity.class));
            }
        });
        lyFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListSongActivity.this, MainActivity.class));
            }
        });
        lySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListSongActivity.this, MainActivity.class));
            }
        });

    }


    private void loadSongs(String nameList) {
        mDatabase.child(nameList).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Song songs=snapshot.getValue(Song.class);
                listSong.add(songs);
                adapter.notifyDataSetChanged();
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
    private void anhXa() {
        lv=(ListView) findViewById(R.id.listViewId);
        lyMore=(LinearLayout) findViewById(R.id.lyMore);
        lyFavorites=(LinearLayout) findViewById(R.id.lyMyFavorites);
        lyPlaylist=(LinearLayout) findViewById(R.id.lyPlayList);
        lySearch=(LinearLayout) findViewById(R.id.lySearch);
    }
}