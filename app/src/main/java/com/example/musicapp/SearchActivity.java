package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    ListView lvTracks1,lvTracks2, lvTracks3,lvNewTracks1,lvNewTracks2;
    LinearLayout nhacEDM,nhacDance,nhacJazz,nhacRock,nhacPop,topVPop,topEDM,topUSUK,topJPop,lyMyfavorites,lyPlaylist,lyMore;
    TextView seeMoreTracks, seeMoreNewTracks;
    EditText searchEditText;
    ImageView searchButton;
    List<Song> listSearch=new ArrayList<Song>();;
    List<Song> listTopTracks=new ArrayList<Song>();
    List<Song> listTopNewTracks=new ArrayList<Song>();
    List<Song> listTopTracksItem1=new ArrayList<Song>();
    List<Song> listTopTracksItem2=new ArrayList<Song>();
    List<Song> listTopTracksItem3=new ArrayList<Song>();
    List<Song> listTopNewTracksItem1=new ArrayList<Song>();
    List<Song> listTopNewTracksItem2=new ArrayList<Song>();
    ListViewSongAdapter adapter=null;
    ListViewSongAdapter adapter2=null;
    ListViewSongAdapter adapter3=null;
    ListViewSongAdapter adapterN1=null;
    ListViewSongAdapter adapterN2=null;
    int index=0;
    int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        anhXa();
        loadSongs();

        adapter=new ListViewSongAdapter(SearchActivity.this,R.layout.list_view_search,listTopTracksItem1);
        lvTracks1.setAdapter(adapter);
        adapter2=new ListViewSongAdapter(SearchActivity.this,R.layout.list_view_search,listTopTracksItem2);
        lvTracks2.setAdapter(adapter2);
        adapter3=new ListViewSongAdapter(SearchActivity.this,R.layout.list_view_search,listTopTracksItem3);
        lvTracks3.setAdapter(adapter3);
        adapterN1=new ListViewSongAdapter(SearchActivity.this,R.layout.list_view_search,listTopNewTracksItem1);
        lvNewTracks1.setAdapter(adapterN1);
        adapterN2=new ListViewSongAdapter(SearchActivity.this,R.layout.list_view_search,listTopNewTracksItem2);
        lvNewTracks2.setAdapter(adapterN2);

        //event
        nhacEDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this,ListSongActivity.class)
                        .putExtra("NAMELIST","myFavorites").putExtra("NAMEMENU","EDM")
                        .putExtra("ly","SearchActivity"));
            }
        });
        nhacJazz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this,ListSongActivity.class)
                        .putExtra("NAMELIST","myFavorites").putExtra("NAMEMENU","JAZZ").putExtra("ly","SearchActivity"));
            }
        });
        nhacPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this,ListSongActivity.class)
                        .putExtra("NAMELIST","myFavorites").putExtra("NAMEMENU","POP").putExtra("ly","SearchActivity"));
            }
        });
        nhacRock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this,ListSongActivity.class)
                        .putExtra("NAMELIST","myFavorites").putExtra("NAMEMENU","ROCK").putExtra("ly","SearchActivity"));
            }
        });
        nhacDance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this,ListSongActivity.class)
                        .putExtra("NAMELIST","myFavorites").putExtra("NAMEMENU","DANCE").putExtra("ly","SearchActivity"));
            }
        });
        topJPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this,ListSongActivity.class)
                        .putExtra("NAMELIST","myFavorites").putExtra("NAMEMENU","Top J-Pop").putExtra("ly","SearchActivity"));
            }
        });
        topEDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this,ListSongActivity.class)
                        .putExtra("NAMELIST","myFavorites").putExtra("NAMEMENU","Top EDM").putExtra("ly","SearchActivity"));
            }
        });
        topUSUK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this,ListSongActivity.class)
                        .putExtra("NAMELIST","myFavorites").putExtra("NAMEMENU","Top US-UK").putExtra("ly","SearchActivity"));
            }
        });
        topVPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this,ListSongActivity.class)
                        .putExtra("NAMELIST","myFavorites").putExtra("NAMEMENU","Top V-Pop").putExtra("ly","SearchActivity"));
            }
        });
        seeMoreTracks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this,ListSongActivity.class)
                        .putExtra("NAMELIST","myFavorites").putExtra("NAMEMENU","Tracks").putExtra("ly","SearchActivity"));
            }
        });
        seeMoreNewTracks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this,ListSongActivity.class)
                        .putExtra("NAMELIST","myFavorites").putExtra("NAMEMENU","New Tracks").putExtra("ly","SearchActivity"));
            }
        });
        lyMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this, MoreActivity.class));
            }
        });
        lyPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this, PlayListActivity.class));
            }
        });
        lyMyfavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this, MainActivity.class));
            }
        });
        lvTracks1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song song = (Song) lvTracks1.getItemAtPosition(i);

                startActivity(new Intent(SearchActivity.this,PlayerActivity.class)
                        .putExtra("MyListSong", (Serializable) listTopTracksItem1)
                        .putExtra("pos",i).putExtra("Activity","com.example.musicapp.SearchActivity"));

            }
        });
        lvTracks2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song song = (Song) lvTracks2.getItemAtPosition(i);

                startActivity(new Intent(SearchActivity.this,PlayerActivity.class)
                        .putExtra("MyListSong", (Serializable) listTopTracksItem2)
                        .putExtra("pos",i).putExtra("Activity","com.example.musicapp.SearchActivity"));

            }
        });
        lvTracks3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song song = (Song) lvTracks3.getItemAtPosition(i);

                startActivity(new Intent(SearchActivity.this,PlayerActivity.class)
                        .putExtra("MyListSong", (Serializable) listTopTracksItem3)
                        .putExtra("pos",i).putExtra("Activity","com.example.musicapp.SearchActivity"));

            }
        });
        lvNewTracks1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song song = (Song) lvNewTracks1.getItemAtPosition(i);

                startActivity(new Intent(SearchActivity.this,PlayerActivity.class)
                        .putExtra("MyListSong", (Serializable) listTopNewTracksItem1)
                        .putExtra("pos",i).putExtra("Activity","com.example.musicapp.SearchActivity"));

            }
        });
        lvNewTracks2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song song = (Song) lvNewTracks2.getItemAtPosition(i);

                startActivity(new Intent(SearchActivity.this,PlayerActivity.class)
                        .putExtra("MyListSong", (Serializable) listTopNewTracksItem2)
                        .putExtra("pos",i).putExtra("Activity","com.example.musicapp.SearchActivity"));

            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = searchEditText.getText().toString();

                // Tạo một tham chiếu đến nút myFavorites trong Firebase Realtime Database
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("myFavorites");

                // Sử dụng phương thức orderByChild và startAt để tìm kiếm bài hát theo tên
                Query query = databaseRef.orderByChild("nameSong").startAt(keyword).endAt(keyword + "\uf8ff");

                ((Query) query).addValueEventListener(new ValueEventListener() {
                    
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Xử lý kết quả tìm kiếm

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            // Lấy dữ liệu bài hát từ DataSnapshot
                            String songName = dataSnapshot.child("nameSong").getValue(String.class);
                            String author = dataSnapshot.child("author").getValue(String.class);
                            String img = dataSnapshot.child("img").getValue(String.class);
                            String link = dataSnapshot.child("link").getValue(String.class);

                            Song s=new Song(img,link,songName,author);
                            listSearch.add(s);
                        }
                        if (listSearch.size()==0){
                            Toast.makeText(SearchActivity.this, "Song is not available", Toast.LENGTH_SHORT).show();
                        }
                        if (listSearch.size()>0){
                            startActivity(new Intent(SearchActivity.this,ListSongActivity.class)
                                    .putExtra("ListSearch", (Serializable) listSearch).putExtra("NAMEMENU","Search")
                                    .putExtra("ly","SearchActivity"));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý khi có lỗi xảy ra
//                        Toast.makeText(SearchActivity.this, "Song is not available", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    private void loadSongs() {
        //LoadTracks
        mDatabase.child("myFavorites").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Song songs=snapshot.getValue(Song.class);
                listTopTracks.add(songs);
                index++;
                if (index<5){
                    listTopTracksItem1.add(songs);
                }
                if (index>=5 && index<9){
                    listTopTracksItem2.add(songs);
                }
                if (index>=9&&index<13){
                    listTopTracksItem3.add(songs);
                }
                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
                adapter3.notifyDataSetChanged();
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
        //loadNEwTracks
        mDatabase.child("myFavorites").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Song songs=snapshot.getValue(Song.class);
                listTopNewTracks.add(songs);
                id++;
                if (id<5){
                    listTopNewTracksItem1.add(songs);
                }
                if (id>=5 && id<9){
                    listTopNewTracksItem2.add(songs);
                }
                adapterN1.notifyDataSetChanged();
                adapterN2.notifyDataSetChanged();
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
        lvTracks1=(ListView) findViewById(R.id.lvTopTracks1);
        lvTracks2=(ListView) findViewById(R.id.lvTopTracks2);
        lvTracks3=(ListView) findViewById(R.id.lvTopTracks3);
        lvNewTracks1=(ListView) findViewById(R.id.lvTopNewTracks1);
        lvNewTracks2=(ListView) findViewById(R.id.lvTopNewTracks2);
        nhacEDM=(LinearLayout) findViewById(R.id.nhacEDM);
        nhacDance=(LinearLayout) findViewById(R.id.nhacDance);
        nhacJazz=(LinearLayout) findViewById(R.id.nhacJazz);
        nhacPop=(LinearLayout) findViewById(R.id.nhacPop);
        nhacRock=(LinearLayout) findViewById(R.id.nhacRock);
        topVPop=(LinearLayout) findViewById(R.id.topVPop);
        topUSUK=(LinearLayout) findViewById(R.id.topUSUK);
        topEDM=(LinearLayout) findViewById(R.id.topEDM);
        topJPop=(LinearLayout) findViewById(R.id.topJPop);
        seeMoreNewTracks=(TextView) findViewById(R.id.seeMoreNewTracks);
        seeMoreTracks=(TextView) findViewById(R.id.seeMoreTracks);
        lyMyfavorites=(LinearLayout) findViewById(R.id.lyMyFavorites);
        lyPlaylist=(LinearLayout) findViewById(R.id.lyPlayList);
        lyMore=(LinearLayout) findViewById(R.id.lyMore);
        searchButton=(ImageView) findViewById(R.id.btnSearch);
        searchEditText=(EditText) findViewById(R.id.editTxtSearch);


    }
}