package com.example.androidsocials;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class IntentHandler {

    private final Activity activity;
    public IntentHandler(Activity activity) {
        this.activity = activity;
    }

    public SharePhotoContent postPictureToFacebook(String imagePath) {
        byte[] data = null;

        Bitmap bi = BitmapFactory.decodeFile(imagePath);
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

    public void instaStory(String imagePath){
        File imgFile = new File(imagePath);
        Uri imgUri = Uri.fromFile(imgFile);
        Intent intent = activity.getPackageManager().getLaunchIntentForPackage("com.instagram.android");
        if (intent != null) {
            Intent shareIntent = new Intent("com.instagram.share.ADD_TO_STORY");
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage("com.instagram.android");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
            shareIntent.setType("image/jpeg");
            activity.startActivity(shareIntent);
        } else {
            // bring user to the market to download the app.
            // or let them choose an app
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "com.instagram.android"));
            activity.startActivity(intent);
        }
    }
    public void instaPost() {

    }

    public void launchTwitter(String text){
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, text);
        tweetIntent.setType("text/plain");

        PackageManager packManager = activity.getPackageManager();
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
            activity.startActivity(tweetIntent);
        }else{
            Toast.makeText(activity, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }
}
