package com.rogmax.amirjaber.fundamentals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class BundleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bundle);

        TextView textViewIntent = findViewById(R.id.tv_recieved_string);

        Intent intent = getIntent();
        String bundle = intent.getStringExtra("data");
        textViewIntent.setText(bundle);

    }
}
