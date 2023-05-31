package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageAccontActivity extends AppCompatActivity {
    ListAccountAdapter adapter=null;
    List<Account> listAccount=new ArrayList<Account>();
    LinearLayout lyManageSong, lyMore;
    DatabaseReference mDatabase;
    ImageView btnAddAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_accont);
        LinearLayout lyMore=(LinearLayout) findViewById(R.id.lyMoreMan);
        LinearLayout lyManageSong=(LinearLayout) findViewById(R.id.lyManageSong);
        ListView lv=(ListView) findViewById(R.id.listViewId);
        lyMore=(LinearLayout) findViewById(R.id.lyMoreMan);
        lyManageSong=(LinearLayout) findViewById(R.id.lyManageSong);
        btnAddAccount=(ImageView) findViewById(R.id.btnAddAccount); 
        mDatabase = FirebaseDatabase.getInstance().getReference();
        loadAccount();

        adapter=new ListAccountAdapter(ManageAccontActivity.this,R.layout.row_account,listAccount);
        lv.setAdapter(adapter);
        adapter.reload();
        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAddAcc();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DialogEditAcc(i+1,listAccount);
            }
        });
        lyManageSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManageAccontActivity.this, ManageSongActivity.class));
            }
        });
        lyMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManageAccontActivity.this, MoreActivity.class));
            }
        });


    }
    private void DialogEditAcc(int i, List<Account> list) {
        Dialog dialog=new Dialog(ManageAccontActivity.this);
        dialog.setContentView(R.layout.dialog_add_account);
        dialog.setCanceledOnTouchOutside(false);
        // map
        EditText user=(EditText) dialog.findViewById(R.id.txtUser);
        EditText pass=(EditText) dialog.findViewById(R.id.txtPass);
        EditText fullName=(EditText) dialog.findViewById(R.id.txtFullName);
        Button btnAdd=(Button) dialog.findViewById(R.id.btnAddAccount);
        Button btnCancel=(Button) dialog.findViewById(R.id.btnCancelAddAccount);


        user.setText(list.get(i).getEmail());
        pass.setText(list.get(i).getPass());
        fullName.setText(list.get(i).getName());
        //event
        btnAdd.setText("Update");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!user.getText().equals("")) && (!pass.getText().equals("")) && (!fullName.getText().equals(""))){
                    Account account = listAccount.get(i);
                    mDatabase.child("Accounts").child(String.valueOf(i)).setValue(account);
                    dialog.dismiss();
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
    private void DialogAddAcc() {
        Dialog dialog=new Dialog(ManageAccontActivity.this);
        dialog.setContentView(R.layout.dialog_add_account);
        dialog.setCanceledOnTouchOutside(false);
        // map
        EditText user=(EditText) dialog.findViewById(R.id.txtUser);
        EditText pass=(EditText) dialog.findViewById(R.id.txtPass);
        EditText fullName=(EditText) dialog.findViewById(R.id.txtFullName);
        Button btnAdd=(Button) dialog.findViewById(R.id.btnAddAccount);
        Button btnCancel=(Button) dialog.findViewById(R.id.btnCancelAddAccount);
        //event

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!user.getText().equals("")) && (!pass.getText().equals("")) && (!fullName.getText().equals(""))){
                    Account account = new Account(user.getText().toString(), pass.getText().toString(),fullName.getText().toString(),"null","null","null",new ArrayList<String>(),"null","null");
                    mDatabase.child("Accounts").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long count = snapshot.getChildrenCount();
                            String key = String.format("%02d", count + 1);
                            mDatabase.child("Accounts").child(key).setValue(account);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    dialog.dismiss();
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
    public void reSetView(){
        adapter.notifyDataSetChanged();
    }
}