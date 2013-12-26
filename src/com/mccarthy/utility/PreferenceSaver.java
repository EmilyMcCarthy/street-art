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
public class PreferenceSaver {
    public static final String SESSION_TOKEN_PREFERENCES = "SessionTokenPreferences";
    public static final String SESSION_TOKEN_KEY = "SessionTokenKey";
    public static final String ACCESS_LIST_KEY = "AccessListKey";
    public void storeSessionToken(Context context, CMSessionToken sessionToken) {

        String sessionTokenKey = SESSION_TOKEN_KEY;
        String sessionToken1 = sessionToken.getSessionToken();
        saveString(context, sessionTokenKey, sessionToken1);
    }

    private void saveString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(key, value);
        editor.apply();
    }

    private SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    public CMSessionToken getSessionToken(Context context) {
        String token = getSharedPreferences(context).getString(SESSION_TOKEN_KEY, null);
        if(Strings.isEmpty(token)) return CMSessionToken.FAILED;
        else                       return new CMSessionToken(token, new Date());
    }

    public void storeAccessListId(Context context, String accessListId) {
        saveString(context, ACCESS_LIST_KEY, accessListId);

    }

    public String getAccessListId(Context context) {
        String token = getSharedPreferences(context).getString(ACCESS_LIST_KEY, null);
        return token;
    }

    private SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SESSION_TOKEN_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void clearSessionTokenAndAccessList(Context context) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(SESSION_TOKEN_KEY, null);
        editor.putString(ACCESS_LIST_KEY, null);
        editor.apply();
    }
}
