package com.example.musicapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class PlayListActivity extends AppCompatActivity {
    ListView listPlayList;
    ArrayList<PlayList> arrayList = new ArrayList<>();
    ArrayList<String> nameLPlayList = new ArrayList<>();
    PlayListAdapter adapter;
    DatabaseReference mDatabase;
     int numChildren = 0;
     int numSong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_play);
        setTitle("PlayLists");
        ListView listViewPlay = findViewById(R.id.listViewPlay);
        LinearLayout menuBot = findViewById(R.id.menuBot);

        // Lấy chiều cao của LinearLayout
        int menuBotHeight = menuBot.getHeight();

        // Lấy chiều cao màn hình
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        // Thiết lập chiều cao của ListView
        int listViewHeight = screenHeight - menuBotHeight;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, listViewHeight);
        listViewPlay.setLayoutParams(layoutParams);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        nameLPlayList = new ArrayList<>();
        loadLists();

        Anhxa();
        adapter = new PlayListAdapter(this,R.layout.view_play_list, arrayList);
        listPlayList.setAdapter(adapter);
        listPlayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Song song = (Song) listPlayList.getItemAtPosition(i);
//                String namePlay = nameLPlayList.get(i);
                PlayList list = arrayList.get(i);
                startActivity(new Intent(PlayListActivity.this,PlayList.class)
                        .putExtra("MyListSong", (Serializable) list.getList())
                        .putExtra("name", (Serializable) list.getName())
                        .putExtra("pos",i));

            }
        });
        Button btn_add = findViewById(R.id.addPlayList);
        btn_add.setOnClickListener(new AdapterView.OnClickListener(){

            @Override
            public void onClick(View view) {
                showCreatePlaylistDialog();
            }
        });

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
                String key = String.format("%02d", count + 1); // Định dạng số thứ tự

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
                    arrayList.get(numChildren).setSize(numSong);
                    arrayList.get(numChildren).setList(listSong);
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
    private void Anhxa(){
        listPlayList = (ListView) findViewById(R.id.listViewPlay);

        arrayList.add(new PlayList("list1",3,"https://firebasestorage.googleapis.com/v0/b/musicpjandroid.appspot.com/o/img%2FRight%20Now%20Na%20Na%20Na%20Lyrics.jpg?alt=media&token=41c70495-7128-49da-b0e0-e9e6d8f16dcb"));

    }
    public  int getNumSong(){
        return 0;
    }
}