package com.yqtms.locationsdk.amap.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.hdgq.locationlib.http.callback.JsonCallBack;
import com.hdgq.locationlib.http.model.ServerResponse;
import com.hdgq.locationlib.listener.OnGetLocationResultListener;
import com.hdgq.locationlib.service.AlarmService;
import com.lzy.okgo.model.Response;
import com.yqtms.locationsdk.amap.constant.Constants;
//import com.hdgq.locationlib.entity.SendLocationInfo;
import com.hdgq.locationlib.util.SharedPreferencesUtils;
import com.yqtms.locationsdk.amap.entity.AMapLocationInfo;
import com.yqtms.locationsdk.amap.entity.AMapSessionInfo;
import com.yqtms.locationsdk.amap.entity.UpdatePositionOptions;
import com.yqtms.locationsdk.amap.http.AMapHttpManager;
import com.yqtms.locationsdk.amap.receive.AMapAlarmReceive;
import com.yqtms.locationsdk.amap.util.AMapLocationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AMapAlarmService extends IntentService {

    private static AtomicInteger countAtom = new AtomicInteger(0);
    private static AMapSessionInfo sessionInfo = null;
    private static UpdatePositionOptions options = null;
    //上一次有效的经纬度
    private static double lastLat = 0;
    private static double lastLng = 0;


    public AMapAlarmService(){
        super("AMapAlarmService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("myAMapIntentService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        sessionInfo = Constants.getSessionInfo(this);
        if (sessionInfo == null) {
            // 如果没有登陆用户信息那么就不去获取地理位置
            return;
        }

        AMapLocationUtils.getLocation(true,new OnGetLocationResultListener() {

            @Override
            public void onGetLocationSuccess(AMapLocation aMapLocation) {
                // Test 展示获取到的经纬度，这样我们知道，经纬度获取成功或者失败
//                Toast.makeText(AMapAlarmService.this.getApplicationContext(), aMapLocation.getLongitude() + "||" + aMapLocation.getLatitude(), Toast.LENGTH_LONG).show();
                AMapAlarmService.this.sendLocationInfo(aMapLocation,"");

            }

            @Override
            public void onFailure(String s, String s1) {

            }

        });

    }

    private void sendLocationInfo(final AMapLocation aMapLocation, final String errorMsg) {
        /** 获取传递的周期性获取定位信息的时间 **/
        long sendLocationTimes;
        options = Constants.getUpdatePositionOptions(this);
        if(options == null || TextUtils.isEmpty(options.getRemoteUrl())){
            // 此时应该上报异常，没有参数的话无法发送定位信息了
            return;
        }
        sendLocationTimes = options.getPostIntervalTimes();

        final long locationTime = System.currentTimeMillis();
        boolean shouldAccept = acceptLocationInfo(aMapLocation);

        int count = countAtom.get();
        if(shouldAccept) {
            count = countAtom.incrementAndGet();
            saveLocationInfo(aMapLocation,locationTime,"",sessionInfo);
        }

        if(count < sendLocationTimes) {
            // 如果未存储到指定数量的数据就暂时存起来，存到一定数量再发送（另外发送失败时也会存储起来，等待下个周期，如果一定发送失败，存储了太多的数据那怎么办呢，比如网络断了）
            return;
        }

        AMapHttpManager.sendLocationInfo(this, options.getRemoteUrl(), sessionInfo,"", new JsonCallBack() {
            @Override
            public void onSuccess(Response<ServerResponse> response) {
                // 满足某些特定条件的话也可以删除派单的定位信息上传
                if (response.body() != null) {
                    ServerResponse serverResponse = (ServerResponse)response.body();
                    if (serverResponse.code == 0 || serverResponse.data != null && !TextUtils.isEmpty(serverResponse.data.toJSONString())) {
                        Constants.clearLocationInfos(AMapAlarmService.this);
                        countAtom.set(0);

                    }
                }

            }

            @Override
            public void onError(Response<ServerResponse> response) {
                super.onError(response);

            }
        });

    }

    private void saveLocationInfo(AMapLocation aMapLocation, long locationTime, String remark, AMapSessionInfo sessionInfo) {
        if(aMapLocation == null || aMapLocation.getLongitude() <= 0 || aMapLocation.getLatitude() <=0){
            return;
        }

        AMapLocationInfo locationInfo = new AMapLocationInfo();
        locationInfo.setSessionInfo(sessionInfo);
        locationInfo.setLongitude(aMapLocation.getLongitude());
        locationInfo.setLatitude(aMapLocation.getLatitude());
        locationInfo.setUpdateTimeStamp(locationTime);
        locationInfo.setRemark(remark);
        locationInfo.setAccuracy(aMapLocation.getAccuracy());
        locationInfo.setBearing(aMapLocation.getBearing());
        locationInfo.setSpeed(aMapLocation.getSpeed());
        List<AMapLocationInfo> list = new ArrayList();
        list.add(locationInfo);

        ArrayList<AMapLocationInfo> locationInfos = Constants.getLocationInfos(this);
        list.addAll(locationInfos);

        Constants.saveLocationInfos(this,list);

    }

    // 判定此次获取的定位信息是否应该抛弃，比如跟上次获取的定位距离过短，精度较差，true就是当成有效定位，false就是无效定位
    private boolean acceptLocationInfo(AMapLocation aMapLocation){

        double curLat = aMapLocation.getLatitude();
        double curLng = aMapLocation.getLongitude();

        float accuracy = aMapLocation.getAccuracy();
//        //精度大于200的坐标抛弃
        if (accuracy > 200) {

            return false;
        }

        if(curLat <= 0.0 || curLng <= 0.0){
            return false;
        }

        //2次连续坐标点相同 抛弃
        if (lastLat == curLat || lastLng == curLng) {
            return false;
        }

        int minDistance = 10;
        if(options != null && options.getMinDistance() > 0){
            minDistance = options.getMinDistance();
        }

        if (lastLat > 0 || lastLng > 0) {
            //计算与上一个有效点之间的距离
            float[] results=new float[1];
            Location.distanceBetween(lastLat,lastLng,curLat, curLng,results);

            if (results[0] < minDistance) {
                return false;
            }
        }

        lastLat = curLat;
        lastLng = curLng;
        return true;
    }



}
