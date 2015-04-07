package io.rewyndr.reflectbig.interfaces;

import io.rewyndr.reflectbig.common.PhotoType;

import java.io.File;
import java.util.List;

/**
 * This provides methods upload, retrieve photos from the backend
 * Created by Satish on 9/2/2014.
 */
public interface PhotoService {

    /**
     * This retrieves photos from an event starting from <tt>start</tt> to <tt>end</tt>
     *
     * @param eventId   the id of the event
     * @param start     the starting number of photos to retrieve
     * @param end       the end number of photos to retrieve
     * @param photoType the type of photo to retrieve
     * @return the url of all photos
     * @throws Exception in case of any exception
     */
    List<String> getPhotos(String eventId, int start, int end, PhotoType photoType) throws Exception;

    /**
     * This retrieves a single photo from an event
     *
     * @param eventId   the id of the event
     * @param num       the number of the photo to retrieve
     * @param photoType the type of photo to retrieve
     * @return the url of the required photo
     * @throws Exception in case of any exception
     */
    String getPhoto(String eventId, int num, PhotoType photoType) throws Exception;

    /**
     * This retrieves the total number of photos in an event
     *
     * @param eventId the id of the event
     * @return the number of photos
     * @throws Exception in case of any exception
     */
    int getCount(String eventId) throws Exception;

    /**
     * This method uploads a photo into the server
     *
     * @param eventId the id of the event
     * @param file    the file object representing the photo
     * @throws Exception
     */
    void uploadPhoto(String eventId, File file) throws Exception;
}
