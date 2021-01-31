package com.example.androidsocials;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton uploadBtn;
    FloatingActionButton cameraBtn;
    FloatingActionButton postButton;
    FloatingActionButton searchButton;
    public static final int LAUNCH_CAMERA_CODE = 502;
    public static final int IMPORT_IMAGE_CODE = 501;
    public static final String IMAGE_NAME = "imageBitmap";
    public static final String MOVE_IMAGE = "moveitpls";
    private LoginButton fbLogInBtn;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();
        fbLogInBtn = findViewById(R.id.fb_login_button);
        //fbLogInBtn.setPermissions(Arrays.asList("Instagram Public Content Access", "instagram_basic","instagram_manage_comments"));

        postButton = (FloatingActionButton) findViewById(R.id.postBtn);
        searchButton = (FloatingActionButton) findViewById(R.id.searchButton);
        uploadBtn = (FloatingActionButton) findViewById(R.id.uploadBtn);
        cameraBtn = (FloatingActionButton) findViewById(R.id.cameraBtn);

        fbLogInBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FB_LOGIN_SUCCESS", "login Result is: "+loginResult);
            }

            @Override
            public void onCancel() {
                Log.d("FB_LOGIN_CANCELLED", "login Result is: CANCELLED");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FB_LOGIN_ERROR", error.toString());
            }
        });

        postButton.setOnClickListener(v -> {
            Log.d("POST", "CREATE POST");
            Intent i = new Intent(MainActivity.this, CreateStatus.class);
            startActivity(i);
        });

        searchButton.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Payed feature, unlock with high grade", Toast.LENGTH_LONG);
        });

        uploadBtn.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                importImage();

            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, IMPORT_IMAGE_CODE);
            }
        });


        cameraBtn.setOnClickListener(v -> {
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                launchCamera();
            }
            else{
                String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, LAUNCH_CAMERA_CODE);
            }
        });

    }

    private void importImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, IMPORT_IMAGE_CODE);
    }

    private void launchCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,LAUNCH_CAMERA_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LAUNCH_CAMERA_CODE:{
                if(grantResults.length>0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED){
                    launchCamera();
                }
                else {
                    Log.e("LAUNCH_CAMERA", "onRequestPermissionsResult: !!!!!!!!!!!!!!!!NOT HAVING PERMISSIONS!!!!!!!!!!!!!!!!");
                    Toast.makeText(this,"Permission to Camera Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case IMPORT_IMAGE_CODE:{
                if (grantResults.length>0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    importImage();
                }
                else {
                    Log.e("IMPORT IMAGE", "onRequestPermissionsResult: !!!!!!!!!!!!!!!!NOT HAVING PERMISSIONS!!!!!!!!!!!!!!!!");
                    Toast.makeText(this,"Permission to Gallery Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap;
        switch (requestCode){
            case IMPORT_IMAGE_CODE: {
                if(resultCode == RESULT_OK) {
                    assert data != null;
                    Uri imageUri = data.getData();
                    Intent i = new Intent(MainActivity.this, PostImage.class);
                    final String imageName = IMAGE_NAME;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        File file = new File(Environment.getExternalStorageDirectory() +"/" +imageName + ".png");
                        FileOutputStream fOut = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                        fOut.flush();
                        fOut.close();
                        i.putExtra(MOVE_IMAGE,imageName);
                        startActivity(i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                break;
                }
            }

            case LAUNCH_CAMERA_CODE: {
                if(resultCode == RESULT_OK){
                    assert data != null;
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    Intent i = new Intent(MainActivity.this, PostImage.class);
                    final String imageName = IMAGE_NAME;
                    try {
                        File file = new File(Environment.getExternalStorageDirectory() +"/" +imageName + ".png");
                        FileOutputStream fOut = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                        fOut.flush();
                        fOut.close();
                        i.putExtra(MOVE_IMAGE,imageName);
                        startActivity(i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            default:{
                bitmap=null;

            }
        }

    }


    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null){
                LoginManager.getInstance().logOut();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}