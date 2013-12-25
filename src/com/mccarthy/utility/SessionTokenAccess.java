package com.mccarthy.utility;

import android.content.Context;
import android.content.SharedPreferences;
import com.cloudmine.api.CMSessionToken;
import com.cloudmine.api.Strings;
import com.google.inject.Singleton;

import java.util.Date;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
@Singleton
public class SessionTokenAccess {
    public static final String SESSION_TOKEN_PREFERENCES = "SessionTokenPreferences";
    public static final String SESSION_TOKEN_KEY = "SessionTokenKey";
    public void storeSessionToken(Context context, CMSessionToken sessionToken) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SESSION_TOKEN_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putString(SESSION_TOKEN_KEY, sessionToken.getSessionToken());
    }

    public CMSessionToken getSessionToken(Context context) {
        String token = context.getSharedPreferences(SESSION_TOKEN_PREFERENCES, Context.MODE_PRIVATE).getString(SESSION_TOKEN_KEY, null);
        if(Strings.isEmpty(token)) return CMSessionToken.FAILED;
        else                       return new CMSessionToken(token, new Date());
    }
}
