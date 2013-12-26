package com.mccarthy.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mccarthy.R;
import com.mccarthy.models.StreetArt;

/**
 * <br>Copyright CloudMine LLC. All rights reserved
 * <br> See LICENSE file included with SDK for details.
 */
public class ArtRowView extends LinearLayout {

    private NetworkImageView thumbnail;
    private TextView titleText;

    public ArtRowView(Context context) {
        super(context);
    }

    public ArtRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setStreetArt(StreetArt art, ImageLoader imageLoader) {

        thumbnail.setImageUrl(art.getMainPhotoId(), imageLoader);
        titleText.setText(art.getTitle());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        thumbnail = (NetworkImageView) findViewById(R.id.img_thumbnail);
        titleText = (TextView) findViewById(R.id.txt_title);

        thumbnail.setDefaultImageResId(R.drawable.abc_spinner_ab_default_holo_dark);
        thumbnail.setErrorImageResId(R.drawable.image_missing);
    }
}
