package com.codepath.apps.beetwitterultimate.MainActivity;

import android.app.ProgressDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.beetwitterultimate.Other_useful_class.GlobalVariable;
import com.codepath.apps.beetwitterultimate.R;
import com.codepath.apps.beetwitterultimate.Twitter_Client.TwitterApplication;
import com.codepath.apps.beetwitterultimate.Twitter_Client.TwitterClient;
import com.codepath.apps.beetwitterultimate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    private TwitterClient client;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private User user;
    private ImageView background,profile_image;
    private TextView tweets,following,follower,name,screen_name;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String id = getIntent().getStringExtra(GlobalVariable.CURRENT_USER_ID);

        registerWidgets();

        client = TwitterApplication.getInstance(ProfileActivity.this);
        client.getCurrentUserTimeLine(id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    user = User.fromJSON(response.getJSONObject(0).getJSONObject("user"));
                    if(user.getProfileImageURL()!=null)
                        Glide.with(ProfileActivity.this).load(user.getProfileImageURL()).placeholder(R.drawable.placeholder).into(profile_image);
                    if (user.getBackgroundURL()!=null)
                        Glide.with(ProfileActivity.this).load(user.getBackgroundURL()).placeholder(R.drawable.placeholder).into(background);

                    tweets.setText(user.getTweetCount()+"");
                    following.setText(user.getFollowingCount()+"");
                    follower.setText(user.getFollowerCount() + "");

                    name.setText(user.getName());
                    screen_name.setText(user.getScreenName());

                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });





    }

    private void registerWidgets() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading user");
        progressDialog.show();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar_profile);

        collapsingToolbarLayout.setTitle("");

        background = (ImageView) findViewById(R.id.imageView_profile_background);
        profile_image = (ImageView) findViewById(R.id.imageView_profile_image);



        tweets = (TextView)findViewById(R.id.textView_profile_tweets);

        following = (TextView)findViewById(R.id.textView_profile_following);

        follower = (TextView)findViewById(R.id.textView_follower);
        name = (TextView)findViewById(R.id.textView_profile_name);
        screen_name = (TextView)findViewById(R.id.textView_profile_screenName);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
