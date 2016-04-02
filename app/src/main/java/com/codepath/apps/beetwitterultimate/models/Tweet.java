package com.codepath.apps.beetwitterultimate.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by beeiscoding on 28/03/2016.
 */
public class Tweet implements Parcelable {

    ArrayList<String> tags = new ArrayList<>();
    private String body;
    private long uid;//inique id for the tweet
    private String createAt;
    private User user;
    private String photo;
    private String video;
    boolean favorited,retweeted;
    long favorited_count;
    long retweeted_count;

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public long getFavorited_count() {
        return favorited_count;
    }

    public long getRetweeted_count() {
        return retweeted_count;
    }


    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public static Tweet fromJSON(JSONObject object) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = object.getString("text");
            tweet.uid = object.getLong("id");
            tweet.createAt = object.getString("created_at");
            tweet.user = User.fromJSON(object.getJSONObject("user"));

            JSONArray media = object.getJSONObject("extended_entities").getJSONArray("media");

            JSONObject objectMedia = media.getJSONObject(0);
            if (objectMedia.getString("type").equals("photo"))
            {
                tweet.photo = objectMedia.getString("media_url_https");

            }else {
                JSONArray video = objectMedia.getJSONObject("video_info").getJSONArray("variants");

                for(int y = 0;y<video.length();y++)
                {
                    JSONObject objectVideo = video.getJSONObject(y);
                    if(objectVideo.getString("content_type").equals("video/mp4"))
                    {
                        tweet.video = objectVideo.getString("url");
                        break;
                    }
                }
            }
            tweet.favorited = object.getBoolean("favorited");
            tweet.retweeted = object.getBoolean("retweeted");
            tweet.retweeted_count = object.getLong("retweet_count");
            tweet.favorited_count = object.getLong("favorite_count");

            JSONObject entities = object.getJSONObject("entities");
            if(!entities.isNull("user_mentions"))
            {
                JSONArray arrayEntities = entities.getJSONArray("user_mentions");
                for (int z = 0;z<arrayEntities.length();z++)
                {
                    JSONObject object_screenName = arrayEntities.getJSONObject(z);
                    tweet.tags.add("@"+object_screenName.getString("screen_name")+" ");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray array) {

        ArrayList<Tweet> arr = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                Log.d("JSONVALUE", object.toString());
                Tweet tweet = Tweet.fromJSON(object);
                if (tweet != null)
                    arr.add(tweet);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

        }

        return arr;
    }

    public String getVideo() {
        return video;
    }

    public String getPhoto() {
        return photo;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreateAt() {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(createAt).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public User getUser() {
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
        dest.writeLong(this.uid);
        dest.writeString(this.createAt);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.photo);
        dest.writeString(this.video);
        dest.writeByte(favorited ? (byte) 1 : (byte) 0);
        dest.writeByte(retweeted ? (byte) 1 : (byte) 0);
        dest.writeLong(this.favorited_count);
        dest.writeLong(this.retweeted_count);
    }

    public Tweet() {
    }

    protected Tweet(Parcel in) {
        this.body = in.readString();
        this.uid = in.readLong();
        this.createAt = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.photo = in.readString();
        this.video = in.readString();
        this.favorited = in.readByte() != 0;
        this.retweeted = in.readByte() != 0;
        this.favorited_count = in.readLong();
        this.retweeted_count = in.readLong();
    }

    public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
