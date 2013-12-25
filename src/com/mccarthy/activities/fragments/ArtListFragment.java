package com.mccarthy.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.android.volley.Response;
import com.cloudmine.api.CMSessionToken;
import com.cloudmine.api.SearchQuery;
import com.cloudmine.api.rest.ObjectLoadRequestBuilder;
import com.cloudmine.api.rest.response.CMObjectResponse;
import com.google.inject.Inject;
import com.mccarthy.R;
import com.mccarthy.activities.BaseLoggedInActivity;
import com.mccarthy.models.StreetArt;
import com.mccarthy.utility.ErrorHandling;
import roboguice.fragment.RoboListFragment;
import roboguice.inject.InjectView;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */

public class ArtListFragment extends RoboListFragment{

    @Inject
    private ErrorHandling errorHandling;
    @InjectView
    private ListView artList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.art_list, container, false);

        String searchQuery = SearchQuery.filter(StreetArt.class).and("location").near()
        CMSessionToken sessionToken = ((BaseLoggedInActivity) getActivity()).getSessionToken();
        new ObjectLoadRequestBuilder(sessionToken, new Response.Listener<CMObjectResponse>() {
            @Override
            public void onResponse(CMObjectResponse cmObjectResponse) {

            }
        }, errorHandling.defaultErrorListener(getActivity(), R.string.error_loading_art)).search();

        return view;
    }
}
