package com.example.musicapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class MoreActivity extends AppCompatActivity {
    ImageView btnMore;
    LinearLayout lyMyFavorites, lySearch, lyPlaylist, logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        anhXa();
        TextView login = findViewById(R.id.moreLogin);
        LinearLayout history = findViewById(R.id.moreHistory);
        LinearLayout manage = findViewById(R.id.moreManage);
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String accountJson = preferences.getString("account", "");
        Gson gson = new Gson();
        Account restoredAccount = gson.fromJson(accountJson, Account.class);
        if(restoredAccount!=null){
            login.setText(restoredAccount.getName());
            if(restoredAccount.getAdmin().equals("admin")){
                manage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MoreActivity.this, ManageSongActivity.class);
                        // Chuyển đến Activity mục tiêu
                        startActivity(intent);
                    }
                });
            }

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("account"); // Xóa một key cụ thể
                editor.apply();
                    Intent intent = new Intent(MoreActivity.this, MoreActivity.class);
                    // Chuyển đến Activity mục tiêu
                    startActivity(intent);
                }
            });
        }
        if (restoredAccount==null){
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MoreActivity.this, SignInFragment.class);
                    // Chuyển đến Activity mục tiêu
                    startActivity(intent);
                }
            });
        }


        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoreActivity.this, HistoryActivity.class);
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
        logout=(LinearLayout) findViewById(R.id.logout);
    }

}