package com.rewyndr.reflectbig.Activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.rewyndr.reflectbig.Adapter.ImagePagerAdapter;
import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.Service.SinglePhotoService;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity linked with the single image view layout(activity_image_view.xml)
 *
 * Created by Raja on 7/16/2014.
 */
public class SinglePhotoView_Activity extends Activity {
    private static List<String> urls = new ArrayList<String>();
    private int currentId = 0;
    /**
     * This method is used for setting the image from the id of the image clicked in thumbnail activity.
     * @param id
     */
    public void setUrl(int id){
        currentId = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(currentId == 0) {
            currentId = 7;
        }
        int total = SinglePhotoService.getImageCount(this);
        int start = 1, end = total;
        urls = SinglePhotoService.getImageUrls(this, start, end);
        Log.i("URLSize", ""+total);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ViewPager viewPager = (ViewPager) findViewById(R.id.singleView_viewPager_currentImage);
        ImagePagerAdapter adapter = new ImagePagerAdapter(SinglePhotoView_Activity.this, urls);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentId-1);
        viewPager.setBackgroundColor(Color.BLACK);
    }
}