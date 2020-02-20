package com.yqtms.cordova.plugin.location;

import com.hdgq.locationlib.LocationOpenApi;
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
        // Get the android activity
        mContext = cordova.getActivity();
        // Get the Java Package Name, important: not the applicationId or package in AndroidManifest.xml
        mPackageName = BuildConfig.class.getPackage().toString();
        mMainWebView = webView;
        mPref = mMainWebView.getPreferences();
    }

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        LOG.d(TAG, "action code:" + action);

        // As sdk is initializing
        if (LOCATION_SDK_INIT.equals(action)) {

            LOG.d(TAG, "Location is initializing...");

            String appSecurity = mPref.getString(WLHY_APP_SECURITY, null);
            String enterpriseSenderCode = mPref.getString(ENTERPRISE_SENDER_CODE, null);
            String environment = Build.TYPE;

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
                String errMsg = "Invalid BuildType: " + Build.TYPE;
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
                            callbackContext.success();
                        }

                        @Override
                        public void onFailure(String s, String s1) {
                            callbackContext.error(s1);
                        }
                    }
            );
            return true;
        } else if (LOCATION_SDK_START.equals(action)) { // Send start location and ticket info
            LOG.d(TAG, "Sending start location and tickets...");
            return true;
        } else if (LOCATION_SDK_STOP.equals(action)) { // Send end location and ticket info
            LOG.d(TAG, "Sending end location and tickets...");
            return true;
        }
        callbackContext.error("Invalid function is invoked!");
        return false;
    }

}
