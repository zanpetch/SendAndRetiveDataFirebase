package com.example.send;

/* Firebase USE
   - firebase-core
   - firebase-database
   - firebase-storage
   - firebase-ui-storage
*/

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference dataObjects;

    private ListView listDatas;
    private List<Data> datas = new ArrayList<>();
    private List<String> keys = new ArrayList<>();
    private FloatingActionButton floatButton;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        dataObjects = database.getReference().child("objects");         //Database : Table objects

        adapter = new ListAdapter(MainActivity.this, datas);
        listDatas = (ListView) findViewById(R.id.listView);

        dataObjects.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datas.clear();
                keys.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){      // child in Table objects
                    Data data = ds.getValue(Data.class);                // read child objects
                    datas.add(data);                                    // add child objects to datas --> use in ListView
                    keys.add(ds.getKey());                              // add key child objects to keys --> use to send primary key
                }
                listDatas.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,"Failed to read data",Toast.LENGTH_SHORT).show();
            }
        });

        listDatas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("key",keys.get(position));      // send primary key of data to DetailActivity
                startActivity(intent);
            }
        });

        floatButton =  (FloatingActionButton) findViewById(R.id.floatButton);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });
    }
}
