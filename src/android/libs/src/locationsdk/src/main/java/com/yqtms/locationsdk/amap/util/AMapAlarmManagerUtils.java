package com.yqtms.locationsdk.amap.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hdgq.locationlib.entity.ShippingNoteInfo;
import com.hdgq.locationlib.util.SharedPreferencesUtils;
import com.yqtms.locationsdk.amap.constant.Constants;
import com.yqtms.locationsdk.amap.entity.UpdatePositionOptions;
import com.yqtms.locationsdk.amap.receive.AMapAlarmReceive;

import java.util.ArrayList;

public class AMapAlarmManagerUtils {
    private static AlarmManager mAlarmManager;
    public static boolean mIsStart;

    public AMapAlarmManagerUtils() {
    }

    private static void initAlarmManager(Context context) {
        if (mAlarmManager == null) {
            mAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        }

    }

    public static void startAlarm(Context context) {
        mIsStart = true;
        initAlarmManager(context);

        /** 获取传递的周期性获取定位信息的时间 **/
        UpdatePositionOptions options = Constants.getUpdatePositionOptions(context);
        long getLocationInterval = Constants.GET_LOCATION_INTERVAL;
        if (options != null && options.getInterval() > 0) {
            getLocationInterval = options.getInterval() * 1000;
        }

        long triggerAtTime = SystemClock.elapsedRealtime() + getLocationInterval;
        Intent i = new Intent(context, AMapAlarmReceive.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
        if (Build.VERSION.SDK_INT >= 23) {
            mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 19) {
            mAlarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        } else {
            mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        }

    }
}
