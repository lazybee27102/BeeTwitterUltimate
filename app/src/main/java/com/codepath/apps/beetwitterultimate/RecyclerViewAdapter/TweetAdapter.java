package com.codepath.apps.beetwitterultimate.RecyclerViewAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.codepath.apps.beetwitterultimate.Comment_Retweet_Like_Share_Activty.CommentFragment;
import com.codepath.apps.beetwitterultimate.Comment_Retweet_Like_Share_Activty.RetweetFragment;
import com.codepath.apps.beetwitterultimate.MainActivity.Profile.ProfileActivity;
import com.codepath.apps.beetwitterultimate.Other_useful_class.GlobalVariable;
import com.codepath.apps.beetwitterultimate.R;
import com.codepath.apps.beetwitterultimate.Twitter_Client.TwitterApplication;
import com.codepath.apps.beetwitterultimate.Twitter_Client.TwitterClient;
import com.codepath.apps.beetwitterultimate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by beeiscoding on 28/03/2016.
 */
public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.TweetViewHolder> {
    Context context;
    private ArrayList<Tweet> tweets;

    public TweetAdapter(Context context, ArrayList<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        TweetViewHolder viewHolder = new TweetViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TweetViewHolder holder, int position) {

        final TweetViewHolder tweetViewHolder = (TweetViewHolder) holder;

        Typeface typeface_medium = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Light.ttf");
        holder.status.setTypeface(typeface_medium);

        Typeface typeface_bold = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");
        holder.name.setTypeface(typeface_bold);

        final Tweet tweet = tweets.get(position);
        if (tweet.getVideo() != null && tweet.getVideo().length()>0) {
            holder.videoView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, GlobalVariable.getScreenHeight((Activity) context) * 4 / 10);
            params.gravity = Gravity.CENTER_HORIZONTAL;

            holder.linearLayout_1photo.setVisibility(View.GONE);
            holder.linearLayout_2photo.setVisibility(View.GONE);
            holder.linearLayout_3photo.setVisibility(View.GONE);
            holder.linearLayout_4photo.setVisibility(View.GONE);

            holder.videoView.setLayoutParams(params);
            holder.videoView.setVideoPath(tweet.getVideo());
            MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(holder.videoView);
            holder.videoView.setMediaController(mediaController);
            holder.videoView.requestFocus();
            holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {

                }
            });
        } else {
            if (tweet.getPhoto().size()>0) {
                switch (tweet.getPhoto().size())
                {
                    default:
                    {
                        holder.linearLayout_1photo.setVisibility(View.VISIBLE);
                        holder.linearLayout_2photo.setVisibility(View.GONE);
                        holder.linearLayout_3photo.setVisibility(View.GONE);
                        holder.linearLayout_4photo.setVisibility(View.GONE);
                        final String s = tweet.getPhoto().get(0);
                        Glide.with(context).load(s).placeholder(R.drawable.placeholder).into(holder.main_image);
                        Log.d("WTF", position + " - " + tweet.getPhoto().get(0));
                        break;
                    }
                    case 2:
                    {
                        holder.linearLayout_1photo.setVisibility(View.GONE);
                        holder.linearLayout_2photo.setVisibility(View.VISIBLE);
                        holder.linearLayout_3photo.setVisibility(View.GONE);
                        holder.linearLayout_4photo.setVisibility(View.GONE);
                        final String s = tweet.getPhoto().get(0);
                        final String s1 = tweet.getPhoto().get(1);
                        Glide.with(context).load(s).placeholder(R.drawable.placeholder).into(holder.imageView_2photo_1);
                        Glide.with(context).load(s1).placeholder(R.drawable.placeholder).into(holder.imageView_2photo_2);
                        Log.d("WTF", position + " - " + tweet.getPhoto().get(0));
                        break;

                    }
                    case 3:
                    {
                        holder.linearLayout_1photo.setVisibility(View.GONE);
                        holder.linearLayout_2photo.setVisibility(View.GONE);
                        holder.linearLayout_3photo.setVisibility(View.VISIBLE);
                        holder.linearLayout_4photo.setVisibility(View.GONE);
                        final String s = tweet.getPhoto().get(0);
                        final String s1 = tweet.getPhoto().get(1);
                        final String s2 = tweet.getPhoto().get(2);
                        Glide.with(context).load(s).placeholder(R.drawable.placeholder).into(holder.imageView_3photo_1);
                        Glide.with(context).load(s1).placeholder(R.drawable.placeholder).into(holder.imageView_3photo_2);
                        Glide.with(context).load(s2).placeholder(R.drawable.placeholder).into(holder.imageView_3photo_3);
                        Log.d("WTF", position + " - " + tweet.getPhoto().get(0));
                        break;

                    }
                    case 4:
                    {
                        final String s = tweet.getPhoto().get(0);
                        final String s1 = tweet.getPhoto().get(1);
                        final String s2 = tweet.getPhoto().get(2);
                        final String s3 = tweet.getPhoto().get(3);

                        holder.linearLayout_1photo.setVisibility(View.GONE);
                        holder.linearLayout_2photo.setVisibility(View.GONE);
                        holder.linearLayout_3photo.setVisibility(View.GONE);
                        holder.linearLayout_4photo.setVisibility(View.VISIBLE);
                        Glide.with(context).load(s).placeholder(R.drawable.placeholder).into(holder.imageView_4photo_1);
                        Glide.with(context).load(s1).placeholder(R.drawable.placeholder).into(holder.imageView_4photo_2);
                        Glide.with(context).load(s2).placeholder(R.drawable.placeholder).into(holder.imageView_4photo_3);
                        Glide.with(context).load(s3).placeholder(R.drawable.placeholder).into(holder.imageView_4photo_4);
                        Log.d("WTF", position + " - " + tweet.getPhoto().get(0));
                        break;

                    }
                }
                holder.videoView.setVisibility(View.GONE);

            }else {
                holder.videoView.setVisibility(View.GONE);
                holder.linearLayout_1photo.setVisibility(View.GONE);
                holder.linearLayout_2photo.setVisibility(View.GONE);
                holder.linearLayout_3photo.setVisibility(View.GONE);
                holder.linearLayout_4photo.setVisibility(View.GONE);
            }
        }


        holder.name.setText(tweet.getUser().getName());
        holder.screen_name.setText("@"+tweet.getUser().getScreenName());
        holder.createAt.setText(tweet.getCreateAt());
        holder.status.setText(tweet.getBody());




        if (tweet.getUser().getProfileImageURL() != null)
            Glide.with(context).load(tweet.getUser().getProfileImageURL()).bitmapTransform(new CropCircleTransformation(context)).into(holder.profile_image);

        //comment,share,retweet,share
        holder.retweet_count.setText(tweet.getRetweeted_count() + "");
        holder.favorite_count.setText(tweet.getFavorited_count() + "");

        final Animation zoomin = AnimationUtils.loadAnimation(context, R.anim.zoomin);
        final Animation zoomout = AnimationUtils.loadAnimation(context, R.anim.zoomout);

       if (tweet.isFavorited() == true) {

            holder.favorited.setAnimation(zoomin);
            holder.favorited.startAnimation(zoomin);
        } else {
          /* holder.favorited.setAnimation(zoomout);
           holder.favorited.startAnimation(zoomin);*/
        }
        if (tweet.isRetweeted() == true) {
            holder.retweet.setAnimation(zoomin);
            holder.retweet.startAnimation(zoomin);
        } else {

        }

        //Event
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                CommentFragment fragment = CommentFragment.newInstance(tweet, new CommentFragment.getResponseFromComment() {
                    @Override
                    public void accessResponse(String s) {
                        TwitterClient client = TwitterApplication.getInstance(context);
                        client.postComment(String.valueOf(tweet.getUid()), s, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                holder.comment.setAnimation(zoomin);
                                holder.comment.startAnimation(zoomin);
                                holder.comment.setAnimation(zoomout);
                                holder.comment.startAnimation(zoomout);
                            }
                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                            }
                        });
                    }
                });
                fragment.show(fragmentManager,"COMMENT");
            }
        });
        holder.retweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                RetweetFragment retweetFragment = RetweetFragment.newInstance(tweet, new RetweetFragment.getResponseFromRetweet() {
                    @Override
                    public void accessResponse(boolean b) {
                        TwitterClient client = TwitterApplication.getInstance(context);
                        if (b) {
                            //isretweet ===> true
                            client.postRetweet(String.valueOf(tweet.getUid()), new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    Toast.makeText(context, "Retweet Successfully", Toast.LENGTH_SHORT).show();
                                    tweet.setRetweeted(true);
                                    holder.retweet.setAnimation(zoomin);
                                    holder.retweet.startAnimation(zoomin);
                                    long tweeter_count = Long.valueOf(holder.retweet_count.getText().toString().trim());
                                    holder.retweet_count.setText(String.valueOf(tweeter_count + 1));
                                }
                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                    Toast.makeText(context, "Retweet Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            //isretweet ===> false
                            client.postUnRetweet(String.valueOf(tweet.getUid()), new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    Toast.makeText(context, "UnRetweet Successfully", Toast.LENGTH_SHORT).show();
                                    tweet.setRetweeted(false);
                                    holder.retweet.setAnimation(zoomout);
                                    holder.retweet.startAnimation(zoomout);
                                    long tweeter_count = Long.valueOf(holder.retweet_count.getText().toString().trim());
                                    holder.retweet_count.setText(String.valueOf(tweeter_count - 1));
                                }
                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                    Toast.makeText(context, "UnRetweet Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                retweetFragment.show(fragmentManager, "RETWEET");
            }
        });
        holder.favorited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterClient client = TwitterApplication.getInstance(context);
                if (tweet.isFavorited() == true) {
                    holder.favorited.setAnimation(zoomout);
                    holder.favorited.startAnimation(zoomout);
                    client.postUnFavorite(String.valueOf(tweet.getUid()), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            tweet.setFavorited(false);
                            long favorite_count = Long.valueOf(holder.favorite_count.getText().toString());
                            holder.favorite_count.setText(String.valueOf(favorite_count - 1));

                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                } else {
                    holder.favorited.setAnimation(zoomin);
                    holder.favorited.startAnimation(zoomin);
                    client.postFavorite(String.valueOf(tweet.getUid()), new JsonHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            tweet.setFavorited(true);
                            long favorite_count = Long.valueOf(holder.favorite_count.getText().toString());
                            holder.favorite_count.setText(String.valueOf(favorite_count + 1));

                        }
                    });
                }
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, tweet.getBody());
                context.startActivity(Intent.createChooser(shareIntent, "Share via "));
                holder.share.setAnimation(zoomin);
                holder.share.startAnimation(zoomin);
                holder.share.setAnimation(zoomout);
                holder.share.startAnimation(zoomout);
            }
        });

        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(context, ProfileActivity.class);
                Bundle b = new Bundle();
                b.putString(GlobalVariable.CURRENT_FROM_TWEET_TO_PROFILE, String.valueOf(tweet.getUser().getUid()));
                profile.putExtra("DATA", b);
                context.startActivity(profile);
            }
        });


    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public static class TweetViewHolder extends RecyclerView.ViewHolder {
        private ImageView main_image;
        private VideoView videoView;
        private TextView name, createAt, status,screen_name;
        private ImageView comment, share, retweet, favorited;
        private TextView retweet_count, favorite_count;
        private ImageView profile_image;

        private LinearLayout linearLayout_1photo;
        private LinearLayout linearLayout_2photo;
        private LinearLayout linearLayout_3photo;
        private LinearLayout linearLayout_4photo;

        private ImageView imageView_2photo_1,imageView_2photo_2;
        private ImageView imageView_3photo_1,imageView_3photo_2,imageView_3photo_3;
        private ImageView imageView_4photo_1,imageView_4photo_2,imageView_4photo_3,imageView_4photo_4;

        public TweetViewHolder(View itemView) {
            super(itemView);
            profile_image = (ImageView) itemView.findViewById(R.id.imageView_tweet_profile_picture);
            videoView = (VideoView) itemView.findViewById(R.id.videoView);
            name = (TextView) itemView.findViewById(R.id.textView_tweet_name);
            createAt = (TextView) itemView.findViewById(R.id.textView_tweet_create_at);
            status = (TextView) itemView.findViewById(R.id.textView_tweet_status);
            retweet_count = (TextView) itemView.findViewById(R.id.textView_tweet_retweet_count);
            favorite_count = (TextView) itemView.findViewById(R.id.textView_tweet_favorited_count);
            main_image = (ImageView) itemView.findViewById(R.id.imageView_tweet_main_photo);
            comment = (ImageView) itemView.findViewById(R.id.imageView_tweet_comment);
            share = (ImageView) itemView.findViewById(R.id.imageView_tweet_share);
            retweet = (ImageView) itemView.findViewById(R.id.imageView_tweet_retweet);
            favorited = (ImageView) itemView.findViewById(R.id.imageView_tweet_favorited);
            screen_name = (TextView)itemView.findViewById(R.id.textView_tweet_sceen_name);

            linearLayout_1photo = (LinearLayout)itemView.findViewById(R.id.layout_tweet_1photo);
            linearLayout_2photo = (LinearLayout)itemView.findViewById(R.id.layout_tweet_2photo);
            linearLayout_3photo = (LinearLayout)itemView.findViewById(R.id.layout_tweet_3photo);
            linearLayout_4photo = (LinearLayout)itemView.findViewById(R.id.layout_tweet_4photo);

            imageView_2photo_1 = (ImageView)itemView.findViewById(R.id.imageView_tweet_2photo_1);
            imageView_2photo_2 = (ImageView)itemView.findViewById(R.id.imageView_tweet_2photo_2);

            imageView_3photo_1 = (ImageView)itemView.findViewById(R.id.imageView_tweet_3photo_1);
            imageView_3photo_2 = (ImageView)itemView.findViewById(R.id.imageView_tweet_3photo_2);
            imageView_3photo_3 = (ImageView)itemView.findViewById(R.id.imageView_tweet_3photo_3);

            imageView_4photo_1 = (ImageView)itemView.findViewById(R.id.imageView_tweet_4photo_1);
            imageView_4photo_2 = (ImageView)itemView.findViewById(R.id.imageView_tweet_4photo_2);
            imageView_4photo_3 = (ImageView)itemView.findViewById(R.id.imageView_tweet_4photo_3);
            imageView_4photo_4 = (ImageView)itemView.findViewById(R.id.imageView_tweet_4photo_4);

        }


    }
}