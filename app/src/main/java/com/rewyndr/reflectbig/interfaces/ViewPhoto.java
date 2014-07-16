package com.rewyndr.reflectbig.interfaces;

import java.util.List;

/**
 * This provides methods to retrieve photos from the backend
 * Created by Satish on 7/12/2014.
 */
public interface ViewPhoto {

    /**
     * This retrieves photos starting from <tt>start</tt> to <tt>end</tt>
     *
     * @param start the starting number of photos to retrieve
     * @param end   the end number of photos to retrieve
     * @return the url of all photos
     */
    List<String> getPhotos(int start, int end) throws Exception;

    /**
     * This retrieves a single photo
     *
     * @param num the number of the photo to retrieve
     * @return the url of the required photo
     */
    List<String> getPhoto(int num) throws Exception;
}
