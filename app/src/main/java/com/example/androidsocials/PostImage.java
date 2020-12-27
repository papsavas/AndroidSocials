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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class PostImage extends AppCompatActivity {

    CheckBox fbCb;
    CheckBox instaCb;
    CheckBox twitterCb;
    Button postButton;
    TextInputEditText caption;
    ToggleButton toggleButton;
    ShareButton fbShareButton;

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
        fbShareButton = findViewById(R.id.fb_ShareBtn);
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

            if(!(fbCb.isChecked() || instaCb.isChecked() || twitterCb.isChecked())){
                Toast.makeText(getApplicationContext(), "You have to select at least one Social Media ", Toast.LENGTH_LONG).show();
            }

            if(fbCb.isChecked()){
                facebookPost(path);
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

    private void facebookPost(String imgPath) {



        byte[] data = null;

        Bitmap bi = BitmapFactory.decodeFile(imgPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bi.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data = baos.toByteArray();

        Bundle params = new Bundle();
        params.putString("method", "photos.upload");
        params.putByteArray("picture", data);
        params.putString("caption", "description here");


        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(bi)
                .build();

        SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                .addPhoto(sharePhoto)
                .build();

        fbShareButton.setShareContent(sharePhotoContent);
    }
}