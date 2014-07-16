package com.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.adapter.ImageAdapter;
import com.rewyndr.reflectbig.interfaces.ViewPhoto;
import com.rewyndr.reflectbig.model.Photo;
import com.rewyndr.reflectbig.parse.impl.ViewPhotoParse;

import java.util.ArrayList;
import java.util.List;

public class PhotoMultiViewActivity extends Activity {
    private int start = 1;
    private int end = 15;

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
            List<String> photos= viewPhoto.getPhotos(1, 15);
            List<Photo> pics = new ArrayList<Photo>();
            for(String photo : photos){
                Log.i("urls",photo);
                Photo p = new Photo(photo);
                pics.add(p);
            }
            ImageAdapter imageAdapter = new ImageAdapter(this,pics);
            GridView gridViewImage = (GridView) findViewById(R.id.grid_view1);
            gridViewImage.setAdapter(imageAdapter);

            for (Photo photo: pics){
                photo.loadImage(imageAdapter);
            }

        } catch (Exception e) {
            Log.i("exception in parse", e.toString());
        }
    }
}
