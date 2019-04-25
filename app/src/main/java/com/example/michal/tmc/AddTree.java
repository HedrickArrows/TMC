package com.example.michal.tmc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AddTree extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tree);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Dodaj drzewo...");
        setSupportActionBar(toolbar);

    }

}
