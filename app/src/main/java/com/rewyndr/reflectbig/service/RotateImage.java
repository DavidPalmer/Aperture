package com.rewyndr.reflectbig.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Raja on 1/22/2015.
 */
public class RotateImage {

    /*public Bitmap rotateImageFromURL(File file, Bitmap bitmap) {
        try {
            bitmap = cleanRotatedImage(file.getAbsolutePath(), bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap cleanRotatedImage(String photoPath, Bitmap bitmap) throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                bitmap = rotateImage(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                bitmap = rotateImage(bitmap, 180);
                break;
            // etc.
        }
        return bitmap;
    }*/

    public static File rotateImage(File photo, float angle) {
        File f;
        try {
            String filePath = photo.getAbsolutePath();
            Bitmap source = BitmapFactory.decodeFile(filePath);
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            Bitmap new_bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

            f = new File(filePath);
            Log.d("Rotating File: ",filePath + " by " + Float.toString(angle) + " degrees.");
            f.createNewFile();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            new_bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
        } catch (Exception e) {
            return photo;
        }
        return f;
    }
}
