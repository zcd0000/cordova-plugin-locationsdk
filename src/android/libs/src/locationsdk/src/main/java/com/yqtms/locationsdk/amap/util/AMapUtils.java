package com.yqtms.locationsdk.amap.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yqtms.locationsdk.R;
import com.yqtms.locationsdk.amap.activity.TraceActivity;

public final class AMapUtils {


    private static Bitmap originalIcons = null;

    public static ImageView getImageViewForMarker(Context context, MarkerIcon icon){
        if(originalIcons == null){
            originalIcons = BitmapFactory.decodeResource(context.getResources(), R.mipmap.dir_marker);
        }

        int originalWidth = originalIcons.getWidth();
        int originalHeight = originalIcons.getHeight();

        int offset = 0;
        switch (icon){
            case Middle:
                offset = originalWidth / 3;
                break;
            case End:
                offset = originalWidth * 2 / 3;
                break;
        }
        int[] iconPixels = new int[ originalWidth * originalHeight / 3 ];
        originalIcons.getPixels(iconPixels,0,originalWidth / 3,offset,0,originalWidth / 3, originalHeight);
        Bitmap loadIconMap = Bitmap.createBitmap(iconPixels,originalWidth / 3,originalHeight, Bitmap.Config.ARGB_8888);
        ImageView iconView = new ImageView(context);
        iconView.setImageBitmap(loadIconMap);
        iconView.setLayoutParams(new LinearLayout.LayoutParams(110,97));

        return iconView;
    }

    public enum MarkerIcon
    {
        Start,Middle,End
    }
}
