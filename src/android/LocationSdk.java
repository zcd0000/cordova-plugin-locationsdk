package com.yqtms.cordova.plugin.location;

import com.hdgq.locationlib.LocationOpenApi;
import com.hdgq.locationlib.entity.ShippingNoteInfo;
import com.hdgq.locationlib.listener.OnResultListener;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.apache.cordova.CordovaPlugin;

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
        LOG.d(TAG, "action code:" + action);
        // As sdk is initializing
        if (LOCATION_SDK_INIT.equals(action)) {
            LOG.d(TAG, "Location is initializing...");
            String appSecurity = mPref.getString(WLHY_APP_SECURITY, null);
            String enterpriseSenderCode = mPref.getString(ENTERPRISE_SENDER_CODE, null);
            String environment = BuildConfig.BUILD_TYPE;
            LOG.d(TAG, String.format("appSecurity:%s, enterpriseSenderCode:%s. environment: %s", appSecurity, enterpriseSenderCode,enterpriseSenderCode));
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
            LOG.d(TAG, "Sending start location and tickets...");
            ShippingNoteInfo noteInfoList[] = (ShippingNoteInfo[]) args.get(0);
            if(noteInfoList.equals(null) || noteInfoList.length == 0) {
                callbackContext.error("Invaid ShippingNoteInfo[] parameters!");
                return false;
            }
            LocationOpenApi.start(mContext, noteInfoList, buildListener(callbackContext));
            return true;
        } else if (LOCATION_SDK_STOP.equals(action)) { // Send end location and ticket info
            LOG.d(TAG, "Sending end location and tickets...");
            ShippingNoteInfo noteInfoList[] = (ShippingNoteInfo[]) args.get(0);
            if(noteInfoList.equals(null) || noteInfoList.length == 0) {
                callbackContext.error("Invaid ShippingNoteInfo[] parameters!");
                return false;
            }
            LocationOpenApi.stop(mContext, noteInfoList, buildListener(callbackContext));
            return true;
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
            public void onFailure(String s, String s1) {
                callbackContext.error(String.format("ErrorCode: %s, ErrorMsg: %s!", s, s1));
            }
        };
    }
}
