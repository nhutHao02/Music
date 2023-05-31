package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignUpFragment extends AppCompatActivity {
    DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    int numChildren = 0;
    private EditText editTextUsername, editTextEmail, editTextPassword,editConfirmPassword;
    ArrayList<Account> listAccounts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.fragment_sign_up);
        Button signup = findViewById(R.id.button2);
        editTextUsername = findViewById(R.id.editTextTextPersonName);
        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        editConfirmPassword = findViewById(R.id.editTextTextPassword2);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(signUp()){
                    startActivity(new Intent(SignUpFragment.this, SignUpFragment.class));
                }


            }
        });
        TextView signin = findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpFragment.this, SignUpFragment.class));
            }
        });
    }

    private boolean signUp() {
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString();
        String confirm = editConfirmPassword.getText().toString();
        if(password.equals(confirm)){
            Toast.makeText(this, "Password không khớp", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (Account a : listAccounts){
            if(email.equals(a.getEmail())){
                Toast.makeText(this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        DatabaseReference accounts = mDatabase.child("Accounts");
        accounts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount(); // Lấy số lượng child nodes hiện tại
                String key = String.format("%02d", count + 1); // Định dạng số thứ tự
                // Tạo đối tượng Account
                Account account = new Account(email, password,username,null,null,null,null,null,null);

                // Thêm mới vào playlist
                accounts.child(key).setValue(account);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
        return true;
    }
}