package com.rewyndr.reflectbig.interfaces;

import com.parse.ParseException;

import java.util.List;

/**
 * This provides methods to retrieve photos from the backend
 * Created by Satish on 7/12/2014.
 */
public interface ViewPhoto {

    /**
     * This retrieves photos of actual size starting from <tt>start</tt> to <tt>end</tt>
     *
     * @param start the starting number of photos to retrieve
     * @param end   the end number of photos to retrieve
     * @return the url of all photos
     * @throws Exception in case of any exception
     */
    List<String> getPhotos(int start, int end) throws Exception;

    /**
     * This retrieves a single photo in actual size
     *
     * @param num the number of the photo to retrieve
     * @return the url of the required photo
     * @throws Exception in case of any exception
     */
    String getPhoto(int num) throws Exception;

    /**
     * This retrieves photos of thumbnail size starting from <tt>start</tt> to <tt>end</tt>
     *
     * @param start the starting number of photos to retrieve
     * @param end   the end number of photos to retrieve
     * @return the url of all photos
     * @throws Exception in case of any exception
     */
    List<String> getPhotosThumbnail(int start, int end) throws Exception;

    /**
     * This retrieves a single photo in thumbnail size
     *
     * @param num the number of the photo to retrieve
     * @return the url of the required photo
     * @throws Exception in case of any exception
     */
    String getPhotoThumbnail(int num) throws Exception;

    /**
     * This retrieves photos of smaller size starting from <tt>start</tt> to <tt>end</tt>
     *
     * @param start the starting number of photos to retrieve
     * @param end   the end number of photos to retrieve
     * @return the url of all photos
     * @throws Exception in case of any exception
     */
    List<String> getPhotosSmall(int start, int end) throws Exception;

    /**
     * This retrieves a single photo in smaller size
     *
     * @param num the number of the photo to retrieve
     * @return the url of the required photo
     * @throws Exception in case of any exception
     */
    String getPhotoSmall(int num) throws Exception;

    /**
     * This retrieves the total number of photos
     * @return the number of photos
     * @throws Exception in case of any exception
     */
    int getCount() throws Exception;

}
