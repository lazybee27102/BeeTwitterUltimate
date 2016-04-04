package com.codepath.apps.beetwitterultimate.Other_useful_class;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * Created by Administrator on 04/04/2016.
 */
public class ProcessImageFile extends AsyncTask<String,Void,Bitmap> {
    private Context context;
    private ProgressDialog progressDialog;
    private accessResponse delegate;

    public ProcessImageFile(Context context, accessResponse delegate) {
        this.context = context;
        this.delegate = delegate;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Processing image...");
        progressDialog.show();
    }

    public interface accessResponse
    {
        void ProcessBimap(Bitmap bitmap);
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        String inputPath = params[0];
        Bitmap bitmapObject = BitmapFactory.decodeFile(inputPath);

        return bitmapObject;
    }

    @Override
    protected void onPostExecute(Bitmap aVoid) {
        super.onPostExecute(aVoid);
        if(progressDialog.isShowing())
            progressDialog.dismiss();
        delegate.ProcessBimap(aVoid);

    }
}
