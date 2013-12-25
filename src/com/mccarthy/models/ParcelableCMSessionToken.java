package com.mccarthy.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.cloudmine.api.CMSessionToken;

import java.util.Date;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
//TODO redo the Android library to drop Java support
public class ParcelableCMSessionToken extends CMSessionToken implements Parcelable{
    public static final Creator<ParcelableCMSessionToken> CREATOR = new Creator<ParcelableCMSessionToken>() {
        @Override
        public ParcelableCMSessionToken createFromParcel(Parcel source) {
            return new ParcelableCMSessionToken(source);
        }

        @Override
        public ParcelableCMSessionToken[] newArray(int size) {
            return new ParcelableCMSessionToken[size];
        }
    };

    public ParcelableCMSessionToken(String sessionToken, Date expires) {
        super(sessionToken, expires);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getSessionToken());
        dest.writeLong(getExpiredDate().getTime());
    }

    private ParcelableCMSessionToken(Parcel input) {
        super(input.readString(), new Date(input.readLong()));
    }
}
