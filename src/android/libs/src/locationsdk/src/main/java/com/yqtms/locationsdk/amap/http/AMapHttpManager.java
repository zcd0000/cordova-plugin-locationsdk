package com.yqtms.locationsdk.amap.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.hdgq.locationlib.bcprov.AESOperator;
import com.hdgq.locationlib.bcprov.SM2Utils;
import com.hdgq.locationlib.bcprov.Util;
import com.hdgq.locationlib.entity.EncryptionResponse;
import com.hdgq.locationlib.entity.LocationInfo;
import com.hdgq.locationlib.entity.ShippingNoteInfo;
import com.hdgq.locationlib.http.callback.JsonCallBack;
import com.hdgq.locationlib.util.SharedPreferencesUtils;
import com.yqtms.locationsdk.amap.entity.AMapLocationInfoRequest;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;
import com.yqtms.locationsdk.amap.constant.Constants;
import com.yqtms.locationsdk.amap.entity.AMapLocationInfo;
import com.yqtms.locationsdk.amap.entity.AMapSessionInfo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AMapHttpManager {
    public AMapHttpManager(){}

    public static void sendLocationInfo(Context context, String url, AMapSessionInfo sessionInfo, String remark, JsonCallBack callback) {

        ((PostRequest)((PostRequest) OkGo.post(url).headers("Authorization","Bearer " + sessionInfo.getToken()).upJson(getSendJson(context, sessionInfo, remark))).retryCount(0)).execute(callback);
    }

    private static JSONObject getSendJson(Context context, AMapSessionInfo sessionInfo, String remark) {
        try {

            List<AMapLocationInfo> list = new ArrayList();

            ArrayList<AMapLocationInfo> locationInfos = Constants.getLocationInfos(context);
            list.addAll(locationInfos);

            AMapLocationInfoRequest locationInfoRequest = new AMapLocationInfoRequest();
            locationInfoRequest.setPoints(list);

            JSONObject jsonObject = new JSONObject(JSON.toJSON(locationInfoRequest).toString());
            Log.e("jsonObject", jsonObject.toString());
            return jsonObject;
        } catch (Exception var24) {
            var24.printStackTrace();
            return new JSONObject();
        }
    }
}
