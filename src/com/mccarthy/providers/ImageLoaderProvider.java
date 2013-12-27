package com.mccarthy.providers;

import android.app.Application;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.cloudmine.api.rest.CMImageLoader;
import com.cloudmine.api.rest.DiskBitmapCache;
import com.cloudmine.api.rest.SharedRequestQueueHolders;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
@ContextSingleton
public class ImageLoaderProvider {
    @Inject
    private Application context;
    private ImageLoader imageLoader;
    public synchronized ImageLoader getImageLoader() {
        if(imageLoader == null){
            RequestQueue queue = SharedRequestQueueHolders.getRequestQueue(context);
            imageLoader = new CMImageLoader(queue, new DiskBitmapCache(context));
        }
        return imageLoader;
    }
}
