package com.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.GridView;

import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.adapter.ImageAdapter;
import com.rewyndr.reflectbig.common.PhotoType;
import com.rewyndr.reflectbig.interfaces.ViewPhoto;
import com.rewyndr.reflectbig.model.Photo;
import com.rewyndr.reflectbig.parse.impl.ViewPhotoParse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dileeshvar on 7/16/2014.
 */
public class PhotoMultiViewActivity extends Activity {
    ImageAdapter imageAdapter;
    ArrayList<Photo> photos = new ArrayList<Photo>();
    GridView gridViewImage;
    private int start = 0;
    private int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_multi_view);
        displayPhotos();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo_multi_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayPhotos(){
        ViewPhoto viewPhoto = ViewPhotoParse.getInstance(this);
        try {
            addPhotoList();
            imageAdapter = new ImageAdapter(this, photos);
            gridViewImage = (GridView) findViewById(R.id.grid_view1);
            gridViewImage.setAdapter(imageAdapter);
            gridViewImage.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (visibleItemCount + firstVisibleItem == totalItemCount && size > totalItemCount) {
                        addPhotoList();
                        imageAdapter.notifyDataSetChanged();
                    }
                }
            });
        } catch (Exception e) {
            Log.i("exception in parse", e.toString());
        }
    }

    public void addPhotoList() {
        ViewPhoto viewPhoto = ViewPhotoParse.getInstance(this);

        try {
            if (size <= 0)
                size = viewPhoto.getCount();
            List<String> urls = null;
            if (start < size) {
                urls = viewPhoto.getPhotos(start + 1, start + 15 > size ? size : start + 15, PhotoType.THUMBNAIL);
                start += 15;
                for (String url : urls) {
                    Log.i("test", url);
                    Photo p = new Photo(url);
                    photos.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
