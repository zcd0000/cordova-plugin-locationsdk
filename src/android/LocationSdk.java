package com.yqtms.cordova.plugin.location;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.hdgq.locationlib.LocationOpenApi;
import com.hdgq.locationlib.entity.ShippingNoteInfo;
import com.hdgq.locationlib.keeplive.KeepLive;
import com.hdgq.locationlib.keeplive.config.ForegroundNotification;
import com.hdgq.locationlib.keeplive.config.ForegroundNotificationClickListener;
import com.hdgq.locationlib.keeplive.config.KeepLiveService;
import com.hdgq.locationlib.listener.OnResultListener;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONObject;

import android.content.Context;

import android.content.pm.PackageInstaller;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.amap.api.maps.MapView;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.location.LocationManager;
import android.app.Activity;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.yqtms.locationsdk.amap.AMapLocationOpenApi;
import com.yqtms.locationsdk.amap.activity.MapActivity;
import com.yqtms.locationsdk.amap.activity.TraceActivity;
import com.yqtms.locationsdk.amap.constant.Constants;
import com.yqtms.locationsdk.amap.entity.AMapSessionInfo;
import com.yqtms.locationsdk.amap.entity.UpdatePositionOptions;

public class LocationSdk extends CordovaPlugin {

    private static final String TAG = "LocationSdkPlugin";
    private static final String LOCATION_SDK_INIT = "init";
    private static final String LOCATION_SDK_START = "start";
    private static final String LOCATION_SDK_STOP = "stop";
    private static final String WLHY_APP_SECURITY = "wlhyAppSecurity";
    private static final String ENTERPRISE_SENDER_CODE = "enterpriseSenderCode";
    private static final String BUILD_TYPE_DEBUG = "debug";
    private static final String BUILD_TYPE_RELEASE = "release";

    private static Context mContext;
    private static String mPackageName;
    private CordovaWebView mMainWebView;
    private CordovaPreferences mPref;

    private static final String START_LOCATION_ACTION = "startUpdatePosition";
    private static final String STOP_LOCATION_ACTION  = "stopUpdatePosition";

    private static final String SHOW_MAP_ACTION       = "showMap";
    private static final String TRACE_MAP_ACTION      = "traceMap";

