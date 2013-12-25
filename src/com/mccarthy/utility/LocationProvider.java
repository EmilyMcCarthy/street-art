package com.mccarthy.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.inject.Inject;
import com.mccarthy.R;
import roboguice.inject.ContextSingleton;

import java.util.HashSet;
import java.util.Set;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
@ContextSingleton
public class LocationProvider implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {
    /** GOOGLE PLAY SERVICES **/
    protected final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    public static final int DISCONNECT_EVENT = 500;
    public static final int CONNECT_EVENT = 501;
    public static final int CONNECT_FAILED_EVENT = 502;

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

    @Inject
    private Activity context;
    private LocationClient locationClient;
    private Set<Handler> connectionHandlers = new HashSet<Handler>();

    public Location getLastLocation() {
        return locationClient.getLastLocation();
    }

    public boolean tryConnect() {
        boolean canTry = checkServicesConnected();
        if(canTry) getLocationClient().connect();
        return canTry;
    }

    public void disconnect() {
        getLocationClient().disconnect();
    }

    public void addHandler(Handler handler) {
        connectionHandlers.add(handler);
    }

    public void removeHandler(Handler handler) {
        connectionHandlers.remove(handler);
    }

    public LocationClient getLocationClient() {
        if(locationClient == null) locationClient = new LocationClient(context, this, this);
        return locationClient;
    }

    protected boolean checkServicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(context);
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
                context,
                CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {
            // Create a new DialogFragment for the error dialog
            ErrorDialogFragment errorFragment =
                    new ErrorDialogFragment();
            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);
            // Show the error dialog in the DialogFragment TODO
//            errorFragment.show(context.getSupportFragmentManager(),
//                    "Location Updates");
        } else {
            Toast.makeText(context, R.string.missing_play_store, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        for(Handler handler : connectionHandlers) {
            Message message = handler.obtainMessage(CONNECT_EVENT, bundle);
            handler.sendMessage(message);
        }
    }

    @Override
    public void onDisconnected() {
        for(Handler handler : connectionHandlers) {
            Message message = handler.obtainMessage(DISCONNECT_EVENT);
            handler.sendMessage(message);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        for(Handler handler : connectionHandlers) {
            Message message = handler.obtainMessage(CONNECT_FAILED_EVENT);
            handler.sendMessage(message);
        }
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
                        context,
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
