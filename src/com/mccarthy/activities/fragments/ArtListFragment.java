package com.mccarthy.activities.fragments;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import com.android.volley.Response;
import com.cloudmine.api.CMSessionToken;
import com.cloudmine.api.SearchQuery;
import com.cloudmine.api.rest.BaseObjectLoadRequest;
import com.cloudmine.api.rest.ObjectLoadRequestBuilder;
import com.cloudmine.api.rest.SharedRequestQueueHolders;
import com.cloudmine.api.rest.response.CMObjectResponse;
import com.google.inject.Inject;
import com.mccarthy.R;
import com.mccarthy.activities.BaseLoggedInActivity;
import com.mccarthy.models.StreetArt;
import com.mccarthy.utility.ErrorHandling;
import com.mccarthy.utility.LocationClientWrapper;
import com.mccarthy.views.ArtListAdapter;
import roboguice.fragment.RoboListFragment;

import java.util.List;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */

public class ArtListFragment extends RoboListFragment{

    @Inject
    private ErrorHandling errorHandling;
    @Inject
    private LocationClientWrapper locationProvider;

    public void onViewCreated(View view, Bundle savedInstanceState) {
        Location lastLocation = locationProvider.getLastLocation();
        String searchQuery = SearchQuery.filter(StreetArt.class).and("location").near(lastLocation.getLongitude(), lastLocation.getLatitude()).searchQuery();
        CMSessionToken sessionToken = ((BaseLoggedInActivity) getActivity()).getSessionToken();

        setEmptyText(getString(R.string.loading_art));
        BaseObjectLoadRequest request = new ObjectLoadRequestBuilder(sessionToken, new Response.Listener<CMObjectResponse>() {
            @Override
            public void onResponse(CMObjectResponse cmObjectResponse) {
                Activity activity = getActivity();
                if(activity != null && isAdded()){
                    setEmptyText(getString(R.string.no_art));
                    List<StreetArt> streetArtList = cmObjectResponse.getObjects(StreetArt.class);
                    setListAdapter(new ArtListAdapter(activity, streetArtList));
                }
            }
        }, errorHandling.defaultErrorListener(getActivity(), R.string.error_loading_art)).search(searchQuery).getShared().limit(10).getCount().build();
        SharedRequestQueueHolders.getRequestQueue(getActivity()).add(request);
    }
}
