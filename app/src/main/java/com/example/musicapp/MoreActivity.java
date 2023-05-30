package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MoreActivity extends AppCompatActivity {
    ImageView btnMore;
    LinearLayout lyMyFavorites, lySearch, lyPlaylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        anhXa();
        TextView login = findViewById(R.id.moreLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoreActivity.this, SignInFragment.class);
                // Chuyển đến Activity mục tiêu
                startActivity(intent);
            }
        });
        lyMyFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MoreActivity.this, MainActivity.class));
            }
        });
        lySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MoreActivity.this, SearchActivity.class));
            }
        });
        lyPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MoreActivity.this, PlayListActivity.class));
            }
        });
    }

    private void anhXa() {
        lyMyFavorites=(LinearLayout) findViewById(R.id.lyMyFavorites);
        lySearch=(LinearLayout) findViewById(R.id.lySearch);
        lyPlaylist=(LinearLayout) findViewById(R.id.lyPlayList);
    }

}