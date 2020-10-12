package com.yqtms.locationsdk.amap;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.hdgq.locationlib.keeplive.KeepLive;
import com.hdgq.locationlib.keeplive.config.ForegroundNotification;
import com.hdgq.locationlib.keeplive.config.ForegroundNotificationClickListener;
import com.hdgq.locationlib.keeplive.config.KeepLiveService;
import com.yqtms.locationsdk.R;

public class LocationApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ForegroundNotification foregroundNotification = new ForegroundNotification("物流运输", "物流运输服务", R.mipmap.ic_launcher, new ForegroundNotificationClickListener() {
            @Override
            public void foregroundNotificationClick(Context context, Intent intent) {
                // 无需做任何事情；一个JobService会定时出发此方法的点击事件只为保活
            }
        });

        KeepLive.startWork(LocationApplication.this, KeepLive.RunMode.ENERGY, foregroundNotification, new KeepLiveService() {
            @Override
            public void onWorking() {
                Toast.makeText(LocationApplication.this,"保活服务执行",Toast.LENGTH_LONG);
                AMapLocationOpenApi.init(LocationApplication.this);
            }

            @Override
            public void onStop() {
                Toast.makeText(LocationApplication.this,"保活服务停止",Toast.LENGTH_LONG);
            }
        });
    }
}
