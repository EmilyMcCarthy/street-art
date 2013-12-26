package com.mccarthy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.cloudmine.api.CMSessionToken;
import com.google.inject.Inject;
import com.mccarthy.utility.PreferenceSaver;

import java.util.Collections;
import java.util.List;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
public class BaseLoggedInActivity extends RoboActionBarActivity {

    public static final String SESSION_TOKEN_KEY = "SessionTokenKey";

    public static void addSessionTokenToIntent(Intent intent, CMSessionToken sessionToken) {
        intent.putExtra(SESSION_TOKEN_KEY, sessionToken.getSessionToken());
    }

    public static final String TAG = "Street-Art";

    protected CMSessionToken sessionToken;
    @Inject
    private PreferenceSaver preferenceSaver;

    public void onCreate(Bundle savedInstanceState) {
        sessionToken = new CMSessionToken(getIntent().getStringExtra(SESSION_TOKEN_KEY), null);
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
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

    public CMSessionToken getSessionToken() {
        return sessionToken;
    }

    public void onLogout(MenuItem item) {
        startActivity(LoginActivity.newInstance(this, preferenceSaver));
    }

    protected List<Integer> getMenuIds() {
        return Collections.EMPTY_LIST;
    }


}
