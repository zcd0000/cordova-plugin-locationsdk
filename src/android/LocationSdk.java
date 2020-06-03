package com.yqtms.cordova.plugin.location;

import com.google.gson.Gson;
import com.hdgq.locationlib.LocationOpenApi;
import com.hdgq.locationlib.entity.ShippingNoteInfo;
import com.hdgq.locationlib.listener.OnResultListener;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONObject;

import android.content.Context;
import android.os.Build;


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
                    buildListener(callbackContext)
            );
            return true;
        } else if (LOCATION_SDK_START.equals(action)) { // Send start location and ticket info
            return locationSdkOperations(true, args, callbackContext);
        } else if (LOCATION_SDK_STOP.equals(action)) { // Send end location and ticket info
            return locationSdkOperations(false, args, callbackContext);
        }
        callbackContext.error("Invalid function is invoked!");
        return false;
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

}
