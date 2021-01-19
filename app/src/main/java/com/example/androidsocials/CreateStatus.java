package com.example.androidsocials;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.reflect.Method;

public class CreateStatus extends AppCompatActivity {
    TextInputEditText caption;
    ShareButton fbButton;
    Button twitterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_status);
        caption = (TextInputEditText) findViewById(R.id.captionTxt);
        fbButton = findViewById(R.id.fb_ShareBtn);
        twitterButton = findViewById(R.id.twitterBtn);
        IntentHandler intentHandler = new IntentHandler(CreateStatus.this);
        /*SharePhotoContent sharePhotoContent = intentHandler.postToFacebook("text");
        fbButton.setShareContent(sharePhotoContent);
        */
        fbButton.setOnClickListener(v -> {
            copyText("fbStatus", caption);
        });

        twitterButton.setOnClickListener(v -> {
            /*copyText("twitterStatus", caption);*/
            if(caption.getText() == null)
                Toast.makeText(CreateStatus.this, "You need to add a status", Toast.LENGTH_LONG);
            else
                intentHandler.launchTwitter(caption.getText().toString());
        });
    }


    private void copyText(String label, TextInputEditText caption){
        if(caption.getText() == null)
            Toast.makeText(CreateStatus.this, "You need to add a status", Toast.LENGTH_LONG);
        else{
            copyToClipBoard(label, caption.getText().toString());
        }
    }

    private void copyToClipBoard(String label, String str) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, str);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(CreateStatus.this, "Status saved to clip board", Toast.LENGTH_SHORT).show();
    }
}