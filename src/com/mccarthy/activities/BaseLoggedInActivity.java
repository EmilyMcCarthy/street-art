package com.mccarthy.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;

import java.util.Collections;
import java.util.List;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
public class BaseLoggedInActivity extends RoboActionBarActivity {

    public static final String TAG = "Street-Art";

    public void onCreate(Bundle savedInstanceState) {
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

    protected List<Integer> getMenuIds() {
        return Collections.EMPTY_LIST;
    }
}
