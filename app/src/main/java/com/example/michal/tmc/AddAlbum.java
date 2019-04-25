package com.example.michal.tmc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ListView;

import java.util.ArrayList;

public class AddAlbum extends AppCompatActivity {

    ArrayList<String> columns;
    ListView rView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_album);

        columns = new ArrayList<>();
        columns.add("wspolrzedne");
        columns.add("zdjecie");

        rView = (ListView) findViewById(R.id.cols);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, columns);


        rView.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Utwórz nowy zbiór...");
        setSupportActionBar(toolbar);

        Button done = (Button) findViewById(R.id.buttonDone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text = (EditText) findViewById(R.id.albumName);


                SQLiteDatabase db = openOrCreateDatabase("TREES",MODE_PRIVATE,null);

                String extraCols = "";

                for(String s : columns){
                    if(s != "wspolrzedne" && s != "zdjecie")
                    extraCols += "," + s + " TEXT";
                }

                String sqlCreateTable = "CREATE TABLE " + text.getText().toString() +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "LON TEXT," +
                        "LAT TEXT," +
                        "IMAGE BLOB" +
                        extraCols +
                        "); ";
                db.execSQL(sqlCreateTable);


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

        final Context c = this;

        Button add = (Button) findViewById(R.id.newColButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(c);

                alert.setTitle("Dodaj nową kolumnę");
                alert.setMessage("Nazwa:");

                // Set an EditText view to get user input
                final EditText input = new EditText(c);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        columns.add(input.getText().toString());
                        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(c,
                         //       android.R.layout.simple_list_item_1, android.R.id.text1, cols);

                        //rView.setAdapter(adapter);

                        //rView.invalidateViews();
                    }
                });

                alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });
    }

}
