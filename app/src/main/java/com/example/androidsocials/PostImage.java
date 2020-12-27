package com.example.androidsocials;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;


import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.UploadedMedia;
import twitter4j.conf.ConfigurationBuilder;

public class PostImage extends AppCompatActivity {

    TextInputEditText caption;
    ToggleButton modeButton;
    ShareButton fbShareButton;
    Button twitterBtn;
    Twitter twitter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image);

        modeButton = (ToggleButton) findViewById(R.id.toggleButton);
        fbShareButton = findViewById(R.id.fb_ShareBtn);
        twitterBtn = findViewById(R.id.twitterBtn);
        caption = (TextInputEditText) findViewById(R.id.captionTxt);
        TextInputLayout textLayout = (TextInputLayout) findViewById(R.id.textInputLayout);

        ImageView imageView = findViewById(R.id.imageView);

        String fName = getIntent().getStringExtra(MainActivity.MOVE_IMAGE);
        String imagePath = Environment.getExternalStorageDirectory() +"/" + fName + ".png";
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bm);

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(String.valueOf(R.string.twitter_consumer_key));
        cb.setOAuthConsumerSecret(String.valueOf(R.string.twitter_consumer_secret));
        cb.setOAuthAccessToken(String.valueOf(R.string.twitter_access_token));
        cb.setOAuthAccessTokenSecret(String.valueOf(R.string.twitter_access_token_secret));

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();

        twitterBtn.setOnClickListener(v -> postToTwitter(imagePath));

        modeButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                Log.d("POST_AS", "Posting on Story");
                caption.setText(null);
                textLayout.setVisibility(View.GONE);
                //fbShareButton.setText("Post Story");
                fbShareButton.setTextKeepState("Post Story");
            }
            else{
                Log.d("POST_AS", "Posting on Timeline");
                textLayout.setVisibility(View.VISIBLE);
                //fbShareButton.setText("Post Picture");
                fbShareButton.setTextKeepState("Post Picture");
            }
        });
        SharePhotoContent sharePhotoContent = postToFacebook(imagePath);
        fbShareButton.setShareContent(sharePhotoContent);

    }




    private void postToTwitter(String imagePath){

        try
        {
            StatusUpdate status = new StatusUpdate("Τεστ Τεξτ");
            status.setMedia(new File(imagePath));
            //twitter.updateStatus(status);

            Log.d("FAVORITES", twitter.getFavorites().toString());
        }
        catch (TwitterException te)
        {
            Log.e("postToTwitter", "Error: "+ te.getMessage());
        }

    }

    private void instaPost() {

    }

    private SharePhotoContent postToFacebook(String imgPath) {
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
                //.setCaption("Test Caption")
                .build();

        return new SharePhotoContent.Builder()
                .addPhoto(sharePhoto)
                .build();


    }
}