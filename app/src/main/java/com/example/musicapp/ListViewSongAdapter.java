package com.example.musicapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListViewSongAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Song> listSong;
    DatabaseReference mDatabase;

    public ListViewSongAdapter(Context context, int layout, List<Song> listSong) {
        this.context = context;
        this.layout = layout;
        this.listSong = listSong;
    }

    public int getCount() {
        return listSong.size();
    }

    @Override
    public Object getItem(int i) {
        return listSong.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private class ViewHolder{
        ImageView img,btn, remove;
        TextView nameSong;
        TextView author;

    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder=new ViewHolder();;
        if(view==null){

            view=inflater.inflate(layout,null);

            //anh xa
            holder.img      =(ImageView) view.findViewById(R.id.imgSong);
            holder.nameSong =(TextView) view.findViewById(R.id.nameSong);
            holder.author   =(TextView) view.findViewById(R.id.nameAuthor);
            holder.btn      = (ImageView) view.findViewById(R.id.icAdd);
            if(view.findViewById(R.id.icRemove)!= null){
                holder.remove = (ImageView) view.findViewById(R.id.icRemove);
            }
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }

        //gan gia tri
        Song song =listSong.get(i);
        holder.nameSong.setSelected(true);
        holder.nameSong.setText(song.getNameSong());
        holder.author.setText(song.getAuthor());
        if(holder.img != null){
            Picasso.get().load(song.getImg()).into(holder.img);
        }

        if(holder.remove!= null){
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDeleteConfirmationDialog(i+1);
                    notifyDataSetChanged();
                }
            });
        }
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAddPlayList();
            }
        });


        return view;
    }

    private void DialogAddPlayList() {
    }
    private void showDeleteConfirmationDialog(int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Song");
        builder.setMessage("Are you sure you want to delete this song?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform the delete operation
                deleteSong(i);
                updateListView();
                notifyDataSetChanged();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel the delete operation
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void deleteSong(int index) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference songs = mDatabase.child("Song");
        String indexString = "";
        if (index < 10) {
            indexString = "0" + index;
        } else {
            indexString = String.valueOf(index);
        }
        songs.child(indexString).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Remove Success", Toast.LENGTH_SHORT).show();
                updateListView();
                notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Remove False because " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateListView() {
        // Lấy danh sách dữ liệu từ cơ sở dữ liệu Firebase và cập nhật lại adapter của ListView
        DatabaseReference songsRef = mDatabase.child("Song");
        songsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Xóa toàn bộ dữ liệu hiện tại trong danh sách
                listSong.clear();

                // Duyệt qua tất cả các node con và thêm vào danh sách
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Song song = snapshot.getValue(Song.class);
                    listSong.add(song);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xảy ra lỗi trong quá trình truy vấn cơ sở dữ liệu
            }
        });
    }
}
