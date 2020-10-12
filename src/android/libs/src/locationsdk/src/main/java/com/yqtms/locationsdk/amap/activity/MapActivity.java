package com.yqtms.locationsdk.amap.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.hdgq.locationlib.listener.OnGetLocationResultListener;
import com.yqtms.locationsdk.R;
import com.yqtms.locationsdk.amap.entity.AMapLocationPoint;
import com.yqtms.locationsdk.amap.entity.AMapVehicleLocationInfo;
import com.yqtms.locationsdk.amap.util.AMapLocationUtils;
import com.yqtms.locationsdk.amap.util.AMapUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapActivity extends Activity implements LocationSource,
        AMapLocationListener, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter{

    private AMap                      aMap;
    private MapView                   mMapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient        mlocationClient;
    private AMapLocationClientOption  mLocationOption;

    private AMapVehicleLocationInfo vehicleLocationInfo = null;
    private String title = null;

//    private PolylineOptions plineOptions;
    private TextView mTvTitilName;
    private TextView mTvTitilBack;
    private String backColor;
    private RelativeLayout mRlTitil;
    private TextView timeTxtView;
    private TextView speedTxtView;
    private TextView addressTxtView;

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            setUpMap();
        }

        mTvTitilBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
                /**测试发短信**/
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setResult(RESULT_OK);
        this.finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        mTvTitilName = (TextView) findViewById(R.id.title_name);
        mTvTitilBack = (TextView) findViewById(R.id.title_back);
        mRlTitil = (RelativeLayout) findViewById(R.id.title_rl);
        timeTxtView = (TextView) findViewById(R.id.updateTime);
        speedTxtView = (TextView) findViewById(R.id.speed);

        Bundle bundle = this.getIntent().getExtras();
        vehicleLocationInfo = JSON.parseObject(bundle.getString("vehicleLocationInfo"),AMapVehicleLocationInfo.class);
        title = vehicleLocationInfo.getLoadAddress().getName() + '-' + vehicleLocationInfo.getUnloadAddress().getName();
        if (title != null ) {
            setTitle(title);
            mTvTitilName.setText(title);
        }
        backColor = "#000000";
        mRlTitil.setBackgroundColor(Color.parseColor(backColor));
        mTvTitilBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);

         init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.layout.menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        //缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));// 设置小蓝点的图标

//        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
//        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
//         myLocationStyle.anchor(int,int)//设置小蓝点的锚点
//        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细

        aMap.setMyLocationStyle(myLocationStyle);
        // 如果处于送货状态就显示当前坐标
//        if(vehicleLocationInfo!=null && !vehicleLocationInfo.getArrived()){
//            aMap.setLocationSource(this);// 设置定位监听
//            aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
//            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//        }

        aMap.setInfoWindowAdapter(this);
        aMap.setOnMarkerClickListener(this);

        addMarkersToMap();// 往地图上添加marker
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
            //设定周期为1分钟定位一次
