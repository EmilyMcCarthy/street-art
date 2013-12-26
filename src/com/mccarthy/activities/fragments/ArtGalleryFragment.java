package com.mccarthy.activities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.cloudmine.api.rest.CMImageLoader;
import com.cloudmine.api.rest.DiskBitmapCache;
import com.cloudmine.api.rest.SharedRequestQueueHolders;
import com.mccarthy.R;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
public class ArtGalleryFragment extends Fragment {
    
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.art_gallary, viewGroup, false);
        return view;
    }
}