    private CallbackContext cCtx;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        // Get the current activity
        mContext = cordova.getActivity();
        // Get the Java Package Name, important: not the applicationId or package in AndroidManifest.xml
        mPackageName = mContext.getClass().getPackage().getName();
        mMainWebView = webView;
        mPref = mMainWebView.getPreferences();

    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        cCtx = callbackContext;
        Log.d(TAG, " action code:" + action);
        // As sdk is initializing
        if (LOCATION_SDK_INIT.equals(action)) {
            String appSecurity = mPref.getString(WLHY_APP_SECURITY, null);
            String enterpriseSenderCode = mPref.getString(ENTERPRISE_SENDER_CODE, null);
            String environment = BuildConfig.BUILD_TYPE;
            LOG.d(TAG, String.format("appSecurity:%s, enterpriseSenderCode:%s. environment: %s", appSecurity, enterpriseSenderCode, enterpriseSenderCode));
            if (appSecurity.isEmpty()) {
                String errMsg = "Invalid appSecurity: null";
                LOG.e(TAG, errMsg);
                callbackContext.error(errMsg);
                return false;
            }
            if (enterpriseSenderCode.isEmpty()) {
                String errMsg = "Invalid enterpriseSenderCode: null";
                LOG.e(TAG, errMsg);
                callbackContext.error(errMsg);
                return false;
            }
            // Replace leader code: "ESC:" to avoid to get invalid enterpriseSenderCode while code number length exceeding 10
            enterpriseSenderCode = enterpriseSenderCode.substring(4);
            if (!environment.equals(BUILD_TYPE_DEBUG) && !environment.equals(BUILD_TYPE_RELEASE)) {
                String errMsg = "Invalid environment: " + environment;
                LOG.e(TAG, errMsg);
                callbackContext.error(errMsg);
                return false;
            }
            LocationOpenApi.init(
                    mContext,
                    mPackageName,
                    appSecurity,
                    enterpriseSenderCode,
                    environment,
                    new OnResultListener() {
                        @Override
                        public void onSuccess() {
                            initUpdatePosition(args,callbackContext);
                        }

                        @Override
                        public void onFailure(String code, String errMsg) {
                            callbackContext.error(String.format("%s(%s)!", errMsg, code));
                        }
                    });

            return true;
        } else if (LOCATION_SDK_START.equals(action)) { // Send start location and ticket info
            return locationSdkOperations(true, args, callbackContext);
        } else if (LOCATION_SDK_STOP.equals(action)) { // Send end location and ticket info
            return locationSdkOperations(false, args, callbackContext);
        } else if(START_LOCATION_ACTION.equals(action)) {

            return initUpdatePosition(args,callbackContext);

        } else if(STOP_LOCATION_ACTION.equals(action)) {
            Constants.clearSessionInfo(mContext);

            callbackContext.success();
            return true;
        } else if (SHOW_MAP_ACTION.equals(action)) {
            try {
                String vehicleLocationInfo = (String)args.getString(0);
                String title = (String)args.getString(1);

                //下面两句最关键，利用intent启动新的Activity
                Intent intent = new Intent(cordova.getActivity(), MapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("vehicleLocationInfo", vehicleLocationInfo);
                bundle.putString("title", title);
                intent.putExtras(bundle);
                this.cordova.startActivityForResult(this, intent, 200);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else if(TRACE_MAP_ACTION.equals(action)) {
            String routeInfo = (String)args.getString(0);
            String title       = (String)args.getString(1);
            try {
                //下面两句最关键，利用intent启动新的Activity
                Intent intent = new Intent(cordova.getActivity(), TraceActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("routeInfo", routeInfo);
                bundle.putString("title", title);
                intent.putExtras(bundle);
                this.cordova.startActivityForResult(this, intent, 200);

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR, PluginResult.Status.INVALID_ACTION.toString());
        pluginResult.setKeepCallback(false);
        callbackContext.sendPluginResult(pluginResult);
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){

        super.onActivityResult(requestCode, resultCode, intent);

        Log.d(TAG, " request code" + requestCode);
        Log.d(TAG, " result code" + resultCode);

        if(resultCode==Activity.RESULT_OK){
            cCtx.success("success");
        } else {
            cCtx.error("fail");
        }
    }

    private OnResultListener buildListener(CallbackContext callbackContext) {
        return new OnResultListener() {
            @Override
            public void onSuccess() {
                callbackContext.success();
            }

            @Override
            public void onFailure(String code, String errMsg) {
                callbackContext.error(String.format("%s(%s)!", errMsg, code));
            }
        };
    }

    private Boolean locationSdkOperations(Boolean isStart, JSONArray args, CallbackContext callbackContext) {
        try {
            String noteInfosJson = args.getString(0);
            Gson gson = new Gson();
            ShippingNoteInfo[] noteInfoList = gson.fromJson(noteInfosJson, ShippingNoteInfo[].class);
            if (noteInfoList.equals(null) || noteInfoList.length == 0) {
                callbackContext.error("Invaid ShippingNoteInfo[] parameters!");
                return false;
            }
            if (isStart) {
                LocationOpenApi.start(mContext, noteInfoList, buildListener(callbackContext));
            } else {
                LocationOpenApi.stop(mContext, noteInfoList, buildListener(callbackContext));
            }
            return true;
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
        return false;
    }

    private Boolean initUpdatePosition(JSONArray args, CallbackContext callbackContext) {
        try
        {
            String sessionInfoStr = args.getString(0);
            String optionsStr = args.getString(1);
            AMapSessionInfo sessionInfo = JSON.parseObject(sessionInfoStr,AMapSessionInfo.class);
            UpdatePositionOptions options = JSON.parseObject(optionsStr, UpdatePositionOptions.class);
            // just for test
            options.setInterval(10);
            options.setMinDistance(1);

            Constants.saveSessionInfo(mContext,sessionInfo);
            Constants.saveUpdatePositionOptions(mContext,options);

            callbackContext.success();
            return true;
        } catch (Exception e){

            callbackContext.error(e.getMessage());
            return false;
        }
    }

}
