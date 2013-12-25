package com.mccarthy.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Response;
import com.cloudmine.api.CMFile;
import com.cloudmine.api.CMSessionToken;
import com.cloudmine.api.Strings;
import com.cloudmine.api.rest.FileCreationRequest;
import com.cloudmine.api.rest.SharedRequestQueueHolders;
import com.cloudmine.api.rest.response.FileCreationResponse;
import com.cloudmine.api.rest.response.ObjectModificationResponse;
import com.google.inject.Inject;
import com.mccarthy.R;
import com.mccarthy.models.StreetArt;
import com.mccarthy.utility.ErrorHandling;
import com.mccarthy.utility.Photo;
import com.mccarthy.utility.SessionTokenAccess;
import com.mccarthy.utility.Storage;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import java.io.IOException;
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
    @Inject
    private Storage storageUtility;
    @Inject
    private SessionTokenAccess sessionTokenAccess;
    @Inject
    private ErrorHandling errorHandling;
    @InjectView(R.id.img_photo)
    private ImageView photoView;
    @InjectView(R.id.txt_title)
    private EditText titleText;
    @InjectView(R.id.txt_description)
    private EditText descriptionText;
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
                    }else {
                        Log.e(TAG, "Got a photo response but don't have a photo path");
                    }
                }
                break;
        }
    }

    protected List<Integer> getMenuIds() {
        return Arrays.asList(R.menu.find_art, R.menu.take_picture, R.menu.save);
    }

    public void onFindArt(MenuItem item) {
        startActivity(FindArtActivity.newIntent(this, sessionToken));
    }

    public void onTakePicture(MenuItem item) {
        startTakePictureIntent();
    }

    public void onSave(MenuItem item) {
        if(Strings.isEmpty(photoPath)) {
            photoView.setImageResource(R.drawable.image_missing);
            Toast.makeText(this, R.string.missing_image, Toast.LENGTH_LONG).show();
            return;
        }
        final String title = titleText.getText().toString();
        if(Strings.isEmpty(title)) {
            titleText.setError(getString(R.string.missing_title));
            return;
        }

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle(R.string.saving_art);
        dialog.show();
        new AsyncTask<Void, Void, byte[]>() {
            @Override
            protected byte[] doInBackground(Void... params) {
                try {
                    return storageUtility.readFile(photoPath);
                } catch (IOException e) {
                    return null;
                }
            }

            public void onPostExecute(byte[] fileBytes) {
                if(fileBytes == null) {
                    Toast.makeText(AddPhotosActivity.this, R.string.error_reading_file, Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    return;
                }
                CMFile photoFile = new CMFile(fileBytes, null, "image/jpg");
                Log.e(TAG, "Saving photo");
                SharedRequestQueueHolders.getRequestQueue(AddPhotosActivity.this).add(new FileCreationRequest(photoFile, new Response.Listener<FileCreationResponse>() {
                    @Override
                    public void onResponse(FileCreationResponse fileCreationResponse) {
                        Log.e(TAG, "File saved, saving art");
                        Location currentLocation = locationClient.getLastLocation();
                        if(currentLocation == null){ //TODO redo this section
                            currentLocation = new Location("gps");
                            currentLocation.setLatitude(0);
                            currentLocation.setLongitude(0);
                        }

                        StreetArt art = new StreetArt(currentLocation.getLatitude(), currentLocation.getLongitude(), title, descriptionText.getText().toString(), fileCreationResponse.getfileId());
                        art.addAccessListId(sessionTokenAccess.getAccessListId(AddPhotosActivity.this));
                        art.save(AddPhotosActivity.this, sessionToken, new Response.Listener<ObjectModificationResponse>() {
                            @Override
                            public void onResponse(ObjectModificationResponse objectModificationResponse) {
                                Log.e(TAG, "Art saved, dismissing dialog");
                                dialog.dismiss();
                            }
                        }, errorHandling.defaultErrorListener(AddPhotosActivity.this, R.string.error_saving_file, dialog));
                    }
                }, errorHandling.defaultErrorListener(AddPhotosActivity.this, R.string.error_saving_file, dialog)));
            }
        }.execute();
    }
}
