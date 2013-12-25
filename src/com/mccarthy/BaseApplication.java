package com.mccarthy;

import android.app.Application;
import com.cloudmine.api.CMApiCredentials;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
public class BaseApplication extends Application {

    private static final String API_KEY = "85fb0971ebec48b5a02c26bf6ceb5544";
    private static final String APP_ID = "3edda371a41d4fc0a60bf968b856a9de";

    public void onCreate() {
        super.onCreate();
        CMApiCredentials.initialize(APP_ID, API_KEY, this);
    }
}
