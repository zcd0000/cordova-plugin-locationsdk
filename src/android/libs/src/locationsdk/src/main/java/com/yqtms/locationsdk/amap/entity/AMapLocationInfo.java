package com.yqtms.locationsdk.amap.entity;


import java.text.SimpleDateFormat;
import java.util.List;

public class AMapLocationInfo {
    private double longitude;
    private double latitude;
    private long updateTimeStamp;
    private String remark;
    private float speed;
    private float bearing;
    private float accuracy;
    private AMapSessionInfo sessionInfo;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    public void setUpdateTimeStamp(long updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public AMapSessionInfo getSessionInfo() {
        return sessionInfo;
    }

    public void setSessionInfo(AMapSessionInfo sessionInfo) {
        this.sessionInfo = sessionInfo;
    }
}
