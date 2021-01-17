package com.example.androidsocials;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class PostImage extends AppCompatActivity {

    TextInputEditText caption;
    ToggleButton storyMode;
    Button fbShareButton;
    Button twitterBtn;
    Button instaButton;
    static String imagePath;
    static String captionText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                postFleet();
            }
            else{
                TwitterHandler twHandler = new TwitterHandler(
                        getString(R.string.twitter_api_key),
                        getString(R.string.twitter_api_key_secret),
                        getString(R.string.twitter_access_token),
                        getString(R.string.twitter_access_token_secret)
                );
                captionText = caption.getText().toString();
                twHandler.execute();
            }

        });

        instaButton.setOnClickListener(v-> {
            if(storyMode.isChecked()){
                Log.d("INSTA", "Insta Story");
                instaStory(imagePath);
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
        SharePhotoContent sharePhotoContent = postToFacebook(imagePath);
        //fbShareButton.setShareContent(sharePhotoContent);

        fbShareButton.setOnClickListener(v->{
            postFBwithGraphAPI();

        });



    }

    private void postFleet(){
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, "This is a Test.");
        tweetIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent,  PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for(ResolveInfo resolveInfo: resolvedInfoList){
            if(resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")){
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name );
                resolved = true;
                break;
            }
        }
        if(resolved){
            startActivity(tweetIntent);
        }else{
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
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

    private void instaStory(String imagePath){
        File imgFile = new File(imagePath);
        Uri imgUri = Uri.fromFile(imgFile);
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
        if (intent != null) {
            Intent shareIntent = new Intent("com.instagram.share.ADD_TO_STORY");
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage("com.instagram.android");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
            shareIntent.setType("image/jpeg");
            startActivity(shareIntent);
        } else {
            // bring user to the market to download the app.
            // or let them choose an app
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "com.instagram.android"));
            startActivity(intent);
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