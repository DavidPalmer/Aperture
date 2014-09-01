package com.rewyndr.reflectbig.interfaces;

import java.io.File;

/**
 * This provides methods to upload photo to the backend
 * Created by Satish on 7/24/2014.
 */
public interface UploadPhoto {

    /**
     * This method uploads a photo into the server
     * @param file the file object representing the photo
     * @throws Exception
     */
    void uploadPhoto(File file) throws Exception;
}
