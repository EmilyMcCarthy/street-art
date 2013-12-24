package com.mccarthy.utility;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import com.cloudmine.api.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
@Singleton
public class Photo {

    public static final int PHOTO_REQUEST = 416;
    public static final int DEFAULT_HEIGHT = 640;
    public static final int DEFAULT_WIDTH = 480;
    @Inject private Storage storage;

    public Pair<Intent, String> createPhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            String timeStamp =
                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = timeStamp + ".jpg";
            File photoFile = storage.createFile(imageFileName);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            return Pair.create(takePictureIntent, photoFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("Street-Art", "Failed creating photo file", e);
            return null;
        }
    }

    public Bitmap readScaledPhoto(String photoPath) {
        return readScaledPhoto(photoPath, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Bitmap readScaledPhoto(String photoPath, View containerView) {
        return readScaledPhoto(photoPath, containerView.getWidth(), containerView.getHeight());
    }

    public Bitmap readScaledPhoto(String photoPath, int targetWidth, int targetHeight) {
        if(Strings.isEmpty(photoPath)) {
            return null;
        }

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetWidth, photoH/targetHeight);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }
}
