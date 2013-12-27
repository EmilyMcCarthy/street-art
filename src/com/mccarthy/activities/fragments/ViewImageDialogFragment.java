package com.mccarthy.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.toolbox.NetworkImageView;
import com.google.inject.Inject;
import com.mccarthy.R;
import com.mccarthy.providers.ImageLoaderProvider;
import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.InjectView;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
public class ViewImageDialogFragment extends RoboDialogFragment {

    public static ViewImageDialogFragment newInstance(String imageId) {
            //Bitmap photo) {
        ViewImageDialogFragment fragment = new ViewImageDialogFragment();
        Bundle args = new Bundle();
//        args.putParcelable(PHOTO_KEY, photo);
        args.putString(PHOTO_KEY, imageId);
        fragment.setArguments(args);
        return fragment;
    }

    public static final String PHOTO_KEY = "PhotoKey";

    @InjectView(R.id.img_photo)
    private NetworkImageView photoView;
    @Inject
    private ImageLoaderProvider imageLoaderProvider;

    public ViewImageDialogFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.image_view, container);
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,  savedInstanceState);

//        Bitmap photo = getArguments().getParcelable(PHOTO_KEY);
//        photoView.setImageBitmap(photo);
        String imageId = getArguments().getString(PHOTO_KEY);
        photoView.setImageUrl(imageId, imageLoaderProvider.getImageLoader());
        photoView.setErrorImageResId(R.drawable.image_missing);
        photoView.setDefaultImageResId(R.drawable.abc_spinner_ab_default_holo_dark);
    }

}
