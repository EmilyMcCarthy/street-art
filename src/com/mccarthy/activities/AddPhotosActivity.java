package com.mccarthy.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.ImageView;
import com.cloudmine.api.CMSessionToken;
import com.cloudmine.api.Strings;
import com.google.inject.Inject;
import com.mccarthy.R;
import com.mccarthy.utility.Photo;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import java.util.Arrays;
import java.util.List;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
@ContentView(R.layout.edit_photo)
public class AddPhotosActivity extends BaseLoggedInActivity {

    public static Intent newIntent(Context callingContext, CMSessionToken sessionToken) {
        Intent takePicturesIntent = new Intent(callingContext, AddPhotosActivity.class);
        addSessionTokenToIntent(takePicturesIntent, sessionToken);
        return takePicturesIntent;
    }

    private static final String PHOTO_PATH_KEY = "PhotoPathKey";

    @Inject
    private Photo photoUtility;
    @InjectView(R.id.img_photo)
    private ImageView photoView;
    private String photoPath;
    private boolean havePhotoToDisplay = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            photoPath = savedInstanceState.getString(PHOTO_PATH_KEY);
        }

        if(Strings.isEmpty(photoPath)) startTakePictureIntent();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if(Strings.isNotEmpty(photoPath)) savedInstanceState.putString(PHOTO_PATH_KEY, photoPath);
    }

    protected void startTakePictureIntent() {
        Pair<Intent, String> intentAndPath = photoUtility.createPhotoIntent();
        if(intentAndPath == null) return; //TODO error handling
        photoPath = intentAndPath.second;
        startActivityForResult(intentAndPath.first, Photo.PHOTO_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Photo.PHOTO_REQUEST:
                if(resultCode == RESULT_OK) {
                    if(Strings.isNotEmpty(photoPath)) {
                        Bitmap photo = photoUtility.readScaledPhoto(photoPath);
                        photoView.setImageBitmap(photo);
//                        photoView.setImageResource(R.drawable.banner);
                    }else {
                        Log.e(TAG, "Got a photo response but don't have a photo path");
                    }
                }
                break;
        }
    }

    protected List<Integer> getMenuIds() {
        return Arrays.asList(R.menu.find_art, R.menu.take_picture);
    }

    public void onFindArt(MenuItem item) {
        startActivity(FindArtActivity.newIntent(this, sessionToken));
    }

    public void onTakePicture(MenuItem item) {
        startTakePictureIntent();
    }
}
