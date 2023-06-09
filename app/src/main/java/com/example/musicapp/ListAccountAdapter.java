package com.example.musicapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListAccountAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Account> listAccount;
    DatabaseReference mDatabase;

    public ListAccountAdapter(Context context, int layout, List<Account> listAccount) {
        this.context = context;
        this.layout = layout;
        this.listAccount = listAccount;
    }

    public int getCount() {
        return listAccount.size();
    }

    @Override
    public Object getItem(int i) {
        return listAccount.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private class ViewHolder{

        TextView nameAccount;
        ImageView btnDelete;


    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder=new ViewHolder();;
        if(view==null){
            view=inflater.inflate(layout,null);
            //anh xa
            holder.nameAccount    =(TextView) view.findViewById(R.id.nameAccount);
            holder.btnDelete    =(ImageView) view.findViewById(R.id.btnDeleteAc);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }

        //gan gia tri
            holder.nameAccount.setText(listAccount.get(i).getEmail());
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String index="";
                    if (i<9){
                        index+="0"+(i+1);
                    }else {
                        index+=(i+1);
                    }
                    DialogMsg("You want delete account ?",index);
                    notifyDataSetChanged();
                }
            });



        return view;
    }
    private void DialogMsg(String msg,String id){
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setTitle("Notification");
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Accounts").child(id).removeValue();
                notifyDataSetChanged();
                alertDialog.setCancelable(true);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.setCancelable(true);
            }
        });

        alertDialog.show();
    }
    public void reload(){
        notifyDataSetChanged();
    }

}
