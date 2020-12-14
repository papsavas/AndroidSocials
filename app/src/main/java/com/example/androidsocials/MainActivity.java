package com.example.androidsocials;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton uploadBtn;
    FloatingActionButton cameraBtn;
    public static final int LAUNCH_CAMERA_CODE = 502;
    public static final int IMPORT_IMAGE_CODE = 501;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadBtn = (FloatingActionButton) findViewById(R.id.uploadBtn);
        cameraBtn = (FloatingActionButton) findViewById(R.id.cameraBtn);


        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //Method of Fragment
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, IMPORT_IMAGE_CODE);
                } else {
                    importImage(); //would call an abstract callback on Java 8+
                }
            }
        });


        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        launchCamera();
                    }
                    else{
                        String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, LAUNCH_CAMERA_CODE);
                    }
                }
            }
        });

    }

    private void importImage() {
        Intent importImageIntent = new Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        startActivityForResult(importImageIntent, IMPORT_IMAGE_CODE);
    }

    private void launchCamera() {
        //ContentValues values = new ContentValues();
        //values.put(MediaStore.Images.Media.TITLE,"new picture");
        //values.put(MediaStore.Images.Media.DESCRIPTION, "taken from Device Camera");
        //imageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, ima
        startActivityForResult(cameraIntent,LAUNCH_CAMERA_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LAUNCH_CAMERA_CODE:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    launchCamera();
                }
                else {
                    Toast.makeText(this,"Permission to Camera Denied", Toast.LENGTH_SHORT).show();
                }
            }

            case IMPORT_IMAGE_CODE:{
                if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    importImage();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case IMPORT_IMAGE_CODE:{
                if(resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver()
                            .query(selectedImage, filePathColumn, null,null,null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    Log.d("IMG_RESULT", "onActivityResult() returned: " + picturePath);
                    /*Move to next Activity*/
                }
            }

            case LAUNCH_CAMERA_CODE:{
                if (resultCode == RESULT_OK) {
                    // Get Extra from the intent
                    Bundle extras = data.getExtras();
                    // Get the returned image from extra
                    Bitmap bmp = (Bitmap) extras.get("data");
                    Log.d("TAKE_PHOTO_RESULT", "onActivityResult() returned: " + bmp);
                    /*Move to next Activity*/
                }
            }
        }

    }




}