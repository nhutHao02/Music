package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SignInFragment extends AppCompatActivity {
    DatabaseReference mDatabase;
    private EditText editTextEmail,editTextPassword;
    ArrayList<Account> listAccounts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.fragment_sign_in);
        Button sigin = findViewById(R.id.button3);
        loadAccounts();
        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
               // Kiểm tra tài khoản
                if (isValidAccount(email, password)) {
                    Toast.makeText(SignInFragment.this, "Login thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInFragment.this, MainActivity.class));
                } else {
                    // Tài khoản không hợp lệ, hiển thị thông báo lỗi
                    Toast.makeText(SignInFragment.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView signup = findViewById(R.id.signup);
        TextView forgot = findViewById(R.id.forgot);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInFragment.this, SignUpFragment.class));
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInFragment.this, SignUpFragment.class));
            }
        });

    }
    private void loadAccounts() {
        mDatabase.child("Account").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Account account=snapshot.getValue(Account.class);
                listAccounts.add(account);
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
    private boolean isValidAccount(String email, String password) {
        for (Account account : listAccounts) {
            if (account.getEmail().equals(email) && account.getPass().equals(password)) {
                return true;
            }
        }
        return false;
    }
}