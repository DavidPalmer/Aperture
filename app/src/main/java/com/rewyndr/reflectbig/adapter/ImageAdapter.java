package com.rewyndr.reflectbig.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.Service.ImageService;
import com.rewyndr.reflectbig.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dil on 7/12/14.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    private List<Photo> photos = new ArrayList<Photo>();

    // Constructor
    public ImageAdapter(Context c, List photos){
        mContext = c;
        this.photos = photos;
    }


    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Photo photo = photos.get(position);
        ImageView imageView;
        float px = 0;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            Resources r = Resources.getSystem();
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, r.getDisplayMetrics());
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams((int)px*2, (int) px*2));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setBackgroundColor(Color.BLACK);
            viewHolder.picture = imageView;
        } else {
            imageView = (ImageView) convertView;
            viewHolder = (ViewHolder)convertView.getTag();
        }
        if(photo.getImage()!=null){
            Bitmap img= photo.getImage();
            int nh = (int) ( img.getHeight() * (512.0 / img.getWidth()));
            Bitmap scaleImg = Bitmap.createScaledBitmap(img, 100, 100, true);
            imageView.setImageBitmap(scaleImg);
        }
        else{
            imageView.setImageResource(R.drawable.ic_launcher);
        }
//        imageView.setImageResource(photos.get(position));
        return imageView;
    }

    static class ViewHolder {

        ImageView picture;
    }
}

