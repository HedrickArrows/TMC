package com.example.michal.tmc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AddAlbum extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_album);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Utwórz nowy zbiór...");
        setSupportActionBar(toolbar);

    }

}
