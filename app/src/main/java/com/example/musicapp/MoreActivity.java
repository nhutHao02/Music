package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MoreActivity extends AppCompatActivity {
    ImageView btnMore;
    LinearLayout lyMyFavorite, lySearch, lyPlaylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        anhXa();


        lyMyFavorite.setOnClickListener(new View.OnClickListener() {
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
        lyMyFavorite=(LinearLayout) findViewById(R.id.lyMyFavorites);
        lySearch=(LinearLayout) findViewById(R.id.lySearch);
        lyPlaylist=(LinearLayout) findViewById(R.id.lyPlayList);
    }

}