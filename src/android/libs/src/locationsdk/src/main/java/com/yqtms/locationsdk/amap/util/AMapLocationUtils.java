package com.yqtms.locationsdk.amap.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.hdgq.locationlib.listener.OnGetLocationListener;
import com.hdgq.locationlib.listener.OnGetLocationResultListener;
import com.yqtms.locationsdk.amap.listener.AMapOnGetLocationListener;

/**需要申请android.permission.ACCESS_FINE_LOCATION权限**/
public class AMapLocationUtils {

    @SuppressLint({"StaticFieldLeak"})
    private static Context mContext;
    private static int MSG_CHECK_TIMEOUT = 1;
    private static boolean mHaveInitialized = false;
    private static OnGetLocationListener mOnGetLocationListener = null;
    private static long mTimeOut;
    private static boolean mForceUpdate;
    @SuppressLint({"StaticFieldLeak"})
    public static AMapLocationClient mLocationClient = null;
    public static AMapLocationClientOption mLocationOption = null;
    public static AMapLocationUtils.MyGaoDeLocationListener myGaoDeLocationListener = new AMapLocationUtils.MyGaoDeLocationListener();
    public static AMapLocation mAMapLocation = null;
    private static Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            if (msg.what == AMapLocationUtils.MSG_CHECK_TIMEOUT) {
                if (AMapLocationUtils.mAMapLocation == null && AMapLocationUtils.mOnGetLocationListener != null) {
                    AMapLocationUtils.mOnGetLocationListener.onGetLocationTimeOut();
                }

                if (AMapLocationUtils.mLocationClient.isStarted()) {
                    AMapLocationUtils.mLocationClient.stopLocation();
                }
            }

        }
    };

    public AMapLocationUtils() {
    }

    public static void init(Context context, OnGetLocationListener listener) {
        if (mLocationClient != null) {
            if (mLocationClient.isStarted()) {
                mLocationClient.stopLocation();
            }
            mLocationClient.onDestroy();
            mLocationClient = null;
        }

        mContext = context;
        mOnGetLocationListener = listener;
        mLocationClient = new AMapLocationClient(context.getApplicationContext());
        mLocationClient.setLocationListener(myGaoDeLocationListener);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setMockEnable(false);
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
        mLocationOption.setGpsFirst(true);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.stopLocation();
        mLocationClient.startLocation();

        mHaveInitialized = true;
    }

    private static void getLocation(long timeOut, boolean forceUpdate) {
        mTimeOut = timeOut;
        mForceUpdate = forceUpdate;
//        if (AMapPermissionUtils.checkPermission((Activity) mContext, 5)) {
            getLocation();
//        } else {
//            Toast.makeText(AMapLocationUtils.mContext, "获取定位信息失败，无定位授权", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            AMapPermissionUtils.requestPermission((Activity)mContext, 5, mPermissionGrant);
//        }

    }

    public static void getLocation(boolean forceUpdate, OnGetLocationResultListener listener) {
        if (!mHaveInitialized) {
            listener.onFailure("888888", "定位失败，请先初始化SDK");
        } else {
            AMapOnGetLocationListener.setOnResultListener(listener);
            getLocation(-1L, forceUpdate);
        }
    }

    private static void getLocation() {
        if ((mForceUpdate || mAMapLocation == null) && mOnGetLocationListener != null) {
            mOnGetLocationListener.onGetLocationStart();
        }

        if (!mForceUpdate && mAMapLocation != null && mOnGetLocationListener != null) {
            mOnGetLocationListener.onReceiveLocation(mAMapLocation);
        }

        mLocationClient.startLocation();
        if (mTimeOut != -1L) {
            mHandler.sendEmptyMessageDelayed(MSG_CHECK_TIMEOUT, mTimeOut);
        }

    }

    private static void stopLocation() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stopLocation();
        }

    }

    private static class MyGaoDeLocationListener implements AMapLocationListener {
        private MyGaoDeLocationListener() {
        }

        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                AMapLocationUtils.mAMapLocation = aMapLocation;
                if (AMapLocationUtils.mOnGetLocationListener != null) {
                    AMapLocationUtils.mOnGetLocationListener.onReceiveLocation(AMapLocationUtils.mAMapLocation);
                }

                AMapLocationUtils.stopLocation();
            }
        }
    }
}
