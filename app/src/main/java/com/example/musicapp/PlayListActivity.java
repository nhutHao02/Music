package com.example.musicapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class PlayListActivity extends AppCompatActivity {
    ListView listViewPlay1;
    ArrayList<PlayList> arrayList = new ArrayList<>();
    ArrayList<String> nameLPlayList = new ArrayList<>();
    PlayListAdapter adapter;
    DatabaseReference mDatabase;
     int numChildren = 0;
     int numSong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        setTitle("PlayLists");
        LinearLayout menuBot = findViewById(R.id.menuBot);
        getAnhXa();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        nameLPlayList = new ArrayList<>();
        loadLists();


        adapter = new PlayListAdapter(PlayListActivity.this,R.layout.view_play_list, arrayList);
        listViewPlay1.setAdapter(adapter);

        Button btn_add = findViewById(R.id.addPlayList);
        btn_add.setOnClickListener(new AdapterView.OnClickListener(){

            @Override
            public void onClick(View view) {
                showCreatePlaylistDialog();
            }
        });

    }
    public void onItemClick(View view) {
        // Xử lý sự kiện click vào item trong ListView
        int position = listViewPlay1.getPositionForView(view);
        PlayList selectedItem = arrayList.get(position);
        System.out.println("P"+position);
        System.out.println("A"+selectedItem);
        startActivity(new Intent(PlayListActivity.this,ListSongActivity.class)
                .putExtra("NAMELIST","myFavorites").putExtra("NAMEMENU","EDM")
                .putExtra("ly","PlayListActivity"));
        // Xử lý logic với selectedItem
    }
    private void showCreatePlaylistDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Playlist");

        // Tạo layout cho dialog bằng mã Java
        final EditText editTextPlaylistName = new EditText(this);
        editTextPlaylistName.setHint("Enter playlist name");
        builder.setView(editTextPlaylistName);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String playlistName = editTextPlaylistName.getText().toString();
                createPlaylist(playlistName);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void createPlaylist(String playlistName) {
        DatabaseReference playlistsRef = mDatabase.child("playList");
        playlistsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount(); // Lấy số lượng child nodes hiện tại
                String key = String.format("%02d", count); // Định dạng số thứ tự

                // Thêm mới vào playlist
                playlistsRef.child(key).child("nameList").setValue(playlistName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }
    private void loadLists() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String accountJson = preferences.getString("account", "");
        Gson gson = new Gson();
        Account restoredAccount = gson.fromJson(accountJson, Account.class);
        if(restoredAccount!=null){

        }
        mDatabase.child("playList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                numChildren++;
                String playlistKey = snapshot.getKey(); // Lấy mã của playlist
                Map<String, Object> playlistData = (Map<String, Object>) snapshot.getValue();
                String nameList = (String) playlistData.get("nameList"); // Lấy giá trị của thuộc tính "nameList"
                System.out.println("Key: " + playlistKey + ", Name: " + nameList);
                nameLPlayList.add(nameList);
                // Kiểm tra xem đã lấy được tất cả dữ liệu playlists chưa
                System.out.println("s"+nameLPlayList.size());
                System.out.println("c"+ numChildren);
                if (nameLPlayList.size() == numChildren) {
                    System.out.println("Load");
                    loadSongs(numChildren);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                numChildren--;
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    private void loadSongs(int numChildren){
        numSong = 0;

        String i = nameLPlayList.get(numChildren-1);
        System.out.println("name" + i);
        ArrayList <Song> listSong = new ArrayList<>();
        arrayList.add(new PlayList(i,numSong,"https://firebasestorage.googleapis.com/v0/b/musicpjandroid.appspot.com/o/img%2FRight%20Now%20Na%20Na%20Na%20Lyrics.jpg?alt=media&token=41c70495-7128-49da-b0e0-e9e6d8f16dcb",null));
        adapter.notifyDataSetChanged();
            mDatabase.child(i.trim()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Song songs=snapshot.getValue(Song.class);
                    System.out.println("song" +songs);
                    listSong.add(songs);
                    numSong++;
                    arrayList.get(numChildren-1).setSize(numSong);
                    arrayList.get(numChildren-1).setList(listSong);
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
    public void getAnhXa(){
        listViewPlay1 = (ListView) findViewById(R.id.listViewPlay);
    }
    public  int getNumSong(){
        return 0;
    }
}