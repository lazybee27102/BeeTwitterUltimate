package com.codepath.apps.beetwitterultimate.Tab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.beetwitterultimate.Other_useful_class.EndlessRecyclerViewScrollListener;
import com.codepath.apps.beetwitterultimate.R;
import com.codepath.apps.beetwitterultimate.RecyclerViewAdapter.MentionAdapter;
import com.codepath.apps.beetwitterultimate.RecyclerViewAdapter.TweetAdapter;
import com.codepath.apps.beetwitterultimate.Twitter_Client.TwitterApplication;
import com.codepath.apps.beetwitterultimate.Twitter_Client.TwitterClient;
import com.codepath.apps.beetwitterultimate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MentionTabFragment extends Fragment {

    public MentionTabFragment() {
        // Required empty public constructor
    }

    private static MentionTabFragment mentionTabFragment;

    TwitterClient client;
    private ArrayList<Tweet> tweets;
    private MentionAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private long sinceId = -1;
    private long maxId = -1;


    public static MentionTabFragment getInstance()
    {
        if(mentionTabFragment==null)
        {
            mentionTabFragment = new MentionTabFragment();
        }
        return mentionTabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mention_tab, container, false);


        registerWidgets(v);
        handleEvents();


        return v;
    }

    private void handleEvents() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllNewestNews();
                swipeContainer.setRefreshing(false);
            }

        });

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getOldestNews();
            }
        });
    }

    private void registerWidgets(View v) {
        //GetView
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeLayout_mention_wrapper_tweets);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_mention_tweets);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        tweets = new ArrayList<>();
        adapter = new MentionAdapter(getContext(), tweets);
        recyclerView.setAdapter(adapter);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar_mention_progress);

        client = TwitterApplication.getInstance(getContext());
        populateTimeline();
    }

    public void populateTimeline() {
        client.getMentions(1, -1, 20, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                progressBar.setVisibility(View.GONE);
                swipeContainer.setVisibility(View.VISIBLE);

                tweets.clear();
                tweets.addAll(Tweet.fromJSONArray(response));
                int size = tweets.size();
                adapter.notifyItemRangeInserted(0, size);

                sinceId = tweets.get(0).getUid();
                maxId = tweets.get(size - 1).getUid();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("ERROR", errorResponse.toString());
                Toast.makeText(getContext(), "Can't get Mention", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getOldestNews() {

        client.getMentions(-1, maxId - 1, 50, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                int tweetMaxPosition = tweets.size();
                tweets.addAll(Tweet.fromJSONArray(response));
                adapter.notifyItemRangeInserted(tweetMaxPosition, 50);

                sinceId = tweets.get(0).getUid();
                maxId = tweets.get(tweets.size() - 1).getUid();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("ERROR", errorResponse.toString());
                Toast.makeText(getContext(), "Can't get new Mention", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAllNewestNews() {
        client.getMentions(sinceId, -1, 200, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                ArrayList<Tweet> responseTweet = Tweet.fromJSONArray(response);
                if (responseTweet.size() > 0) {
                    tweets.addAll(0, responseTweet);


                    adapter.notifyItemRangeInserted(0, responseTweet.size());
                    recyclerView.smoothScrollToPosition(0);
                    sinceId = tweets.get(0).getUid();
                    maxId = tweets.get(tweets.size() - 1).getUid();
                } else
                    Toast.makeText(getContext(), "No new Mention", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && tweets!=null)
            getAllNewestNews();
    }
}
