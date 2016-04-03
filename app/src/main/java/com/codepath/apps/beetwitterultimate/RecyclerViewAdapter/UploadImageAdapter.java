package com.codepath.apps.beetwitterultimate.RecyclerViewAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.apps.beetwitterultimate.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 03/04/2016.
 */
public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.MyViewHolder> {
    private ArrayList<String> images;
    private Context context;

    public UploadImageAdapter(ArrayList<String> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_image,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final String imagePath = images.get(position);
        File file = new File(imagePath);
        if (file.exists())
        {
            Bitmap bitmapObject = BitmapFactory.decodeFile(file.getAbsolutePath());
            holder.imageUploaded.setImageBitmap(bitmapObject);
        }else
            Toast.makeText(context, "Can't import this file", Toast.LENGTH_SHORT).show();


        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                images.remove(imagePath);
                UploadImageAdapter.this.notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageUploaded,delete_button;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageUploaded = (ImageView) itemView.findViewById(R.id.imageView_upload_image);
            delete_button = (ImageView) itemView.findViewById(R.id.imageView_upload_delete);

        }
    }
}
