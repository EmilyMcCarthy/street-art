package com.mccarthy.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.android.volley.RequestQueue;
import com.cloudmine.api.rest.CMImageLoader;
import com.cloudmine.api.rest.DiskBitmapCache;
import com.cloudmine.api.rest.SharedRequestQueueHolders;
import com.mccarthy.R;
import com.mccarthy.models.StreetArt;

import java.util.List;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
public class ArtListAdapter extends BaseAdapter {

    private final List<StreetArt> artList;
    private final Context context;
    private final CMImageLoader imageLoader;
    public ArtListAdapter(Context context, List<StreetArt> artList) {
        this.context = context;
        this.artList = artList;
        RequestQueue queue = SharedRequestQueueHolders.getRequestQueue(context);
        imageLoader = new CMImageLoader(queue, new DiskBitmapCache(context));
    }

    @Override
    public int getCount() {
        return artList.size();
    }

    @Override
    public Object getItem(int position) {
        return artList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArtRowView artRow;
        if(convertView instanceof ArtRowView) {
            artRow = (ArtRowView) convertView;
        } else {
            artRow = (ArtRowView) View.inflate(context, R.layout.art_row, null);
        }
        artRow.setStreetArt(artList.get(position), imageLoader);
        return artRow;
    }
}
