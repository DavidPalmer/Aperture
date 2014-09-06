package com.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;

import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.common.Constants;
import com.rewyndr.reflectbig.common.PhotoType;
import com.rewyndr.reflectbig.interfaces.PhotoService;
import com.rewyndr.reflectbig.service.ServiceFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SinglePhotoViewActivity extends Activity {
    private static final String CURRENT_POSITION = "current_position";
    int currentId = 0;
    private ImageSwitcher mImageSwitcher;
    private View mOverscrollLeft;
    private View mOverscrollRight;
    private GestureDetector mGestureDetector;
    private int mCurrentPosition = 0;
    private Animation mSlideInLeft;
    private Animation mSlideOutRight;
    private Animation mSlideInRight;
    private Animation mSlideOutLeft;
    private Animation mOverscrollLeftFadeOut;
    private Animation mOverscrollRightFadeOut;
    private int currentStart = 0;
    private int currentEnd = 0;
    private List<String> mImages = new ArrayList<String>();
    private String eventId = "";

    private static int getEndLimit(int currentId, int total) {
        int start = (currentId + Constants.FETCH_LENGTH);
        if (start < total) {
            return start;
        } else {
            return total;
        }
    }

    public void setCurrentId(){
        Intent intent = getIntent();
        currentId = intent.getExtras().getInt("id") + 1;
        eventId = intent.getExtras().getString("eventId");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCurrentPosition = getCurrentPositionFromPause();

        setCurrentId();
        PhotoService service = ServiceFactory.getPhotoServiceInstance(this);
        int total = 0;
        try {
            total = service.getCount(eventId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int start = getStartLimit(currentId), end = getEndLimit(currentId, total);
        try {
            mImages = service.getPhotos(eventId, start, end, PhotoType.SMALL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(mCurrentPosition == 0) {
            mCurrentPosition = currentId - start;
        }
        mImageSwitcher = (ImageSwitcher) findViewById(R.id.image);
        mOverscrollLeft = findViewById(R.id.overscroll_left);
        mOverscrollRight = findViewById(R.id.overscroll_right);

        // Animations
        mSlideInLeft = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        mSlideOutRight = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);
        mSlideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        mSlideOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        mOverscrollLeftFadeOut = AnimationUtils
                .loadAnimation(this, R.anim.fade_out);
        mOverscrollRightFadeOut = AnimationUtils.loadAnimation(this,
                R.anim.fade_out);

        // ImageSwitcher
        mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView view = new ImageView(SinglePhotoViewActivity.this);
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                view.setLayoutParams(new ImageSwitcher.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                return view;
            }
        });
        loadImage(mCurrentPosition);
        currentStart = Math.max(currentId - Constants.FETCH_LENGTH, Constants.IMAGE_START_ID);
        currentEnd = Math.min(currentId + Constants.FETCH_LENGTH, total);
        mGestureDetector = new GestureDetector(this, new SwipeListener());
        mImageSwitcher.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });
        mImageSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicked", "Came here");
                final View contentView = findViewById(R.id.image);
                contentView.setLayoutParams(new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
            }
        });
    }

    private int getCurrentPositionFromPause() {
        SharedPreferences preferences = getSharedPreferences(CURRENT_POSITION, MODE_PRIVATE);
        return preferences.getInt(CURRENT_POSITION, 0);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        SharedPreferences preferences = getSharedPreferences(CURRENT_POSITION, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if(isFinishing()) {
            mCurrentPosition = 0;
        }
        editor.putInt(CURRENT_POSITION, mCurrentPosition);
        editor.commit();
    }

    private void moveNextOrPrevious(int delta) throws Exception {
        int nextImagePos = mCurrentPosition + delta;
        if (nextImagePos < 0) {
            int currentEnd = currentStart + mImages.size();
            Log.d(this.getClass().getName(), "Current URLs start " + currentStart + "end " + currentEnd);

            int fetchStart = Math.max(currentStart - Constants.FETCH_LENGTH, 0);
            int fetchEnd = currentStart - 1;
            Log.d(this.getClass().getName(), "URls start " + fetchStart + "end " + fetchEnd);

            PhotoService viewPhoto = ServiceFactory.getPhotoServiceInstance(this);
            List<String> newUrls = new ArrayList<String>();
            try {
                newUrls = viewPhoto.getPhotos(eventId, fetchStart, fetchEnd, PhotoType.SMALL);
                Log.d(this.getClass().getName(), "URls size " + newUrls.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (newUrls.size() == 0) {
                mOverscrollLeft.setVisibility(View.VISIBLE);
                mOverscrollLeft.startAnimation(mOverscrollLeftFadeOut);
                return;
            }
            mImages.addAll(0, newUrls);
            currentStart = currentStart - newUrls.size();
            nextImagePos += newUrls.size();
            mCurrentPosition = nextImagePos;
        }
        if (nextImagePos >= mImages.size()) {
            int currentStart = currentEnd - mImages.size();
            Log.d(this.getClass().getName(), "Current URLs start " + currentStart + "end " + currentEnd);

            PhotoService viewPhoto = ServiceFactory.getPhotoServiceInstance(this);
            int total = viewPhoto.getCount(eventId);
            int fetchStart = currentEnd + 1;
            int fetchEnd = Math.min(currentEnd + Constants.FETCH_LENGTH, total);
            List<String> newUrls = viewPhoto.getPhotos(eventId, fetchStart, fetchEnd, PhotoType.SMALL);
            Log.d(this.getClass().getName(), "URls size " + newUrls.size());
            if (newUrls.size() == 0) {
                mOverscrollRight.setVisibility(View.VISIBLE);
                mOverscrollRight.startAnimation(mOverscrollRightFadeOut);
                return;
            }
            mImages.addAll(newUrls);
            currentEnd = currentEnd + newUrls.size();
            mCurrentPosition = nextImagePos;
        }
        mImageSwitcher.setInAnimation(delta > 0 ? mSlideInRight : mSlideInLeft);
        mImageSwitcher.setOutAnimation(delta > 0 ? mSlideOutLeft : mSlideOutRight);
        mCurrentPosition = nextImagePos;
        loadImage(mCurrentPosition);
    }

    private void loadImage(int mCurrentPosition) {
        ImageView view = new ImageView(this);
        new DownloadClass(mImageSwitcher, view).execute(mImages.get(mCurrentPosition));
    }

    private int getStartLimit(int currentId) {
        int start = (currentId - Constants.FETCH_LENGTH);
        if (start > 0) {
            return start;
        } else {
            return Constants.IMAGE_START_ID;
        }
    }

    private class SwipeListener extends SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 75;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    moveNextOrPrevious(1);
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    moveNextOrPrevious(-1);
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
    }

    private class DownloadClass extends AsyncTask<String, Void, Bitmap> {
        ImageSwitcher imgSwt;
        ImageView imgView;
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);

        public DownloadClass(ImageSwitcher imgSwt, ImageView imgView) {
            this.imgSwt = imgSwt;
            this.imgView = imgView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap result = null;
            try {
                result = Picasso.with(SinglePhotoViewActivity.this).load(urldisplay).resize(1024, 768).centerInside().get();
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            imgView.setImageBitmap(result);
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            pb.setVisibility(View.GONE);
            imgSwt.setImageDrawable(imgView.getDrawable());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }
    }
}