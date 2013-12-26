package com.mccarthy.activities;

import com.google.inject.Inject;
import com.mccarthy.utility.LocationClientWrapper;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
public class BaseLocationActivity extends BaseLoggedInActivity {

    @Inject
    protected LocationClientWrapper locationProvider;

    public void onStart() {
        super.onStart();
        locationProvider.tryConnect();
    }

    public void onStop() {
        super.onStop();
        locationProvider.disconnect();
    }
}
