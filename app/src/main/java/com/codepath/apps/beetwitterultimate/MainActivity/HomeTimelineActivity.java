package com.codepath.apps.beetwitterultimate.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.apps.beetwitterultimate.Other_useful_class.GlobalVariable;
import com.codepath.apps.beetwitterultimate.R;
import com.codepath.apps.beetwitterultimate.Tab.HomeTabFragment;
import com.codepath.apps.beetwitterultimate.Tab.MentionTabFragment;
import com.codepath.apps.beetwitterultimate.Tab.ViewPagerAdapter;


public class HomeTimelineActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton button;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private String id;

    private static final int HOME_TO_COMPOSE_ACTIVITY = 999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_timeline);

        getCurrentUserId();
        registerWidgets();
        handleEvent();

    }

    private void handleEvent() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent compose = new Intent(HomeTimelineActivity.this, ComposeTweetActivity.class);
                compose.putExtra(GlobalVariable.CURRENT_USER_ID, id);
                startActivityForResult(compose, HOME_TO_COMPOSE_ACTIVITY);
            }
        });


    }

    private void registerWidgets() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        button = (FloatingActionButton) findViewById(R.id.fab);

        //tab
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_main_activty);
        viewPager = (ViewPager) findViewById(R.id.viewpager_main_activity_content);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        HomeTabFragment fragment_home = HomeTabFragment.getInstance();
        MentionTabFragment fragment_tab = MentionTabFragment.getInstance(id);

        viewPagerAdapter.addFragment(fragment_home, "HOME");
        viewPagerAdapter.addFragment(fragment_tab, "MENTION");


        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_profile: {
/*                Intent i = getIntent();
                String id = i.getStringExtra(GlobalVariable.CURRENT_USER_ID);
                Intent profile = new Intent(HomeTimelineActivity.this, ProfileActivity.class);
                profile.putExtra(GlobalVariable.CURRENT_USER_ID, id);
                startActivity(profile);*/
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }


    public String getCurrentUserId() {
        Intent i = getIntent();
        String id = i.getStringExtra(GlobalVariable.CURRENT_USER_ID);
        return id;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case HOME_TO_COMPOSE_ACTIVITY:
            {
                if (resultCode==RESULT_OK)
                {
                    HomeTabFragment.getInstance().getAllNewestNews();
                }
            }break;
        }
    }
}
