package com.rewyndr.reflectbig;

import android.test.AndroidTestCase;

import com.rewyndr.reflectbig.common.PhotoType;
import com.rewyndr.reflectbig.parse.impl.ViewPhotoParse;

import java.util.List;

/**
 * Created by Satish on 7/17/2014.
 */
public class ViewPhotoParseUnitTest extends AndroidTestCase {

    ViewPhotoParse instance = null;

    public void setUp() {
        instance = ViewPhotoParse.getInstance(getContext());
    }

    public void testGetPhotos() {
        try {
            List<String> results = instance.getPhotos(1, 10, PhotoType.ACTUAL);
            assertNotNull("Results cannot be null", results);
            assertEquals("Results size must match input size", 10, results.size());
        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }

    public void testGetPhoto() {
        try {
            String result = instance.getPhoto(10, PhotoType.ACTUAL);
            assertNotNull("Results cannot be null", result);
            assertEquals("Url not matching", "http://files.parsetfss.com/415b86f6-7316-4359-a922-11a1d4d2f96f/tfss-d045e7d6-ec3f-4541-b084-ba172c535622-file0001376718168.jpg", result);
        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }

    public void testGetPhotosThumbnail() {
        try {
            List<String> results = instance.getPhotos(11, 20, PhotoType.THUMBNAIL);
            assertNotNull("Results cannot be null", results);
            assertEquals("Results size must match input size", 10, results.size());
        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }

    public void testGetPhotoThumbnail() {
        try {
            String result = instance.getPhoto(15, PhotoType.THUMBNAIL);
            assertNotNull("Results cannot be null", result);
            assertEquals("Url not matching", "http://files.parsetfss.com/415b86f6-7316-4359-a922-11a1d4d2f96f/tfss-a35113dd-62c9-4d50-b320-3efb5d6d7ea9-file0001608482449_640.jpg", result);
        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }

    public void testGetPhotosSmall() {
        try {
            List<String> results = instance.getPhotos(21, 30, PhotoType.SMALL);
            assertNotNull("Results cannot be null", results);
            assertEquals("Results size must match input size", 10, results.size());
        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }

    public void testGetPhotoSmall() {
        try {
            String result = instance.getPhoto(100, PhotoType.SMALL);
            assertNotNull("Results cannot be null", result);
            assertEquals("Url not matching", "http://files.parsetfss.com/415b86f6-7316-4359-a922-11a1d4d2f96f/tfss-b529add3-efe0-47e6-a161-c889d273c759-ch4_convent_1024.jpg", result);
        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }

    public void testGetCount() {
        try {
            int count = instance.getCount();
            assertEquals("Count not matching", 183, count);
        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }
}
