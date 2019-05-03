package com.example.send;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class AddActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private ImageView imageView;
    private EditText nameText;
    private EditText idText;
    private Button enterButton;
    private Button bowsetteButton;
    private Button peachButton;
    private Button booButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("objects");  //Database : Table objects

        imageView = (ImageView) findViewById(R.id.imageViewAdd);
        nameText = (EditText) findViewById(R.id.nameTextAdd);
        idText = (EditText) findViewById(R.id.idTextAdd);
        enterButton = (Button) findViewById(R.id.enterButtonAdd);
        bowsetteButton = (Button) findViewById(R.id.bowsetteButtonAdd);
        peachButton = (Button) findViewById(R.id.peachButtonAdd);
        booButton = (Button) findViewById(R.id.booButonAdd);

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameText.getText().toString().equals("") || idText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Have Empty Value", Toast.LENGTH_SHORT).show();
                    return;
                }
                Data data = new Data(nameText.getText().toString(), Integer.parseInt(idText.getText().toString())); //prepare data send to database
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();                              //prepare image send to database
                String primaryKey = myRef.push().getKey();      //save primaryKey of new data
                myRef.child(primaryKey).setValue(data);         //add data to database(Realtime)
                uploadImage(bitmap,primaryKey);                 //add image to storage
                Toast.makeText(getApplicationContext(),"Add data Complete", Toast.LENGTH_SHORT).show();

                finish();
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

    }

    public void uploadImage(Bitmap bitmap, String primaryKey) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        FirebaseStorage storage = FirebaseStorage.getInstance();    //prepare storage
        StorageReference storageRef = storage.getReferenceFromUrl("gs://pai-project-40b31.appspot.com/"); //prepare storage
        StorageReference imageRef = storageRef.child("images/"+primaryKey+".jpg"); //prepare storage

        UploadTask uploadTask = imageRef.putBytes(data);            //upload image to storage --> images/(primarykey data).jpg
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // unsuccess upload
                Toast.makeText(getApplicationContext(),"Unsuccess Upload", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // success upload
            }
        });
    }
}
