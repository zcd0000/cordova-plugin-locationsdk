package com.yqtms.locationsdk.amap.constant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface AMapErrorCode {
    String SHIPPING_NOTE_START_EMPTY = "100007";
    String SHIPPING_NOTE_END_EMPTY = "100008";
    String LOCATION_TIME_OUT = "110000";
    String LOCATION_FAILS = "110001";
    String NETWORK_ERROR = "999999";
    String NOT_INIT = "888888";
}
