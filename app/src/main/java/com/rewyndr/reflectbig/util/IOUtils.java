package com.rewyndr.reflectbig.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This provides utility methods for IO operations
 * Created by Satish on 7/24/2014.
 */
public class IOUtils {

    protected IOUtils() {

    }

    /**
     * This method converts a file to byte array
     *
     * @param file the file object representing the file
     * @return a byte array with the contents of the file
     * @throws IOException
     */
    public static byte[] convertFileToBytes(File file) throws IOException {
        byte[] bFile = new byte[(int) file.length()];
        FileInputStream fileInputStream = null;

        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            return bFile;
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }
}
