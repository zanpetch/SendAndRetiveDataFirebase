package com.example.send;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class DetailActivity extends AppCompatActivity {
    final long ONE_MEGABYTE = 1024 * 1024;

    private FirebaseDatabase database;
    private DatabaseReference dataObjects;
    private String primaryKey;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference imageRef;

    private ImageView imageView;
    private EditText nameText;
    private EditText idText;
    private Button bowsetteButton;
    private  Button peachButton;
    private Button booButton;
    private Button editButton;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        database = FirebaseDatabase.getInstance();                  //prepare Database
        dataObjects = database.getReference().child("objects");     //prepare Database
        primaryKey = getIntent().getStringExtra("key");       //primary key

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://pai-project-40b31.appspot.com/"); //prepare Storage
        imageRef = storageRef.child("images/"+primaryKey+".jpg");                               //prepare Image to read/edit/delete

        imageView = (ImageView) findViewById(R.id.imageViewDetail);
        nameText = (EditText) findViewById(R.id.nameTextDetail);
        idText = (EditText) findViewById(R.id.idTextDetail);
        bowsetteButton = (Button) findViewById(R.id.bowsetteButtonDetail);
        booButton = (Button) findViewById(R.id.booButonDetail);
        peachButton = (Button) findViewById(R.id.peachButtonDetail);
        editButton = (Button) findViewById(R.id.editButtonDetail);
        deleteButton = (Button) findViewById(R.id.deleteButtonDetail);

        imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {           //get URL from image in storage
                if (task.isSuccessful()){
                    Glide.with(DetailActivity.this).load(task.getResult()).into(imageView); //download image to show in ImageView
                } else {
                    Toast.makeText(DetailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        dataObjects.child(primaryKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Data data = dataSnapshot.getValue(Data.class);
                setDataDetail(data);                            //read database to add data in editText
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DetailActivity.this,"Failed to read data",Toast.LENGTH_SHORT).show();
            }
        });

        bowsetteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.bowsette);
            }
        });

        peachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.peach);
            }
        });

        booButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.boo);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //edit data and send to datbase and storage
                if (nameText.getText().toString().equals("") || idText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Have Empty EditText", Toast.LENGTH_SHORT).show();
                    return;
                }
                Data data = new Data(nameText.getText().toString(), Integer.parseInt(idText.getText().toString()));
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                dataObjects.child(primaryKey).setValue(data);
                uploadImage(bitmap);
                Toast.makeText(getApplicationContext(),"Edit data Complete", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataObjects.child(primaryKey).removeValue();    //delete child in datbase
                imageRef.delete();                              //delete image in storage
                Toast.makeText(getApplicationContext(),"Delete data Complete", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

    }

    private void setDataDetail(Data data) {
        nameText.setText(data.getName());
        idText.setText(""+data.getId());
    }

    public void uploadImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // unsuccess upload
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // success upload
            }
        });
    }
}
