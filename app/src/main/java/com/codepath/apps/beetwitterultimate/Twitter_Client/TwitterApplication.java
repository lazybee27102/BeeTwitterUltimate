package com.codepath.apps.beetwitterultimate.Twitter_Client;

import android.content.Context;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TwitterClient client = TwitterApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class TwitterApplication extends com.activeandroid.app.Application {
	private static Context context;
	private static TwitterClient client;
	@Override
	public void onCreate() {
		super.onCreate();
		TwitterApplication.context = this;
	}

	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, TwitterApplication.context);
	}

	public static TwitterClient getInstance(Context context)
	{
		if (client==null)
			client = (TwitterClient) TwitterClient.getInstance(TwitterClient.class,context);
		return client;
	}
}