//            mLocationOption.setInterval(1000);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent myIntent = new Intent(MapActivity.this, MainActivity.class);
//            startActivity(myIntent);
            setResult(RESULT_OK);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 在地图上添加marker(之所以写了这么多其实是因为我们的装货地和卸货地可能没有坐标信息，插入Marker等一些操作就无效了)
     */
    private void addMarkersToMap() {
        LatLng firstPoint = new LatLng(vehicleLocationInfo.getLoadAddress().getLatitude(),vehicleLocationInfo.getLoadAddress().getLongitude());
        LatLng lastPoint = new LatLng(vehicleLocationInfo.getUnloadAddress().getLatitude(),vehicleLocationInfo.getUnloadAddress().getLongitude());

        List<AMapLocationPoint> points = vehicleLocationInfo.getPoints();
        List<LatLng> allPoints = new ArrayList<>();

        allPoints.add(firstPoint);
        for (AMapLocationPoint point: points) {
            LatLng p = new LatLng(point.getLatitude(),point.getLongitude());
            allPoints.add(p);
            // 为实际获取到的每个点添加一个Marker标注改点的数独之类的信息（一个显示"经"的pin）
            insertMiddleMarker(p,point);
        }

        PolylineOptions plineOptions = new PolylineOptions();
        for(LatLng point: allPoints){
            plineOptions.add(point);
        }
        adjustLineStyle(plineOptions);

        // 添加轨迹线路
        aMap.addPolyline(plineOptions);

        // 让全部轨迹点在地图上可见
        allPoints.add(lastPoint);
        moveCamera(allPoints);
        // 插入装卸货点
        insertLoadUnloadMarker(firstPoint,lastPoint);
        // 设置当前时间以及时速
        initCurrentTimeAndSpeed();
    }

    // 调整地图显示位置，让全部坐标点显示出来
    private void moveCamera(List<LatLng> points){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i< points.size(); i++){
            builder.include(points.get(i));
        }
        LatLngBounds bounds = builder.build();
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,50));
    }


    private void insertLoadUnloadMarker(LatLng firstPoint,LatLng lastPoint) {

        MarkerOptions loadMarkerOptions = new MarkerOptions();
        loadMarkerOptions.icon(BitmapDescriptorFactory.fromView(AMapUtils.getImageViewForMarker(MapActivity.this,AMapUtils.MarkerIcon.Start)))
                .anchor(0.5f, 0.8f)
                .position(firstPoint);

        aMap.addMarker(loadMarkerOptions);

        MarkerOptions unloadMarkerOptions = new MarkerOptions();
        unloadMarkerOptions.icon(BitmapDescriptorFactory.fromView(AMapUtils.getImageViewForMarker(MapActivity.this,AMapUtils.MarkerIcon.End)))
                .anchor(0.5f, 0.8f)
                .position(lastPoint);

        aMap.addMarker(unloadMarkerOptions);
    }

    private void insertMiddleMarker(LatLng middlePoint,AMapLocationPoint point){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromView(AMapUtils.getImageViewForMarker(MapActivity.this,AMapUtils.MarkerIcon.Middle)))
                .anchor(0.5f, 0.8f)
                .position(middlePoint)
                .snippet(JSON.toJSONString(point));

        aMap.addMarker(markerOptions);
    }

    // 修改轨迹线的纹理样式
    private void adjustLineStyle(PolylineOptions plineOptions){
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        BitmapDescriptor mRedTexture = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_road_blue_arrow);
        BitmapDescriptor mBlueTexture = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_road_green_arrow);
        textureList.add(mRedTexture);
        textureList.add(mBlueTexture);
        // 添加纹理图片对应的顺序
        List<Integer> textureIndexs = new ArrayList<Integer>();
        textureIndexs.add(0);
        textureIndexs.add(1);
        plineOptions.setCustomTextureList(textureList);
        plineOptions.setCustomTextureIndex(textureIndexs);
        plineOptions.setUseTexture(true);
        plineOptions.width(16.0f);
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String content = marker.getSnippet();
        AMapLocationPoint point = null;
        if(!TextUtils.isEmpty(content)){
            point = JSON.parseObject(content,AMapLocationPoint.class);
        }
        if(point != null){
            SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
            String dateString = format.format(point.getUpdateTime());
            timeTxtView.setText(dateString);

            DecimalFormat df = new DecimalFormat("#########.#");
            String speedString = df.format(point.getSpeed() * 3.6);
            speedTxtView.setText("该点时速 "+speedString+"km/h");

        }

        return false;
    }

    private void initCurrentTimeAndSpeed(){
        AMapLocationUtils.getLocation(true, new OnGetLocationResultListener() {
            @Override
            public void onGetLocationSuccess(AMapLocation aMapLocation) {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
                String dateString = format.format(date);
                timeTxtView.setText(dateString);

                DecimalFormat df = new DecimalFormat("#########.#");
                String speedString = df.format(aMapLocation.getSpeed() * 3.6);
                speedTxtView.setText("当前时速 "+speedString+"km/h");
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }

}
