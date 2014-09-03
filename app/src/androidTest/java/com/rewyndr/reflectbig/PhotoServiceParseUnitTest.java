package com.rewyndr.reflectbig;

import android.test.AndroidTestCase;
import android.util.Log;

import com.rewyndr.reflectbig.common.PhotoType;
import com.rewyndr.reflectbig.parse.impl.PhotoServiceParse;
import com.rewyndr.reflectbig.parse.impl.ViewPhotoParse;

import java.util.List;

/**
 * Created by Satish on 9/2/2014.
 */
public class PhotoServiceParseUnitTest extends AndroidTestCase {

    PhotoServiceParse instance = null;

    public void setUp() {
        instance = PhotoServiceParse.getInstance(getContext());
    }

    public void testGetPhotos() {
        try {
            List<String> results = instance.getPhotos("Fgw57rJi7w", 1, 10, PhotoType.ACTUAL);
            Log.d("com.rewyndr.reflectbig", String.valueOf(results));
            assertNotNull("Results cannot be null", results);
            assertEquals("Results size must match input size", 10, results.size());
        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }

}
