package com.yqtms.locationsdk.amap.entity;

public class AMapRouteInfo {
    private long distance;
    private int duration;
    private String path;
    private AMapAddress loadAddress;
    private AMapAddress unloadAddress;

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public AMapAddress getLoadAddress() {
        return loadAddress;
    }

    public void setLoadAddress(AMapAddress loadAddress) {
        this.loadAddress = loadAddress;
    }

    public AMapAddress getUnloadAddress() {
        return unloadAddress;
    }

    public void setUnloadAddress(AMapAddress unloadAddress) {
        this.unloadAddress = unloadAddress;
    }

}
