package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    List<Song> myFavorites=new ArrayList<Song>();
    ListView lvSong;
    ListViewSongAdapter adapter=null;
    LinearLayout lyMore, lySearch, lyPlaylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("History");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //ánh xạ
        anhXa();
        //Load data
        loadSongHistorys();

        adapter=new ListViewSongAdapter(HistoryActivity.this,R.layout.item_history,myFavorites);
        lvSong.setAdapter(adapter);

        //event list View
        lvSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song song = (Song) lvSong.getItemAtPosition(i);

                startActivity(new Intent(HistoryActivity.this,PlayerActivity.class)
                        .putExtra("MyListSong", (Serializable) myFavorites)
                        .putExtra("pos",i).putExtra("Activity","com.example.musicapp.MoreActivity"));

            }
        });
        ImageView listPlay = (ImageView) findViewById(R.id.icPlayList);
        listPlay.setOnClickListener(new AdapterView.OnClickListener(){

            @Override
            public void onClick(View view) {
                // Tạo Intent để chuyển đến Activity mục tiêu
                Intent intent = new Intent(HistoryActivity.this, PlayListActivity.class);
                // Chuyển đến Activity mục tiêu
                startActivity(intent);
            }
        });
        lyMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HistoryActivity.this, MoreActivity.class));
            }
        });
        lySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HistoryActivity.this, SearchActivity.class));
            }
        });



    }

    private void loadSongHistorys() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String accountJson = preferences.getString("account", "");
        Gson gson = new Gson();
        Account restoredAccount = gson.fromJson(accountJson, Account.class);
        if(restoredAccount!=null){
            mDatabase.child(restoredAccount.getHistory()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Song songs=snapshot.getValue(Song.class);
                    myFavorites.add(songs);
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

    }

    private void anhXa() {
        lvSong=(ListView) findViewById(R.id.listViewId);
        lyMore=(LinearLayout) findViewById(R.id.lyMore);
        lySearch=(LinearLayout) findViewById(R.id.lySearch);
        lyPlaylist=(LinearLayout) findViewById(R.id.lyPlayList);
    }

}