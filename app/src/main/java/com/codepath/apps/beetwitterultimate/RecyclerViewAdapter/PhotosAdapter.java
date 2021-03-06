package com.codepath.apps.beetwitterultimate.RecyclerViewAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
 * Created by Administrator on 04/04/2016.
 */
public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>{
    Context context;
    private ArrayList<Tweet> tweets;

    public PhotosAdapter(Context context, ArrayList<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_profile_image, parent, false);
        PhotoViewHolder viewHolder = new PhotoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {

        final PhotoViewHolder tweetViewHolder = (PhotoViewHolder) holder;



        final Tweet tweet = tweets.get(position);
        if (tweet.getVideo() != null && tweet.getVideo().length()>0) {
            holder.videoView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, GlobalVariable.getScreenHeight((Activity) context) * 4 / 10);
            params.gravity = Gravity.CENTER_HORIZONTAL;
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
                holder.main_image.setVisibility(View.VISIBLE);
                Glide.with(context).load(tweet.getPhoto().get(0)).into(holder.main_image);
                holder.videoView.setVisibility(View.GONE);

            }else {
                holder.videoView.setVisibility(View.GONE);
            }
        }








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




    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView main_image;
        private VideoView videoView;
        private ImageView comment, share, retweet, favorited;
        private TextView retweet_count, favorite_count;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            videoView = (VideoView) itemView.findViewById(R.id.videoView_profile_photo_video);
            retweet_count = (TextView) itemView.findViewById(R.id.textView_profile_photo_retweet_count);
            favorite_count = (TextView) itemView.findViewById(R.id.textView_profile_photo_favorited_count);
            main_image = (ImageView) itemView.findViewById(R.id.imageView_profile_photo_mainPhoto);
            comment = (ImageView) itemView.findViewById(R.id.imageView_profile_photo_comment);
            share = (ImageView) itemView.findViewById(R.id.imageView_profile_photo_share);
            retweet = (ImageView) itemView.findViewById(R.id.imageView_profile_photo_retweet);
            favorited = (ImageView) itemView.findViewById(R.id.imageView_profile_photo_favorited);
        }


    }
}
