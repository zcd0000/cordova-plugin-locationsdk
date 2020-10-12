package com.yqtms.locationsdk.amap;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.hdgq.locationlib.LocationOpenApi;
import com.hdgq.locationlib.entity.SendLocationInfo;
import com.hdgq.locationlib.entity.ShippingNoteInfo;
import com.hdgq.locationlib.http.ApiPathManager;
import com.hdgq.locationlib.listener.OnResultListener;
import com.hdgq.locationlib.util.AlarmManagerUtils;
import com.hdgq.locationlib.util.MockLocationUtils;
import com.hdgq.locationlib.util.RootUtil;
import com.yqtms.locationsdk.amap.constant.AMapErrorCode;
import com.yqtms.locationsdk.amap.constant.Constants;
import com.yqtms.locationsdk.amap.entity.AMapSessionInfo;
import com.yqtms.locationsdk.amap.entity.UpdatePositionOptions;
import com.yqtms.locationsdk.amap.listener.AMapOnGetLocationListener;
import com.yqtms.locationsdk.amap.util.AMapAlarmManagerUtils;
import com.yqtms.locationsdk.amap.util.AMapLocationUtils;

import java.util.List;

public class AMapLocationOpenApi {

    public static void init(Context context) {

        AMapLocationUtils.init(context, AMapOnGetLocationListener.getInstance());
        if (!AMapAlarmManagerUtils.mIsStart) {
            AMapAlarmManagerUtils.startAlarm(context.getApplicationContext());
        }

    }
}
