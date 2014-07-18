package com.rewyndr.reflectbig.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rewyndr.reflectbig.Service.SinglePhotoService;

import java.util.List;

/**
 * This class is used by the Imageview_Activity for rendering the image in the image_view.
 *
 * Created by Raja on 7/16/2014.
 */
public class ImagePagerAdapter extends PagerAdapter {
    private Context context = null;
    private List<String> urls = null;
    /**
     * This constructor sets the context and urls obtained from API.
     *
     * @param context
     * @param urls
     */
    public ImagePagerAdapter(Context context, List<String> urls) {
        this.context = context;
        this.urls = urls;
    }

    /**
     * This method sets the number of swipes possible from the urls obtained.
     * @return
     */
    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    /**
     * Instantiate method overriden from the super class.
     * Calls imageLoad() to load the image from the URL
     * On reaching end, new sets of URL are fetched using the parse API
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        SinglePhotoService.imageLoad(imageView, urls.get(position), false);
        if(position + 1 == urls.size()) {
            int currentUrlSize = urls.size();
            int totalImagesFromDB = SinglePhotoService.getImageCount(context);
            if(currentUrlSize < totalImagesFromDB){
                int nextStartLimit = currentUrlSize+1;
                int nextEndLimit = totalImagesFromDB;
                urls.addAll(SinglePhotoService.getImageUrls(context, nextStartLimit, nextEndLimit));
            }
            Log.i("URLS", ""+urls.size());
        }
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    /**
     * Destroy on leaving.
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}