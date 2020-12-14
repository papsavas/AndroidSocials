package com.example.androidsocials;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.io.File;

public class PostImage extends AppCompatActivity {

    public static CheckBox fbCb;
    public static CheckBox instaCb;
    public static CheckBox twitterCb;
    public static Button postButton;
    //textbox
    //image

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

        ImageView imageView = findViewById(R.id.imageView);

        String fName = getIntent().getStringExtra(MainActivity.MOVE_IMAGE);
        String path = Environment.getExternalStorageDirectory() +"/" + fName + ".png";
        Bitmap bm = BitmapFactory.decodeFile(path);
        imageView.setImageBitmap(bm);



        fbCb.setOnClickListener(v -> Log.d(SWITCH_TAG, "FB switch state changed"));

        instaCb.setOnClickListener(v -> Log.d(SWITCH_TAG, "Insta switch state changed"));

        twitterCb.setOnClickListener(v -> Log.d(SWITCH_TAG, "Twitter switch state changed"));


        postButton.setOnClickListener(view -> Log.d(SWITCH_TAG,"*posting image...*"));

        findViewById(R.id.btnPost).setOnClickListener(v -> {
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