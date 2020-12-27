package com.example.androidsocials;

import android.os.AsyncTask;

import java.io.File;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.UploadedMedia;
import twitter4j.User;
import twitter4j.auth.Authorization;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterHandler extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... strings) {
        try{
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(String.valueOf(R.string.twitter_consumer_key))
                    .setOAuthConsumerSecret(String.valueOf(R.string.twitter_consumer_secret))
                    .setOAuthAccessToken(String.valueOf(R.string.twitter_access_token))
                    .setOAuthAccessTokenSecret(String.valueOf(R.string.twitter_access_token_secret));

            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();
            //twitter.setOAuthConsumer(String.valueOf(R.string.twitter_consumer_key), String.valueOf(R.string.twitter_consumer_secret));
            User user = twitter.verifyCredentials();
            String statusMessage = "testing #blessed";
            File file = new File(strings[0]);
            StatusUpdate status = new StatusUpdate(statusMessage);
            status.setMedia(file); // set the image to be uploaded here.
            twitter.updateStatus(status);
        }
        catch (TwitterException e){
            e.printStackTrace();
        }
        return null;
    }

}
