package com.example.musicapp;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SignInFragment extends AppCompatActivity {
    DatabaseReference mDatabase;
    private EditText editTextEmail,editTextPassword;
    boolean isAccountFound = false;
    ArrayList<Account> listAccounts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.fragment_sign_in);
        Button sigin = findViewById(R.id.button3);
        loadAccountsI();
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
    private void loadAccountsI() {

        mDatabase.child("Accounts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot accountSnapshot : dataSnapshot.getChildren()) {
                    String address = accountSnapshot.child("address").getValue(String.class);
                    String dob = accountSnapshot.child("dob").getValue(String.class);
                    String email = accountSnapshot.child("email").getValue(String.class);
                    String myHistory = accountSnapshot.child("myHistory").getValue(String.class);
                    List<String> myList = new ArrayList<>();
                    DataSnapshot myListSnapshot = accountSnapshot.child("myList");
                    for (DataSnapshot itemSnapshot : myListSnapshot.getChildren()) {
                        String item = itemSnapshot.getValue(String.class);
                        myList.add(item);
                    }
                    String name = accountSnapshot.child("name").getValue(String.class);
                    String pass = accountSnapshot.child("pass").getValue(String.class);
                    String sex = accountSnapshot.child("sex").getValue(String.class);
                    String ad = accountSnapshot.child("ad").getValue(String.class);

                    Account account = new Account(name,email,pass,sex,address,dob,myList,myHistory,ad);
                    listAccounts.add(account);
                }
            }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý lỗi (nếu có)
                    }
                });
    }
    private boolean isValidAccount(String email, String password) {
        for (Account account : listAccounts) {
            if (account.getEmail().equals(email) && account.getPass().equals(password)) {
                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                // Chuyển đối tượng thành chuỗi JSON
                Gson gson = new Gson();
                String accountJson = gson.toJson(account);
                editor.putString("account", accountJson);
                editor.apply();
                return true;
            }
        }
        return false;
    }

}