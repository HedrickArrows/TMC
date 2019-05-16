package com.example.michal.tmc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class TreeList extends AppCompatActivity {

    ListView listView ;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        final String message = bundle.getString("message");

        setContentView(R.layout.activity_tree_list);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Zbiór: " + message);

        setSupportActionBar(toolbar);

        db = openOrCreateDatabase("TREES",MODE_PRIVATE,null);
        ArrayList<String> arrTblNames = new ArrayList<String>();
        //db.execSQL("INSERT INTO " + message.toString() +" (LON, LAT, IMAGE) values ('1','2', 'test')");
        Cursor c = db.rawQuery("SELECT * FROM " + message.toString()+";", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                arrTblNames.add( c.getString( c.getColumnIndex("ID")) );
                c.moveToNext();
            }
        }

        listView = (ListView) findViewById(R.id.listTree);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, arrTblNames);

        listView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent = new Intent(TreeList.this, AddTree.class);
                intent.putExtra("message", message);
                startActivity(intent);
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.saveToCsv);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExternalStorageWritable();
                File exportDir = new File(Environment.getExternalStorageDirectory(), "TMC");
                if (!exportDir.exists())
                {
                    exportDir.mkdirs();
                }

                File file = new File(exportDir, message.toString()+".csv");
                try
                {
                    file.createNewFile();
                    CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                    Cursor curCSV = db.rawQuery("SELECT * FROM "+message.toString(),null);
                    csvWrite.writeNext(curCSV.getColumnNames());
                    while(curCSV.moveToNext())
                    {
                        //Which column you want to exprort
                        String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2), curCSV.getString(3)};
                        csvWrite.writeNext(arrStr);
                    }
                    csvWrite.close();
                    curCSV.close();
                }
                catch(Exception sqlEx)
                {
                    Log.e("SaveToDb", sqlEx.getMessage(), sqlEx);
                }

            }
        });
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
