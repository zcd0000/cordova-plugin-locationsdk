package com.yqtms.locationsdk.amap.listener;

import com.amap.api.location.AMapLocation;
import com.hdgq.locationlib.listener.OnGetLocationListener;
import com.hdgq.locationlib.listener.OnGetLocationResultListener;

public class AMapOnGetLocationListener implements OnGetLocationListener {
    private static OnGetLocationResultListener mOnResultListener;
    private static int mOnReceiveTime = 0;
    private static AMapOnGetLocationListener mLocationListener;

    private AMapOnGetLocationListener() {
    }

    public static AMapOnGetLocationListener getInstance() {
        if (mLocationListener == null) {
            mLocationListener = new AMapOnGetLocationListener();
        }

        return mLocationListener;
    }

    public static void setOnResultListener(OnGetLocationResultListener onResultListener) {
        mOnResultListener = onResultListener;
    }

    public void onGetLocationStart() {
        mOnReceiveTime = 0;
    }

    public void onReceiveLocation(AMapLocation mAMapLocation) {
        ++mOnReceiveTime;
        if (mOnReceiveTime < 2) {
            mOnResultListener.onGetLocationSuccess(mAMapLocation);
        } else {
            mOnReceiveTime = 0;
        }

    }

    public void onGetLocationTimeOut() {
        mOnResultListener.onFailure("110000", "定位超时");
    }

    public void onGetLocationFailReturnMessage(String message) {
        mOnResultListener.onFailure("110001", "定位失败," + message);
    }
}
