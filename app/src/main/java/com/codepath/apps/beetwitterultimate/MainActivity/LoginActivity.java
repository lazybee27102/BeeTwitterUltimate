package com.codepath.apps.beetwitterultimate.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.codepath.apps.beetwitterultimate.Other_useful_class.GlobalVariable;
import com.codepath.apps.beetwitterultimate.R;
import com.codepath.apps.beetwitterultimate.Twitter_Client.TwitterApplication;
import com.codepath.apps.beetwitterultimate.Twitter_Client.TwitterClient;
import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {
	private TwitterClient client;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		client = TwitterApplication.getInstance(this);
	}


	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		client.getCurrentUser(new JsonHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					String id = response.getString("id");
					Intent i = new Intent(LoginActivity.this, HomeTimelineActivity.class);
					i.putExtra(GlobalVariable.CURRENT_USER_ID,id);
					startActivity(i);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});


	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		getClient().connect();
	}

}
