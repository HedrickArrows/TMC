package com.example.michal.tmc;

import android.content.Intent;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class AddAlbum extends AppCompatActivity {

    ArrayList<String> cols;
    RecyclerView rView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cols = new ArrayList<String>();
        cols.add("współrzędne");
        cols.add("zdjęcie");

        rView = (RecyclerView) findViewById(R.id.cols);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, cols);
       

        setContentView(R.layout.activity_add_album);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Utwórz nowy zbiór...");
        setSupportActionBar(toolbar);

        Button done = (Button) findViewById(R.id.buttonDone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text = (EditText) findViewById(R.id.albumName);


                SQLiteDatabase db = openOrCreateDatabase("TREES",MODE_PRIVATE,null);

                Intent intent = new Intent(AddAlbum.this, TreeList.class);
                intent.putExtra("message", text.getText().toString());
                startActivity(intent);
            }
        });

        Button cancel = (Button) findViewById(R.id.buttonCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
