package com.mccarthy.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.mccarthy.R;

/**
 * <br>
 * Copyright CloudMine LLC. All rights reserved<br>
 * See LICENSE file included with SDK for details.
 */
public class LoginActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void onLogin(View view) {
    }

    public void onCreateAccount(View view) {

    }

    public void onSkipLogin(View view) {
        startActivity(FindArtActivity.newIntent(this));
    }
}
