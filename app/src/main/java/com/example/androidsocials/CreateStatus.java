package com.example.androidsocials;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateStatus extends AppCompatActivity {
    TextInputEditText caption;
    Button fbButton;
    Button twitterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_status);
        caption = (TextInputEditText) findViewById(R.id.captionTxt);
        fbButton = findViewById(R.id.fb_ShareBtn);
        twitterButton = findViewById(R.id.twitterBtn);
        IntentHandler intentHandler = new IntentHandler(CreateStatus.this);

        //SharePhotoContent sharePhotoContent = intentHandler.postStatusToFacebook(Objects.requireNonNull(caption.getText()).toString());
        //fbButton.setShareContent(sharePhotoContent);

        fbButton.setOnClickListener(v -> {
            //copyText("fbStatus", caption);
            try {
                postFBStatus(caption.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        twitterButton.setOnClickListener(v -> {
            /*copyText("twitterStatus", caption);*/
            if(caption.getText() == null)
                Toast.makeText(CreateStatus.this, "You need to add a status", Toast.LENGTH_LONG);
            else
                intentHandler.twitterIntent(caption.getText().toString());
        });
    }


    private void copyText(String label, TextInputEditText caption){
        if(caption.getText() == null)
            Toast.makeText(CreateStatus.this, "You need to add a status", Toast.LENGTH_LONG);
        else{
            copyToClipBoard(label, caption.getText().toString());
        }
    }

    private void copyToClipBoard(String label, String str) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, str);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(CreateStatus.this, "Status saved to clip board", Toast.LENGTH_SHORT).show();
    }

    public void postFBStatus(String status) throws JSONException {
        AccessToken accessToken = new AccessToken(
                getString(R.string.fb_page_access_token),
                getString(R.string.facebook_app_id),
                getString(R.string.fb_page_id),
                null,null,null,null,null,null,null
        );
        GraphRequest request = GraphRequest.newPostRequest(
                accessToken,
                "/105083264804153/feed",
                new JSONObject("{\"message\":\"" +status+ "  \"}"),
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Toast.makeText(CreateStatus.this, "Status Posting Completed!", Toast.LENGTH_LONG).show();
                    }
                });
        request.executeAsync();
    }
}