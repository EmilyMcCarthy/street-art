package com.mccarthy.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.inject.Singleton;
import com.mccarthy.activities.BaseLoggedInActivity;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
@Singleton
public class ErrorHandling {

    public Response.ErrorListener defaultErrorListener(final Context context, final int textId) {
        return defaultErrorListener(context, textId, null);
    }

    public Response.ErrorListener defaultErrorListener(final Context context, final int textId, final ProgressDialog progressDialog) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(BaseLoggedInActivity.TAG, "Default Error Listener", volleyError);
                if(progressDialog != null) progressDialog.dismiss();
                Toast.makeText(context, textId, Toast.LENGTH_LONG).show();
            }
    };
    }
}
