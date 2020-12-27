package com.example.androidsocials;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

public class PostImage extends AppCompatActivity {

    CheckBox fbCb;
    CheckBox instaCb;
    CheckBox twitterCb;
    Button postButton;
    TextInputEditText caption;
    ToggleButton toggleButton;

    public static final String POST_TAG = "POST";
    public static final String SWITCH_TAG = "SWITCH";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image);

        fbCb = (CheckBox) findViewById(R.id.fbCb);
        instaCb = (CheckBox) findViewById(R.id.instaCb);
        twitterCb = (CheckBox) findViewById(R.id.twitterCb);
        postButton = (Button) findViewById(R.id.btnPost);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        caption = (TextInputEditText) findViewById(R.id.captionTxt);
        TextInputLayout textLayout = (TextInputLayout) findViewById(R.id.textInputLayout);

        ImageView imageView = findViewById(R.id.imageView);

        String fName = getIntent().getStringExtra(MainActivity.MOVE_IMAGE);
        String path = Environment.getExternalStorageDirectory() +"/" + fName + ".png";
        Bitmap bm = BitmapFactory.decodeFile(path);
        imageView.setImageBitmap(bm);


        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked){
                Log.d("POST_AS", "Posting on Story");
                //disableText();
                caption.setText(null);
                textLayout.setVisibility(View.GONE);
                caption.setVisibility(View.GONE);

            }
            else{
                Log.d("POST_AS", "Posting on Timeline");
                //enableText();
                textLayout.setVisibility(View.VISIBLE);
                caption.setVisibility(View.VISIBLE);
            }
        });

        fbCb.setOnClickListener(v -> Log.d(SWITCH_TAG, "FB switch state changed"));
        instaCb.setOnClickListener(v -> Log.d(SWITCH_TAG, "Insta switch state changed"));
        twitterCb.setOnClickListener(v -> Log.d(SWITCH_TAG, "Twitter switch state changed"));
        postButton.setOnClickListener(view -> {
            Log.d("PostButton", "*Posting Image*...");
            if(fbCb.isChecked()){
                //facebookPost();
                Log.d(POST_TAG, "Posting on facebook...");
            }

            if(instaCb.isChecked()){
                //instaPost();
                Log.d(POST_TAG, "Posting on instagram...");
            }

            if(twitterCb.isChecked()){
                //twitterPost();
                Log.d(POST_TAG, "Posting on twitter...");
            }

        });

    }




    private void twitterPost() {
    }

    private void instaPost() {

    }

    private void facebookPost() {
        String type = "image/*";
        String filename = "/myPhoto.jpg";
        String mediaPath = "/path"; //Environment.getExternalStorageDirectory() + filename;


        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);

        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);

        // Broadcast the Intent.
        startActivity(Intent.createChooser(share, "Share to"));
    }
}