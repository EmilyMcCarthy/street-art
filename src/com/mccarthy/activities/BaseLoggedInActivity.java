package com.mccarthy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import com.cloudmine.api.CMSessionToken;

import java.util.Collections;
import java.util.Date;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionToken = new CMSessionToken(getIntent().getStringExtra(SESSION_TOKEN_KEY), new Date());


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

    protected List<Integer> getMenuIds() {
        return Collections.EMPTY_LIST;
    }


}
