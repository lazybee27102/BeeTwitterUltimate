package com.codepath.apps.beetwitterultimate.MainActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.beetwitterultimate.Other_useful_class.GlobalVariable;
import com.codepath.apps.beetwitterultimate.R;
import com.codepath.apps.beetwitterultimate.RecyclerViewAdapter.UploadImageAdapter;
import com.codepath.apps.beetwitterultimate.Twitter_Client.TwitterApplication;
import com.codepath.apps.beetwitterultimate.Twitter_Client.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComposeTweetActivity extends AppCompatActivity {
    private static final int COMPOSEACITVITY_INSERTPICTURE = 998;
    private Toolbar toolbar;
    private ImageView imageView_profilePicture;
    private EditText editText_status;
    private Menu menu;
    private MenuItem post, countCharacter;
    private TwitterClient client;
    private ImageView imageView_insert_picture, imageView_take_picture, imageView_add_link;
    private RecyclerView recyclerView;
    private UploadImageAdapter adapter;
    private ArrayList<String> arrayImageUploaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);
        client = TwitterApplication.getInstance(this);
        registerWidgets();
        handleEvent();

    }

    private void handleEvent() {
        String id = getIntent().getStringExtra(GlobalVariable.CURRENT_USER_ID);
        client.getCurrentUserTimeLine(id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String userProfile = response.getJSONObject(0).getJSONObject("user").getString("profile_image_url_https");
                    Glide.with(ComposeTweetActivity.this).load(userProfile).placeholder(R.drawable.placeholder).into(imageView_profilePicture);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });


        editText_status.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               /* if (editText_status.getText().toString().trim().length() > 0)
                    post.setEnabled(true);
                else
                    post.setEnabled(false);*/

               /* if (Integer.parseInt(countCharacter.getTitle().toString()) >= 0 && !post.isEnabled()) {
                    post.setEnabled(true);
                } else if (Integer.parseInt(countCharacter.getTitle().toString()) < 0 && post.isEnabled()) {
                    post.setEnabled(false);
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = 140 - s.length();
                countCharacter.setTitle(count + "");
                if (count >= 0 && count < 140 && !post.isEnabled()) {
                    post.setEnabled(true);
                } else if ((count < 0 || count >= 140) && post.isEnabled()) {
                    post.setEnabled(false);
                }


            }
        });

        imageView_insert_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayImageUploaded.size() < 4) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, COMPOSEACITVITY_INSERTPICTURE);
                } else {
                    Toast.makeText(ComposeTweetActivity.this, "We just support 4 pictures", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void registerWidgets() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_compose);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        editText_status = (EditText) findViewById(R.id.editText_compose_status);
        editText_status.setFocusable(true);

        imageView_profilePicture = (ImageView) findViewById(R.id.imageView_compose_profile_image);
        imageView_insert_picture = (ImageView) findViewById(R.id.imageView_compose_insertImage);
        imageView_take_picture = (ImageView) findViewById(R.id.imageView_compose_takePicture);
        imageView_add_link = (ImageView) findViewById(R.id.imageView_compose_addLink);

        //recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_compose_images);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayImageUploaded = new ArrayList<>();
        adapter = new UploadImageAdapter(arrayImageUploaded, this);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.compose_tweet_menu, menu);
        this.menu = menu;
        post = menu.findItem(R.id.menu_action_compose);
        post.setEnabled(false);

        countCharacter = menu.findItem(R.id.menu_action_count_character);
        countCharacter.setEnabled(false);
        countCharacter.setTitle("140");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        } else if (item.getItemId() == R.id.menu_action_compose) {
            /*client.postNewTweet(editText_status.getText().toString().trim(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });*/

            if (arrayImageUploaded.size() > 0) {
                final List<String> medias = new ArrayList<>();
                for (int i = 0; i < arrayImageUploaded.size(); i++) {
                    client.postMediaUpload(arrayImageUploaded.get(i), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                medias.add(response.getString("media_id_string"));

                                if(medias.size()==arrayImageUploaded.size())
                                {
                                    client.postNewTweet(editText_status.getText().toString().trim(), medias, new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            super.onSuccess(statusCode, headers, response);
                                            setResult(RESULT_OK);
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                            super.onFailure(statusCode, headers, throwable, errorResponse);
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                    });
                }


                /*client.postNewTweet(editText_status.getText().toString().trim(), medias, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });*/


            }


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (editText_status.getText().toString().trim().length() != 0) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Discard change?");
            builder.setMessage("If you go back now,your draft will be discarded?");
            builder.setPositiveButton("DISCARD", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            builder.setNegativeButton("KEEP", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create();
            builder.show();
        } else {
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COMPOSEACITVITY_INSERTPICTURE && resultCode == RESULT_OK
                && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            if (cursor == null || cursor.getCount() < 1) {
                return; // no cursor or no record. DO YOUR ERROR HANDLING
            }

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            if (columnIndex < 0) // no column index
                return; // DO YOUR ERROR HANDLING

            String picturePath = cursor.getString(columnIndex);

            cursor.close(); // close cursor

            arrayImageUploaded.add(picturePath);


            adapter.notifyItemInserted(arrayImageUploaded.size());
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
    }
}
