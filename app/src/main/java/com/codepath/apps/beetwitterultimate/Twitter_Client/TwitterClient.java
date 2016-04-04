package com.codepath.apps.beetwitterultimate.Twitter_Client;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL

    public static final String UPLOAD_ENDPOINT = "https://upload.twitter.com/1.1";
    public static final String REST_CONSUMER_KEY = "0gsq8iiuzg773rgrjLYdHC0lw";       // Change this
    public static final String REST_CONSUMER_SECRET = "aCA6L0mnzdNQu4udtjQgGfpbt5BRaQj2jET4aOfI7QePIOUYRE"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://cpbeetwitter"; // Change this (here and in manifest)

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    // CHANGE THIS
    // DEFINE METHODS for different API endpoints here
    /*public void getInterestingnessList(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}*/
    public void getHomeTimeline(long sinceId, long maxId, long count, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", count);
        if (sinceId > 0)
            params.put("since_id", sinceId);
        if (maxId > 0)
            params.put("max_id", maxId);
        getClient().get(apiURL, params, handler);
    }

    public void getMentions(long sinceId, long maxId, long count, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", count);
        if (sinceId > 0)
            params.put("since_id", sinceId);
        if (maxId > 0)
            params.put("max_id", maxId);
        getClient().get(apiURL, params, handler);
    }


    public void getCurrentUser(AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("account/verify_credentials.json");
        RequestParams params = new RequestParams();
        params.put("include_entities", true);
        getClient().get(apiURL, params, handler);
    }

    public void getCurrentUserTimeLine(String id, long sinceId, long maxId, int count, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("user_id", id);
        params.put("count", count);
        if (sinceId > 0)
            params.put("since_id", sinceId);
        if (maxId > 0)
            params.put("max_id", maxId);
        getClient().get(apiURL, params, handler);
    }

    public void postNewTweet(String status, List<String> medias, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", status);

        if (medias!=null && medias.size() > 0)
        {
            StringBuilder result = new StringBuilder();
            for (String s : medias)
            {
                result.append(s+",");
            }
            result.substring(0,result.length()-2);
            params.put("media_ids", result.toString());
        }


        getClient().post(apiURL, params, handler);
    }

    public void postRetweet(String tweetId, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("statuses/retweet/" + tweetId + ".json");
        getClient().post(apiURL, handler);
    }

    public void postUnRetweet(String tweetId, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("statuses/unretweet/" + tweetId + ".json");
        getClient().post(apiURL, handler);
    }

    public void postFavorite(String tweetId, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("favorites/create.json");
        RequestParams params = new RequestParams();
        params.put("id", tweetId);
        params.put("include_entities", false);
        getClient().post(apiURL, params, handler);
    }

    public void postUnFavorite(String tweetId, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("favorites/destroy.json");
        RequestParams params = new RequestParams();
        params.put("id", tweetId);
        params.put("include_entities", false);
        getClient().post(apiURL, params, handler);
    }

    public void postComment(String tweetId, String status, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", status);
        params.put("in_reply_to_status_id", tweetId);
        getClient().post(apiURL, params, handler);
    }

    public void postMediaUpload(String filePath, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        File file = new File(filePath);

        Bitmap bitmapObject = BitmapFactory.decodeFile(file.getAbsolutePath());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmapObject.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        String s = Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
        params.put("media_data", s);

        getClient().post("https://upload.twitter.com/1.1/media/upload.json", params, handler);
    }

    public void getUserFavoritesList(String id, long sinceId, long maxId, int count, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/list.json");
        RequestParams params = new RequestParams();
        if (sinceId > 0) {
            params.put("since_id", String.valueOf(sinceId));
        }
        if (maxId > 0) {
            params.put("max_id", String.valueOf(maxId));
        }
        params.put("count", count);
        params.put("user_id", id);
        client.get(apiUrl, params, handler);
    }





	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}