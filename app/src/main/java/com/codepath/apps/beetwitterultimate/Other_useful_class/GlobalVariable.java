package com.codepath.apps.beetwitterultimate.Other_useful_class;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by beeiscoding on 28/03/2016.
 */
public class GlobalVariable {
    public static int getScreenWidth(Activity activity)
    {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity)
    {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    public static float getDensity(Activity activity)
    {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.density;
    }

    public static final String CURRENT_USER_ID = "currentUserId";
    public static final String RETWEET_TWEET_TRANSFER = "tweetTransfer";
    public static final String COMMENT_TWEET_TRANSFER = "commentTweetTransfer";

    public static int convertPXtoDP(Activity context,int px)
    {
        return Math.round(px * getDensity(context));
    }

    public static int convertDPtoPX(Activity context,int px)
    {
        return Math.round(px / getDensity(context));
    }

}

