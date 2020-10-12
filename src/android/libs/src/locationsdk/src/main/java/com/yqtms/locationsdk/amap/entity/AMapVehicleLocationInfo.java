package com.yqtms.locationsdk.amap.entity;

import java.util.List;

public class AMapVehicleLocationInfo {
    private boolean arrived;
    private AMapAddress loadAddress;
    private AMapAddress unloadAddress;
    private List<AMapLocationPoint> points;

    public boolean getArrived() {
        return arrived;
    }

    public void setArrived(boolean arrived) {
        this.arrived = arrived;
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

    public List<AMapLocationPoint> getPoints() {
        return points;
    }

    public void setPoints(List<AMapLocationPoint> points) {
        this.points = points;
    }
}
