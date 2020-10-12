package com.yqtms.locationsdk.amap.entity;

public class UpdatePositionOptions {
    private int interval;
    private boolean gpsFirst;
    private int minDistance;
    private int postIntervalTimes;
    private String remoteUrl;
    private String forgroundTitle;
    private String forgroundDescriptionn;

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
    public boolean getGpsFirst(){
        return gpsFirst;
    }

    public void setGpsFirst(boolean gpsFirst) {
        this.gpsFirst = gpsFirst;
    }

    public int getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }

    public int getPostIntervalTimes() {
        return postIntervalTimes;
    }

    public void setPostIntervalTimes(int postIntervalTimes) {
        this.postIntervalTimes = postIntervalTimes;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getForgroundTitle() {
        return forgroundTitle;
    }

    public void setForgroundTitle(String forgroundTitle) {
        this.forgroundTitle = forgroundTitle;
    }

    public String getForgroundDescriptionn() {
        return forgroundDescriptionn;
    }

    public void setForgroundDescriptionn(String forgroundDescriptionn) {
        this.forgroundDescriptionn = forgroundDescriptionn;
    }



}
