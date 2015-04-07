package io.rewyndr.reflectbig.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import io.rewyndr.reflectbig.model.Photo;
import io.rewyndr.reflectbig.service.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Dileeshvar on 7/12/14.
 */
public class ImageAdapter extends BaseAdapter {
    public ImageLoader imageLoader;
    private Context context;
    private ArrayList<Photo> photos;

    public ImageAdapter(Context context, ArrayList<Photo> photos) {
        this.context = context;
        this.photos = photos;
        imageLoader = new ImageLoader(context);
    }

    public int getCount() {
        return photos.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            Resources r = Resources.getSystem();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, r.getDisplayMetrics());
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams((int) px * 2, (int) px * 2));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }
        imageLoader.DisplayImage(photos.get(position).getImageURL(), imageView);
        return imageView;
    }
}

