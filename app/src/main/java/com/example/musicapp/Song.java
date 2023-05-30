package com.example.musicapp;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class Song implements Serializable {
    private String img;
    private String link;
    private String nameSong;
    private String author;

    public Song() {
    }

    public Song(String img, String link, String nameSong, String author) {
        this.img = img;
        this.link = link;
        this.nameSong = nameSong;
        this.author=author;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public void createSongHistory(Song song,  DatabaseReference mDatabase) {
        DatabaseReference accounts = mDatabase.child("history");
        accounts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount(); // Lấy số lượng child nodes hiện tại
                String key = String.format("%02d", count + 1); // Định dạng số thứ tự
                // Tạo đối tượng Account

                // Thêm mới vào playlist
                accounts.child(key).setValue(song);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }
}
