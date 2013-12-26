package com.mccarthy.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.android.volley.Response;
import com.cloudmine.api.ACMUser;
import com.cloudmine.api.CMAccessPermission;
import com.cloudmine.api.Strings;
import com.cloudmine.api.db.BaseLocallySavableCMAccessList;
import com.cloudmine.api.rest.SharedRequestQueueHolders;
import com.cloudmine.api.rest.response.CreationResponse;
import com.cloudmine.api.rest.response.LoginResponse;
import com.google.inject.Inject;
import com.mccarthy.R;
import com.mccarthy.utility.ErrorHandling;
import com.mccarthy.utility.PreferenceSaver;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
@ContentView(R.layout.create_account)
public class CreateAccountActivity extends RoboActionBarActivity {

    public static Intent newIntent(Context callingContext) {
        return new Intent(callingContext, CreateAccountActivity.class);
    }


    @Inject
    private ErrorHandling errorHandling;
    @Inject
    private PreferenceSaver preferenceSaver;
    @InjectView(R.id.txt_password)
    private EditText passwordTxt;
    @InjectView(R.id.txt_confirm_password)
    private EditText confirmPasswordTxt;
    @InjectView(R.id.txt_username)
    private EditText usernameTxt;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
        SharedRequestQueueHolders.getRequestQueue(this).cancelAll(this);
    }

    public void onCreateAccount(View view) {
        String password = passwordTxt.getText().toString();
        String confirmPassword = confirmPasswordTxt.getText().toString();
        String username = usernameTxt.getText().toString();
        boolean hadErrors = false;
        if(Strings.isEmpty(password)) {
            hadErrors = true;
            passwordTxt.setError(getText(R.string.account_missing_password));
        } else {
            passwordTxt.setError(null);
        }
        if(Strings.isEmpty(username)) {
            hadErrors = true;
            usernameTxt.setError(getText(R.string.account_missing_username));
        } else {
            usernameTxt.setError(null);
        }
        if(!confirmPassword.equals(password)) {
            hadErrors = true;
            confirmPasswordTxt.setError(getText(R.string.account_mismatched_password));
        } else {
            confirmPasswordTxt.setError(null);
        }
        if(!hadErrors) {
            final ACMUser user = new ACMUser(null, username, password);
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle(R.string.creating_account);
            dialog.show();
            user.create(this, new Response.Listener<CreationResponse>() {
                @Override
                public void onResponse(CreationResponse creationResponse) {
                    if(creationResponse.wasSuccess()) {
                        dialog.setTitle(R.string.logging_in);
                        user.login(CreateAccountActivity.this, new Response.Listener<LoginResponse>() {
                            @Override
                            public void onResponse(LoginResponse loginResponse) {
                                user.setSessionToken(loginResponse.getSessionToken());
                                preferenceSaver.storeSessionToken(CreateAccountActivity.this, loginResponse.getSessionToken());
                                createAndSaveAccessList();

                                dialog.dismiss();
                                startActivity(FindArtActivity.newIntent(CreateAccountActivity.this, loginResponse.getSessionToken()));
                            }

                            private void createAndSaveAccessList() {
                                BaseLocallySavableCMAccessList accessList = new BaseLocallySavableCMAccessList(user, CMAccessPermission.READ);
                                accessList.setLoggedIn(true);
                                accessList.saveEventually(CreateAccountActivity.this);
                                accessList.save(CreateAccountActivity.this, null, null); //if this fails, save eventually will take care of this
                                preferenceSaver.storeAccessListId(CreateAccountActivity.this, accessList.getObjectId());
                            }
                        }, errorHandling.defaultErrorListener(CreateAccountActivity.this, R.string.failed_login, dialog));
                    }
                }
            }, errorHandling.defaultErrorListener(this, R.string.failed_account_create, dialog));

        }
    }
}
