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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        anhXa();
        TextView login = findViewById(R.id.moreLogin);
        LinearLayout history = findViewById(R.id.moreHistory);
        LinearLayout manage = findViewById(R.id.moreManage);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoreActivity.this, SignInFragment.class);
                // Chuyển đến Activity mục tiêu
                startActivity(intent);
            }
        });
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoreActivity.this, ManageSongActivity.class);
                // Chuyển đến Activity mục tiêu
                startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoreActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void anhXa() {
    }

}