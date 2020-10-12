package com.yqtms.locationsdk.amap.constant;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.hdgq.locationlib.util.SharedPreferencesUtils;
import com.yqtms.locationsdk.amap.entity.AMapLocationInfo;
import com.yqtms.locationsdk.amap.entity.AMapSessionInfo;
import com.yqtms.locationsdk.amap.entity.UpdatePositionOptions;
import com.yqtms.locationsdk.amap.service.AMapAlarmService;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static String SHARED_PREFERENCES_FILE_NAME = "Filename_AMap_ShippingNoteInfo";
    public static String SHARED_PREFERENCES_OPTIONS_INFO_KEY = "options_info";
    public static String SHARED_PREFERENCES_SESSION_INFO_KEY = "session_info";
    public static String SHARED_PREFERENCES_LOCATION_INFO_KEY = "location_info";

    public static int GET_LOCATION_INTERVAL = 60 * 1000;
    public static int SEND_LOCATION_TIMES = 5;

    public static UpdatePositionOptions getUpdatePositionOptions(Context context){
        String optionsStr = (String) SharedPreferencesUtils.getParam(context, Constants.SHARED_PREFERENCES_FILE_NAME, Constants.SHARED_PREFERENCES_OPTIONS_INFO_KEY, "");
        UpdatePositionOptions options = null;
        if (!TextUtils.isEmpty(optionsStr)) {
            options = JSON.parseObject(optionsStr, UpdatePositionOptions.class);
        }
        return options;
    }

    public static void saveUpdatePositionOptions(Context context,UpdatePositionOptions options){
        if(options == null){
            return;
        }
        SharedPreferencesUtils.setParam(context, Constants.SHARED_PREFERENCES_FILE_NAME, Constants.SHARED_PREFERENCES_OPTIONS_INFO_KEY, JSON.toJSONString(options));
    }

    public static AMapSessionInfo getSessionInfo (Context context){
        String sessionInfoJson = (String) SharedPreferencesUtils.getParam(context, Constants.SHARED_PREFERENCES_FILE_NAME, Constants.SHARED_PREFERENCES_SESSION_INFO_KEY, "");
        AMapSessionInfo sessionInfo = null;
        if (!TextUtils.isEmpty(sessionInfoJson)) {
            sessionInfo = JSON.parseObject(sessionInfoJson,AMapSessionInfo.class);
        }
        return sessionInfo;
    }

    public static  void saveSessionInfo(Context context,AMapSessionInfo sessionInfo){
        if(sessionInfo==null){
            return;
        }
        SharedPreferencesUtils.setParam(context, Constants.SHARED_PREFERENCES_FILE_NAME, Constants.SHARED_PREFERENCES_SESSION_INFO_KEY, JSON.toJSONString(sessionInfo));
    }

    public static void clearSessionInfo(Context context){
        SharedPreferencesUtils.remove(context, Constants.SHARED_PREFERENCES_FILE_NAME,Constants.SHARED_PREFERENCES_SESSION_INFO_KEY);
    }

    public static ArrayList<AMapLocationInfo> getLocationInfos(Context context){
        String locationJson = (String)SharedPreferencesUtils.getParam(context, Constants.SHARED_PREFERENCES_FILE_NAME, Constants.SHARED_PREFERENCES_LOCATION_INFO_KEY, "");
        ArrayList<AMapLocationInfo> locationList = new ArrayList();
        if (!TextUtils.isEmpty(locationJson)) {
            locationList.addAll(JSON.parseArray(locationJson, AMapLocationInfo.class));
        }
        return locationList;
    }

    public static void saveLocationInfos(Context context, List<AMapLocationInfo> locationInfos){
        if(locationInfos == null){
            return;
        }
        SharedPreferencesUtils.setParam(context, Constants.SHARED_PREFERENCES_FILE_NAME, Constants.SHARED_PREFERENCES_LOCATION_INFO_KEY, JSON.toJSONString(locationInfos));
    }

    public static void clearLocationInfos(Context context){
        SharedPreferencesUtils.remove(context, Constants.SHARED_PREFERENCES_FILE_NAME,Constants.SHARED_PREFERENCES_LOCATION_INFO_KEY);
    }
}
