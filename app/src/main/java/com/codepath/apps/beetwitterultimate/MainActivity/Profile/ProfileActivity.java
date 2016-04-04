package com.codepath.apps.beetwitterultimate.MainActivity.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.beetwitterultimate.Other_useful_class.GlobalVariable;
import com.codepath.apps.beetwitterultimate.R;
import com.codepath.apps.beetwitterultimate.Tab.ViewPagerAdapter;
import com.codepath.apps.beetwitterultimate.Twitter_Client.TwitterApplication;
import com.codepath.apps.beetwitterultimate.Twitter_Client.TwitterClient;
import com.codepath.apps.beetwitterultimate.models.Tweet;
import com.codepath.apps.beetwitterultimate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView banner, profile;
    private TextView userName, userScreenName, userTitle;
    private TextView tweetCount, followingCount, followerCount;
    private User user;

    private AppBarLayout appBarLayout;
    private TwitterClient client;

    //Tab
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        Intent i = getIntent();
        Bundle b = i.getBundleExtra("DATA");
        String a = b.getString(GlobalVariable.CURRENT_FROM_TWEET_TO_PROFILE);

        id = a;






        registerWidgets();
        handleEvents();





    }

    private void handleEvents() {
        client.getCurrentUserTimeLine(id, 1, -1, 1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    user = User.fromJSON(response.getJSONObject(0).getJSONObject("user"));
                    Glide.with(ProfileActivity.this).load(user.getBackgroundURL()).placeholder(R.drawable.placeholder).into(banner);
                    Glide.with(ProfileActivity.this).load(user.getProfileImageURL()).placeholder(R.drawable.placeholder).into(profile);

                    userName.setText(user.getName());
                    userScreenName.setText(user.getScreenName());

                    tweetCount.setText(user.getTweetCount() + "");
                    followingCount.setText(user.getFollowingCount() + "");
                    followerCount.setText(user.getFollowerCount() + "");

                    if (user.getDescription().length() > 0)
                        userTitle.setText(user.getDescription());
                    else
                        userTitle.setVisibility(View.GONE);
                    Log.v("WTF", response.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == -collapsingToolbarLayout.getHeight() + toolbar.getHeight()) {
                    collapsingToolbarLayout.setTitle(user.getName());
                } else {
                    collapsingToolbarLayout.setTitle("");
                }

            }
        });
    }

    private void registerWidgets() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar_profile);

        banner = (ImageView) findViewById(R.id.imageView_profile_userBanner);
        profile = (ImageView) findViewById(R.id.imageView_profile_userPicture);

        userName = (TextView) findViewById(R.id.textView_profile_userName);
        userScreenName = (TextView) findViewById(R.id.textView_profile_userScreenName);
        userTitle = (TextView) findViewById(R.id.textView_profile_userTitle);

        tweetCount = (TextView) findViewById(R.id.textView_profile_tweetCount);
        followingCount = (TextView) findViewById(R.id.textView_profile_userFollowing);
        followerCount = (TextView) findViewById(R.id.textView_profile_userFollower);

        client = TwitterApplication.getRestClient();

        appBarLayout = (AppBarLayout) findViewById(R.id.appBar_profile);

        //tab
        tabLayout = (TabLayout) findViewById(R.id.tabs_profile);
        viewPager = (ViewPager) findViewById(R.id.viewpager_profile);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(ProfileTweetFragment.getInstance(id), "TWEETS");
        viewPagerAdapter.addFragment(ProfilePhotosFragment.getInstance(id), "PHOTOS");
        viewPagerAdapter.addFragment(ProfileFavoritesFragment.getInstance(id), "FAVORITES");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
