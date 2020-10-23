package com.example.animdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.arch.core.util.Function;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class HomeActivity extends AppCompatActivity{

    FirebaseAuth mAuth;
    CircleImageView profilepic;
    Button skipbtn;
    Button uploadpic;
    Button savebtn;
    EditText fname, lname, phone, emailet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        skipbtn = findViewById(R.id.skipbtn);
        savebtn = findViewById(R.id.savebtn);
        uploadpic = findViewById(R.id.profilepicbtn);
        profilepic = findViewById(R.id.profile_image);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PlacesActivity.class);
                startActivity(intent);
            }
        });



        uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CROP, true);//default is false
                startActivityForResult(intent, 1213);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
            profilepic.setImageBitmap(selectedImage);

        }
    }
    private void uploadImage() {

        final ProgressDialog pd = new ProgressDialog(getApplicationContext());
        pd.setMessage("Uploading...");
        pd.show();
        if (croppedImageUri != null) {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("users").child(uid);

            storageReference.putFile(croppedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            firebaseDatabase.getInstance().getReference().child("profilephoto").child(uid).child("URLIMAGE").setValue(url);
                            Log.d("DownloadUrl", url);
                            pd.dismiss();

                            Toast.makeText(getApplicationContext(), "Image Upload Successfully!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}