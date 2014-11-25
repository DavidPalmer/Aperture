package com.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.common.Constants;
import com.rewyndr.reflectbig.common.PhotoType;
import com.rewyndr.reflectbig.common.PreferenceConstants;
import com.rewyndr.reflectbig.interfaces.PhotoService;
import com.rewyndr.reflectbig.model.Event;
import com.rewyndr.reflectbig.service.ServiceFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SinglePhotoViewActivity extends Activity {
    final String className = this.getClass().getName();
    View decorView;
    int currentId = 0;
    private ImageSwitcher mImageSwitcher;
    private View mOverscrollLeft;
    private View mOverscrollRight;
    private GestureDetector mGestureDetector;
    private Integer mCurrentPosition = null;
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
    private Event event = null;
    private FrameLayout layout;

    private static int getEndLimit(int currentId, int total) {
        int start = (currentId + Constants.FETCH_LENGTH);
        if (start < total) {
            return start;
        } else {
            return total;
        }
    }

    public void setCurrentId() {
        Intent intent = getIntent();
        currentId = intent.getExtras().getInt("id");
        event = (Event) intent.getSerializableExtra("event");
        eventId = event.getEventId();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentId();
        decorView = getWindow().getDecorView();
        setTitle(event.getEventName());
        setContentView(R.layout.activity_photo_single_view);
        mImageSwitcher = (ImageSwitcher) findViewById(R.id.image);
        mOverscrollLeft = findViewById(R.id.overscroll_left);
        mOverscrollRight = findViewById(R.id.overscroll_right);
        SharedPreferences preferences = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if(preferences.getBoolean(PreferenceConstants.WAS_PAUSED, false)) {
            editor.putString(PreferenceConstants.CURRENT_POSITION, String.valueOf(0));
            editor.putBoolean(PreferenceConstants.WAS_PAUSED, false);
            editor.commit();
        }

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
    }


    @Override
    public void onResume() {
        super.onResume();

        // Read the value from preferences to check if application was just minimized. This can be checked by checking the value of
        // was_paused. As, it will be made to false if destroyed, but it will be true if just paused and waiting for resume
        SharedPreferences pref = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
        boolean flag = pref.getBoolean(PreferenceConstants.WAS_PAUSED, false);
        if(flag) {
            // This means the user minimized the application and then opened it again. Hence, no need to show the image again
            // Just reset this flag as the image must change the next time.
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(PreferenceConstants.WAS_PAUSED);
        } else {
            // This gets executed on happy scenarios
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

            String prevValue = getCurrentPositionFromPause();
            mCurrentPosition = (prevValue == null) ? currentId : Integer.valueOf(prevValue);

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
            Log.d(className, "Current Position : " + String.valueOf(mCurrentPosition));
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
                    Log.d(className, "Came here");
                    final View contentView = findViewById(R.id.image);
                    contentView.setLayoutParams(new ViewGroup.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
                }
            });
            hideSystemUI();
        }
    }

    private String getCurrentPositionFromPause() {
        SharedPreferences preferences = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
        return preferences.getString(PreferenceConstants.CURRENT_POSITION, null);
    }

    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LOW_PROFILE);
        } else {
            decorView.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // There are two times when this activity gets destroyed.
        // When orientation gets changed, the isFinishing() -> False and hence the current position is retained
        // When back is pressed, current position is to be removed
        Log.d(className, "Calling On Destroy()");
        SharedPreferences preferences = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PreferenceConstants.WAS_PAUSED, false);
        if (isFinishing()) {
            editor.remove(PreferenceConstants.CURRENT_POSITION);
        }
        editor.commit();
    }

    @Override
    public void onPause() {
        super.onPause();

        // Saving the current position and paused status for next processing.
        SharedPreferences preferences = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PreferenceConstants.CURRENT_POSITION, String.valueOf(mCurrentPosition));
        editor.putBoolean(PreferenceConstants.WAS_PAUSED, true);
        editor.commit();
    }

    private void moveNextOrPrevious(int delta) throws Exception {
        int nextImagePos = mCurrentPosition + delta;
        if (nextImagePos < 0) {
            int currentEnd = currentStart + mImages.size();
            Log.d(className, "Current URLs start " + currentStart + "end " + currentEnd);

            int fetchStart = Math.max(currentStart - Constants.FETCH_LENGTH, 0);
            int fetchEnd = currentStart - 1;
            Log.d(className, "URls start " + fetchStart + "end " + fetchEnd);

            PhotoService viewPhoto = ServiceFactory.getPhotoServiceInstance(this);
            List<String> newUrls = new ArrayList<String>();
            try {
                newUrls = viewPhoto.getPhotos(eventId, fetchStart, fetchEnd, PhotoType.SMALL);
                Log.d(className, "URls size " + newUrls.size());
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
            Log.d(className, "Current URLs start " + currentStart + "end " + currentEnd);

            PhotoService viewPhoto = ServiceFactory.getPhotoServiceInstance(this);
            int total = viewPhoto.getCount(eventId);
            int fetchStart = currentEnd + 1;
            int fetchEnd = Math.min(currentEnd + Constants.FETCH_LENGTH, total);
            List<String> newUrls = viewPhoto.getPhotos(eventId, fetchStart, fetchEnd, PhotoType.SMALL);
            Log.d(className, "URls size " + newUrls.size());
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
        Log.d(className, "Total Url size : " + mImages.size());
        new DownloadClass(mImageSwitcher, view).execute(mImages.get(mCurrentPosition));

        /* Have to make use of some caching mechanism. This below is DIL's Mechanism. But this is not working for me. See this.

        ImageLoader loader = new ImageLoader(context);
        loader.DisplayImage(mImages.get(mCurrentPosition), view);
        mImageSwitcher.setImageDrawable(view.getDrawable());*/
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
                Log.e(className + " : Error", e.getMessage());
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