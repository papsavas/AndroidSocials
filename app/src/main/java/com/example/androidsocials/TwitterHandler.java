package com.example.androidsocials;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.Date;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterHandler extends AsyncTask<String, Void, Twitter> {
    private String api_key;
    private String api_secret;
    private String access_token;
    private String access_token_secret;

    public TwitterHandler(String api_key, String api_secret, String access_token, String access_token_secret){
        this.api_key = api_key;
        this.api_secret = api_secret;
        this.access_token = access_token;
        this.access_token_secret = access_token_secret;

    };

    @Override
    protected Twitter doInBackground(String... strings) {
        Log.d("TWITTER_USER", api_key +'\n'+ api_secret +'\n'+ access_token +'\n'+ access_token_secret);
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
        .setOAuthConsumerKey(api_key)
        .setOAuthConsumerSecret(api_secret)
        .setOAuthAccessToken(access_token)
        .setOAuthAccessTokenSecret(access_token_secret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        try {
            File file = new File(PostImage.imagePath);
            StatusUpdate status = new StatusUpdate(PostImage.captionText);
            status.setMedia(file); // set the image to be uploaded here.
            twitter.updateStatus(status);
        } catch (TwitterException e) {

            e.printStackTrace();
        }
        return twitter;

    }

    @Override
    protected void onPostExecute(Twitter twitter) {
        Log.d("TWITTER_STATUS", String.valueOf(twitter));
    }
}
