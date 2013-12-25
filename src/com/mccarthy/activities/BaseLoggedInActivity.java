package com.mccarthy.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;
import com.cloudmine.api.CMSessionToken;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.mccarthy.R;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
public class BaseLoggedInActivity extends RoboActionBarActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{

    public static final String SESSION_TOKEN_KEY = "SessionTokenKey";

    public static void addSessionTokenToIntent(Intent intent, CMSessionToken sessionToken) {
        intent.putExtra(SESSION_TOKEN_KEY, sessionToken.getSessionToken());
    }

    public static final String TAG = "Street-Art";

    protected LocationClient locationClient;
    protected CMSessionToken sessionToken;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionToken = new CMSessionToken(getIntent().getStringExtra(SESSION_TOKEN_KEY), new Date());
        locationClient = new LocationClient(this, this, this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
    }

    public void onStart() {
        super.onStart();
        if(checkServicesConnected()) locationClient.connect();
    }

    public void onStop() {
        super.onStop();
        locationClient.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        for(Integer menuId : getMenuIds()) {
            if(menuId == null)continue;
            menuInflater.inflate(menuId, menu);
        }
        return showActionBar() && super.onCreateOptionsMenu(menu);
    }

    protected boolean showActionBar() {
        return true;
    }

    protected List<Integer> getMenuIds() {
        return Collections.EMPTY_LIST;
    }

    /** GOOGLE PLAY SERVICES **/
    protected final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    protected boolean checkServicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Get the error code
            int errorCode = ConnectionResult.iP.getErrorCode();
            // Get the error dialog from Google Play services
            showErrorDialog(errorCode);
        }
        return false;
    }

    protected void showErrorDialog(int errorCode) {
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                this,
                CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {
            // Create a new DialogFragment for the error dialog
            ErrorDialogFragment errorFragment =
                    new ErrorDialogFragment();
            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);
            // Show the error dialog in the DialogFragment
            errorFragment.show(getSupportFragmentManager(),
                    "Location Updates");
        } else {
            Toast.makeText(this, R.string.missing_play_store, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Connected to location service");
    }

    @Override
    public void onDisconnected() {
        Log.d(TAG, "Disconnected to location service");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
      /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            showErrorDialog(connectionResult.getErrorCode());
        }

    }
}
