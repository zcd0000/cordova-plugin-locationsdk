package com.yqtms.locationsdk.amap.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;


public class AMapPermissionUtils {
    private static final String TAG = AMapPermissionUtils.class.getSimpleName();
    public static final int CODE_MULTI_PERMISSION = 100;
    public static final int CODE_RECORD_AUDIO = 0;
    public static final int CODE_READ_CONTACTS = 1;
    public static final int CODE_READ_PHONE_STATE = 2;
    public static final int CODE_READ_CALENDAR = 3;
    public static final int CODE_CAMERA = 4;
    public static final int CODE_ACCESS_FINE_LOCATION = 5;
    public static final int CODE_BODY_SENSORS = 6;
    public static final int CODE_READ_EXTERNAL_STORAGE = 7;
    public static final int CODE_READ_SMS = 8;
    public static final int CODE_INSTALL_PACKAGES_REQUEST_CODE = 9;
    public static final String PERMISSION_RECORD_AUDIO = "android.permission.RECORD_AUDIO";
    public static final String PERMISSION_READ_CONTACTS = "android.permission.READ_CONTACTS";
    public static final String PERMISSION_READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String PERMISSION_READ_CALENDAR = "android.permission.READ_CALENDAR";
    public static final String PERMISSION_CAMERA = "android.permission.CAMERA";
    public static final String PERMISSION_ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public static final String PERMISSION_BODY_SENSORS = "android.permission.BODY_SENSORS";
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    public static final String PERMISSION_READ_SMS = "android.permission.READ_SMS";
    public static final String PERMISSION_REQUEST_INSTALL_PACKAGES = "android.permission.REQUEST_INSTALL_PACKAGES";
    private static final String[] requestPermissions = new String[]{"android.permission.RECORD_AUDIO", "android.permission.READ_CONTACTS", "android.permission.READ_PHONE_STATE", "android.permission.READ_CALENDAR", "android.permission.CAMERA", "android.permission.ACCESS_FINE_LOCATION", "android.permission.BODY_SENSORS", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_SMS", "android.permission.REQUEST_INSTALL_PACKAGES"};
    public static final int CODE_OVERLAY = 8;

    AMapPermissionUtils() {
    }

    public static boolean checkPermission(Activity activity, int requestCode) {
        if (activity == null) {
            return false;
        } else {
            Log.i(TAG, "requestPermission requestCode:" + requestCode);
            if (requestCode >= 0 && requestCode < requestPermissions.length) {
                String requestPermission = requestPermissions[requestCode];

                int checkSelfPermission;
                try {
                    checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);
                } catch (RuntimeException var5) {
                    Toast.makeText(activity, "please open this permission", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "RuntimeException:" + var5.getMessage());
                    return false;
                }

                if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "ActivityCompat.checkSelfPermission != PackageManager.PERMISSION_GRANTED");
                    Log.d(TAG, "ActivityCompat.requestPermissions");
                    return false;
                } else {
                    Log.d(TAG, "ActivityCompat.checkSelfPermission ==== PackageManager.PERMISSION_GRANTED");
                    return true;
                }
            } else {
                Log.w(TAG, "requestPermission illegal requestCode:" + requestCode);
                return false;
            }
        }
    }

    public static void requestPermission(Activity activity, int requestCode, AMapPermissionUtils.PermissionGrant permissionGrant) {
        if (activity != null) {
            Log.i(TAG, "requestPermission requestCode:" + requestCode);
            if (requestCode >= 0 && requestCode < requestPermissions.length) {
                String requestPermission = requestPermissions[requestCode];

                int checkSelfPermission;
                try {
                    checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);
                } catch (RuntimeException var6) {
                    Toast.makeText(activity, "please open this permission", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "RuntimeException:" + var6.getMessage());
                    return;
                }

                if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "ActivityCompat.checkSelfPermission != PackageManager.PERMISSION_GRANTED");
                    Log.d(TAG, "ActivityCompat.requestPermissions");
                    ActivityCompat.requestPermissions(activity, new String[]{requestPermission}, requestCode);
                } else {
                    Log.d(TAG, "ActivityCompat.checkSelfPermission ==== PackageManager.PERMISSION_GRANTED");
                    if (permissionGrant != null) {
                        permissionGrant.onPermissionGranted(requestCode, "opened:" + requestPermissions[requestCode]);
                    }
                }

            } else {
                Log.w(TAG, "requestPermission illegal requestCode:" + requestCode);
            }
        }
    }


    public interface PermissionGrant {
        void onPermissionGranted(int var1, String var2);

        void onPermissionDeny(int var1, String var2);
    }
}
