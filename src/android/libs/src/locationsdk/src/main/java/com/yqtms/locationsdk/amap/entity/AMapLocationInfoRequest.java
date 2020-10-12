package com.yqtms.locationsdk.amap.entity;

import java.util.List;

public class AMapLocationInfoRequest {
    private List<AMapLocationInfo> points;

    public List<AMapLocationInfo> getPoints() {
        return points;
    }

    public void setPoints(List<AMapLocationInfo> points) {
        this.points = points;
    }
}
