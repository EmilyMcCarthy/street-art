package com.mccarthy.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.mccarthy.R;

import java.util.Collections;
import java.util.List;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
public class FindArtActivity extends BaseLoggedInActivity {
    public static Intent newIntent(Context callingContext) {
        Intent intent = new Intent(callingContext, FindArtActivity.class);
        return intent;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected List<Integer> getMenuIds() {
        return Collections.singletonList(R.menu.add_photos);
    }

    public void onAddPhotos(MenuItem item) {
        startActivity(AddPhotosActivity.newIntent(this));
    }
}
