package com.example.androidsocials;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;


import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class PostImage extends AppCompatActivity {

    TextInputEditText caption;
    ToggleButton storyMode;
    ShareButton fbShareButton;
    Button twitterBtn;
    Button instaButton;
    public static String imagePath;
    public static String captionText;
    IntentHandler intentHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentHandler = new IntentHandler(PostImage.this);
        setContentView(R.layout.activity_post_image);
        storyMode = (ToggleButton) findViewById(R.id.toggleButton);
        fbShareButton = findViewById(R.id.fb_ShareBtn);
        twitterBtn = findViewById(R.id.twitterBtn);
        instaButton = findViewById(R.id.instaBtn);
        caption = (TextInputEditText) findViewById(R.id.captionTxt);
        TextInputLayout textLayout = (TextInputLayout) findViewById(R.id.textInputLayout);
        ImageView imageView = findViewById(R.id.imageView);
        String fName = getIntent().getStringExtra(MainActivity.MOVE_IMAGE);
        imagePath = Environment.getExternalStorageDirectory() +"/" + fName + ".png";
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bm);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        twitterBtn.setOnClickListener(v -> {
            if(storyMode.isChecked()){
                //intentHandler.launchTwitter(captionText);
            }
            else{
                TwitterHandler twHandler = new TwitterHandler(
                        getString(R.string.twitter_api_key),
                        getString(R.string.twitter_api_key_secret),
                        getString(R.string.twitter_access_token),
                        getString(R.string.twitter_access_token_secret)
                );
                captionText = Objects.requireNonNull(caption.getText()).toString();
                twHandler.execute();
            }


        });

        instaButton.setOnClickListener(v-> {
            if(storyMode.isChecked()){
                Log.d("INSTA", "Insta Story");
                intentHandler.instaStory(imagePath);
            }
            else{
                Log.d("INSTA", "Insta Post");

            }
        });

        storyMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                Log.d("POST_AS", "Posting on Story");
                caption.setText(null);
                textLayout.setVisibility(View.GONE);
                fbShareButton.setTextKeepState("Post Story");
            }
            else{
                Log.d("POST_AS", "Posting on Timeline");
                textLayout.setVisibility(View.VISIBLE);
                fbShareButton.setTextKeepState("Post Picture");
            }
        });

        SharePhotoContent sharePhotoContent = intentHandler.postPictureToFacebook(imagePath);
        fbShareButton.setShareContent(sharePhotoContent);
/*
        fbShareButton.setOnClickListener(v->{
            intentHandler.postPictureToFacebook(imagePath);

        });*/
    }



}