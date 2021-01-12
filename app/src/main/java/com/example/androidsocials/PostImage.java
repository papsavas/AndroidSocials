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
import com.facebook.login.LoginManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import twitter4j.*;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.UploadedMedia;
import twitter4j.auth.Authorization;
import twitter4j.conf.ConfigurationBuilder;

public class PostImage extends AppCompatActivity {

    TextInputEditText caption;
    ToggleButton modeButton;
    Button fbShareButton;
    Button twitterBtn;
    static String imagePath;


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
        imagePath = Environment.getExternalStorageDirectory() +"/" + fName + ".png";
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bm);



        twitterBtn.setOnClickListener(v -> {
            //tempTwitter();
            TwitterHandler twHandler = new TwitterHandler(
                        getString(R.string.twitter_api_key),
                        getString(R.string.twitter_api_key_secret),
                        getString(R.string.twitter_access_token),
                        getString(R.string.twitter_access_token_secret)
                    );
            twHandler.execute();

        });

        modeButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
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
        SharePhotoContent sharePhotoContent = postToFacebook(imagePath);
        //fbShareButton.setShareContent(sharePhotoContent);

        fbShareButton.setOnClickListener(v->{
            postFBwithGraphAPI();

        });



    }

    private void tempTwitter(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(getString(R.string.twitter_api_key));
        cb.setOAuthConsumerSecret(getString(R.string.twitter_api_key_secret));
        cb.setOAuthAccessToken(getString(R.string.twitter_access_token));
        cb.setOAuthAccessTokenSecret(getString(R.string.twitter_access_token_secret));
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        File file = new File(imagePath);
        StatusUpdate status = new StatusUpdate("testing #blessed");
        status.setMedia(file); // set the image to be uploaded here.
        try {
            //twitter.updateStatus("First Tweet");
            twitter.updateStatus(status);
        } catch (TwitterException e) {

            e.printStackTrace();
        }
    }

    private void postFBwithGraphAPI() {
        GraphRequest request = null;
        try {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if(!accessToken.getPermissions().contains(Arrays.asList("pages_read_engagement","pages_manage_posts"))) {
                Log.d("PERMS", "~~~~~~~~NOT CONTAINED~~~~~~~~~~~~~~~~");
                LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("pages_read_engagement","pages_manage_posts"));
            }
            request = GraphRequest.newPostRequest(
                   accessToken,
                    "/105083264804153/feed",
                    new JSONObject("{\"message\":\"Awesome!\"}"),
                    response -> Log.d("FB_SHARE", String.valueOf(response.getRawResponse())));
            request.executeAsync();
        } catch (JSONException e) {
            e.printStackTrace();
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