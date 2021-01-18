package com.example.androidsocials;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;

public class CreateStatus extends AppCompatActivity {
    TextInputEditText caption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_status);
        caption = (TextInputEditText) findViewById(R.id.captionTxt);
    }
}