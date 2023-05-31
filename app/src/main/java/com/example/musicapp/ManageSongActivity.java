
package com.example.musicapp;

        import androidx.activity.result.ActivityResult;
        import androidx.activity.result.ActivityResultCallback;
        import androidx.activity.result.ActivityResultLauncher;
        import androidx.activity.result.contract.ActivityResultContracts;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.content.ContextCompat;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Environment;
        import android.provider.Settings;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.Query;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;

        import java.io.Serializable;
        import java.util.ArrayList;
        import java.util.List;

public class ManageSongActivity extends AppCompatActivity {
    DatabaseReference mDatabase;
    List<Song> songList=new ArrayList<Song>();
    ListView lvSong;
    ListViewSongAdapter adapter=null;
    LinearLayout lyMore, lyMangeAccount;
    EditText editTextSongName, editTextImgName,editTextLinkName,editTextAuthorName;
    Button addSong;
    ActivityResultLauncher<Intent> imagePickerLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        setTitle("Admin");
        addSong = findViewById(R.id.addSong);
//        requestStoragePermission();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //ánh xạ
        anhXa();
        //Load data
        loadSongHistorys();

        adapter=new ListViewSongAdapter(ManageSongActivity.this,R.layout.item_manage_song,songList);
        lvSong.setAdapter(adapter);
        // Create the launcher for image picker
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Lấy URI của hình ảnh đã chọn
                    Uri imageUri = result.getData().getData();
                    // Lưu hình ảnh lên Firebase Realtime Database
                    saveImageToDatabase(imageUri);
                }
            }
        });
        //event list View
        lvSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song song = (Song) lvSong.getItemAtPosition(i);

                startActivity(new Intent(ManageSongActivity.this,PlayerActivity.class)
                        .putExtra("MyListSong", (Serializable) songList)
                        .putExtra("pos",i).putExtra("Activity","com.example.musicapp.MainActivity"));

            }
        });
                addSong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                                showCreateSongDialog();
                    }
                });



        lyMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManageSongActivity.this, MoreActivity.class));
            }
        });
        lyMangeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManageSongActivity.this, ManageAccontActivity.class));
            }
        });

    }

    private void loadSongHistorys() {
        mDatabase.child("Song").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Song songs=snapshot.getValue(Song.class);
                songList.add(songs);
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
    public void updateNewSong(Song song,String i){
        DatabaseReference accounts = mDatabase.child("Song");
        accounts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                accounts.child(i).setValue(song);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }
    public void addSong (Song song){
        DatabaseReference accounts = mDatabase.child("Song");
        accounts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount(); // Lấy số lượng child nodes hiện tại
                String key = String.format("%02d", count + 1); // Định dạng số thứ tự
                // Thêm mới vào playlist
                accounts.child(key).setValue(song);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }
    public void removeSong(Song song){
        DatabaseReference songs = mDatabase.child("Song");
        songs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount(); // Lấy số lượng child nodes hiện tại
                String key = String.format("%02d", count + 1); // Định dạng số thứ tự
                for(int i = 1 ; i<=count;i++){
                    // Lấy DatabaseReference đến nút cha "song/01"
                    String parentPath = "Song/0"+i;
                    DatabaseReference parentRef = FirebaseDatabase.getInstance().getReference(parentPath);

                    // Tạo một Query để tìm nút có trường "name" có giá trị là "123"
                    Query query = parentRef.orderByChild("nameSong").equalTo(song.getNameSong());

                    // Thực hiện truy vấn
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Duyệt qua các nút tìm thấy
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                // Xóa nút tìm thấy
                                childSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Xử lý lỗi (nếu có)
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }
    private void showCreateSongDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Song");

        // Tạo layout cho dialog bằng mã Java
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        editTextSongName = new EditText(this);
        editTextSongName.setHint("Enter Song name");
        layout.addView(editTextSongName);
        Button selectImageButton = new Button(this);
        selectImageButton.setText("Select Image");
        layout.addView(selectImageButton);
       editTextAuthorName = new EditText(this);
        editTextAuthorName.setHint("Enter Author name");
        layout.addView(editTextAuthorName);
        editTextImgName = new EditText(this);
        editTextImgName.setHint("Enter Img name");
        layout.addView(editTextImgName);
        editTextLinkName = new EditText(this);
        editTextLinkName.setHint("Enter link name");
        layout.addView(editTextLinkName);
        builder.setView(layout);
        // Tạo ActivityResultLauncher trước khi set onClickListener cho nút select image
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một Intent để chọn hình ảnh từ bộ nhớ hình ảnh
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                // Sử dụng imagePickerLauncher để nhận kết quả từ Intent
                imagePickerLauncher.launch(intent);
            }
        });
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Song song = new Song(editTextImgName.getText().toString(),editTextLinkName.getText().toString()
                        ,editTextSongName.getText().toString(),editTextAuthorName.getText().toString());
                addSong(song);
                Toast.makeText(ManageSongActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void anhXa() {
        lvSong=(ListView) findViewById(R.id.listViewId);
        lyMore=(LinearLayout) findViewById(R.id.lyMore);
        lyMangeAccount=(LinearLayout) findViewById(R.id.lyManageAcount);

    }
    private void saveImageToDatabase(Uri imageUri) {
        // Tạo một StorageReference đến vị trí trong Firebase Storage để lưu hình ảnh
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("img").child(imageUri.getLastPathSegment());

        // Tải lên hình ảnh lên Firebase Storage
        UploadTask uploadTask = storageRef.putFile(imageUri);

        // Xử lý sự kiện khi quá trình tải lên hoàn thành
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    // Lấy URL của hình ảnh đã tải lên
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();

                            // Lưu đường dẫn của hình ảnh vào Firebase Realtime Database
                           editTextImgName.setText(imageUrl);

//                            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("song/01");
//                            databaseRef.child(imageName).setValue(imageUrl);

                            // Hiển thị thông báo thành công
                            Toast.makeText(ManageSongActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Hiển thị thông báo lỗi
                    Toast.makeText(ManageSongActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void updateSong(Song song,String i){

        AlertDialog.Builder builder = new AlertDialog.Builder(ManageSongActivity.this);
        builder.setTitle("Create Song");

        // Tạo layout cho dialog bằng mã Java
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        editTextSongName = new EditText(this);
        editTextSongName.setText(song.getNameSong());
        layout.addView(editTextSongName);
        Button selectImageButton = new Button(this);
        selectImageButton.setText("Select Image");
        layout.addView(selectImageButton);
        editTextAuthorName = new EditText(this);
        editTextAuthorName.setText(song.getAuthor());
        layout.addView(editTextAuthorName);
        editTextImgName = new EditText(this);
        editTextImgName.setText(song.getImg());
        layout.addView(editTextImgName);
        editTextLinkName = new EditText(this);
        editTextLinkName.setText(song.getLink());
        layout.addView(editTextLinkName);
        builder.setView(layout);
        // Tạo ActivityResultLauncher trước khi set onClickListener cho nút select image
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một Intent để chọn hình ảnh từ bộ nhớ hình ảnh
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                // Sử dụng imagePickerLauncher để nhận kết quả từ Intent
                imagePickerLauncher.launch(intent);
            }
        });
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Song song = new Song(editTextImgName.getText().toString(),editTextLinkName.getText().toString()
                        ,editTextSongName.getText().toString(),editTextAuthorName.getText().toString());
                updateSong(song,i);
                Toast.makeText(ManageSongActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}