package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ManageAccontActivity extends AppCompatActivity {
    ListAccountAdapter adapter=null;
    List<Account> listAccount=new ArrayList<Account>();
    DatabaseReference mDatabase;
    ImageView btnAddAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_accont);
        LinearLayout lyMore=(LinearLayout) findViewById(R.id.lyMoreMan);
        LinearLayout lyManageSong=(LinearLayout) findViewById(R.id.lyManageSong);
        ListView lv=(ListView) findViewById(R.id.listViewId);
        btnAddAccount=(ImageView) findViewById(R.id.btnAddAccount); 
        mDatabase = FirebaseDatabase.getInstance().getReference();
        loadAccount();

        adapter=new ListAccountAdapter(ManageAccontActivity.this,R.layout.row_account,listAccount);
        lv.setAdapter(adapter);
        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAddAcc();
            }
        });

    }

    private void DialogAddAcc() {
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_account);
        dialog.setCanceledOnTouchOutside(false);
        // map
        EditText user=(EditText) dialog.findViewById(R.id.txtUser);
        EditText pass=(EditText) dialog.findViewById(R.id.txtPass);
        EditText fullName=(EditText) dialog.findViewById(R.id.txtFullName);
        Button btnAdd=(Button) dialog.findViewById(R.id.btnAddAccount);
        Button btnCancel=(Button) dialog.findViewById(R.id.btnAddAccount);
        //event

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!user.getText().equals("")) && (!pass.getText().equals("")) && (!fullName.getText().equals(""))){
                    Account account = new Account(user.toString(), pass.toString(),fullName.toString(),null,null,null,null,null,null);
                    mDatabase.child("Accounts").push().setValue(account);
                }else {
                    Toast.makeText(ManageAccontActivity.this, "Enter Infomation", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void loadAccount() {
        mDatabase.child("Accounts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Account account = snapshot.getValue(Account.class);
                listAccount.add(account);
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
}