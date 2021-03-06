package com.codepath.apps.beetwitterultimate.Other_useful_class;

/**
 * Created by beeiscoding on 31/03/2016.
 */
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

public class RoundedCornersTransformation implements Transformation<Bitmap> {

    private BitmapPool mBitmapPool;

    private int radius;
    private int margin;

    public RoundedCornersTransformation(BitmapPool pool, int radius, int margin) {
        this.radius = radius;
        this.margin = margin;
        mBitmapPool = pool;
    }

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();

        int width = source.getWidth();
        int height = source.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect(new RectF(margin, margin, width - margin, height - margin),
                radius, radius, paint);
        source.recycle();

        return BitmapResource.obtain(bitmap, mBitmapPool);
    }

    @Override
    public String getId() {
        return "RoundedTransformation(radius=" + radius + ", margin=" + margin + ")";
    }
}
