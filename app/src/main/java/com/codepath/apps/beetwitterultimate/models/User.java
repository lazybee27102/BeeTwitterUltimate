package com.codepath.apps.beetwitterultimate.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by beeiscoding on 28/03/2016.
 */
public class User implements Parcelable {
    private String name;
    private long uid;
    private String screenName;
    private String profileImageURL;
    private String backgroundURL;

    public String getDescription() {
        return description;
    }

    private String description;

    public String getBackgroundURL() {
        return backgroundURL;
    }

    private long tweetCount,followingCount,followerCount;

    public long getTweetCount() {
        return tweetCount;
    }

    public long getFollowingCount() {
        return followingCount;
    }

    public long getFollowerCount() {
        return followerCount;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public static User fromJSON(JSONObject object) {
        User user = new User();

        try {
            user.uid = object.getLong("id");
            user.name=object.getString("name");
            user.screenName = object.getString("screen_name");
            user.profileImageURL = object.getString("profile_image_url_https");
            user.tweetCount = object.getLong("statuses_count");
            user.followingCount = object.getLong("friends_count");
            user.followerCount = object.getLong("followers_count");
            user.backgroundURL = object.getString("profile_banner_url");
            user.description = object.getString("description");

        } catch (JSONException e) {
            e.printStackTrace();
        }



        return user;
    }




    public User() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeLong(this.uid);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageURL);
        dest.writeString(this.backgroundURL);
        dest.writeLong(this.tweetCount);
        dest.writeLong(this.followingCount);
        dest.writeLong(this.followerCount);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.uid = in.readLong();
        this.screenName = in.readString();
        this.profileImageURL = in.readString();
        this.backgroundURL = in.readString();
        this.tweetCount = in.readLong();
        this.followingCount = in.readLong();
        this.followerCount = in.readLong();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
