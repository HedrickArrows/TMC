package com.example.michal.tmc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddAlbum extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_album);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Utwórz nowy zbiór...");
        setSupportActionBar(toolbar);

        Button done = (Button) findViewById(R.id.buttonDone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text = (EditText) findViewById(R.id.albumName);
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
