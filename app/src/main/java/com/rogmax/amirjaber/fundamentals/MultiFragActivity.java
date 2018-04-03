package com.rogmax.amirjaber.fundamentals;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.rogmax.amirjaber.fundamentals.fragments.MultiTop;
import com.rogmax.amirjaber.fundamentals.fragments.MultiTwo;

public class MultiFragActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_frag);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        MultiTop multiTop = new MultiTop();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.firstLayout, multiTop, multiTop.getTag())
                .commit();

        MultiTwo multiTwo = new MultiTwo();
        fragmentManager.beginTransaction()
                .replace(R.id.secondLayout, multiTwo, multiTwo.getTag())
                .commit();

    }

}
