package com.rewyndr.reflectbig.interfaces;

import com.rewyndr.reflectbig.common.PhotoType;

import java.util.List;

/**
 * This provides methods to retrieve photos from the backend
 * Created by Satish on 7/12/2014.
 */
public interface ViewPhoto {

    /**
     * This retrieves photos of actual size starting from <tt>start</tt> to <tt>end</tt>
     *
     * @param start     the starting number of photos to retrieve
     * @param end       the end number of photos to retrieve
     * @param photoType the type of photo to retrieve
     * @return the url of all photos
     * @throws Exception in case of any exception
     */
    List<String> getPhotos(int start, int end, PhotoType photoType) throws Exception;

    /**
     * This retrieves a single photo in actual size
     *
     * @param num       the number of the photo to retrieve
     * @param photoType the type of photo to retrieve
     * @return the url of the required photo
     * @throws Exception in case of any exception
     */
    String getPhoto(int num, PhotoType photoType) throws Exception;

    /**
     * This retrieves the total number of photos
     *
     * @return the number of photos
     * @throws Exception in case of any exception
     */
    int getCount() throws Exception;

}
