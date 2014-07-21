package com.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.adapter.ImageAdapter;
import com.rewyndr.reflectbig.interfaces.ViewPhoto;
import com.rewyndr.reflectbig.model.Photo;
import com.rewyndr.reflectbig.parse.impl.ViewPhotoParse;
import com.rewyndr.reflectbig.service.PhotoViewService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dileeshvar on 7/16/2014.
 */
public class PhotoMultiViewActivity extends Activity {
    private final int BUFFER = 100;
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
        Display display = getWindowManager().getDefaultDisplay();
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
            gridViewImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    // Sending image id to FullScreenActivity
                    Intent i = new Intent(getApplicationContext(), SinglePhotoViewActivity.class);
                    // passing array index
                    Log.i("FromMultiActivity", "" + position);
                    i.putExtra("id", position);
                    startActivity(i);
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
                size = PhotoViewService.getImageCount(this);
            List<String> urls = null;
            if (start < size) {
                urls = PhotoViewService.getImageUrls(this, start + 1, start + BUFFER > size ? size : start + BUFFER);
                start += BUFFER;
                for (String url : urls) {
                    Photo p = new Photo(url);
                    photos.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
