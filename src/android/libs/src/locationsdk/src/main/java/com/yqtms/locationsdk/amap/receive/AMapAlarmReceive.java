package com.yqtms.locationsdk.amap.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.hdgq.locationlib.http.ApiPathManager;
import com.hdgq.locationlib.util.AlarmManagerUtils;
import com.yqtms.locationsdk.amap.service.AMapAlarmService;
import com.yqtms.locationsdk.amap.util.AMapAlarmManagerUtils;


public class AMapAlarmReceive extends BroadcastReceiver {

    public AMapAlarmReceive(){
    }

    /***/
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AMapAlarmReceive", "执行AMapAlarmReceive");

        AMapAlarmManagerUtils.startAlarm(context);
        Intent i = new Intent(context, AMapAlarmService.class);
        context.startService(i);
    }
}
