package com.yqtms.locationsdk.amap.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.Text;
import com.yqtms.locationsdk.R;
import com.yqtms.locationsdk.amap.entity.AMapRouteInfo;
import com.yqtms.locationsdk.amap.util.AMapUtils;

import java.util.ArrayList;
import java.util.List;


public class TraceActivity extends Activity {

    private AMap                      aMap;
    private MapView                   mMapView;

    private AMapRouteInfo routeInfo = null;
    private String title = null;

    private Marker marker2;// 有跳动效果的marker对象
    private LatLng latlng = new LatLng(36.061, 103.834);
    private TextView mTvTitilName;
    private TextView mTvTitilBack;
    private String backColor;
    private RelativeLayout mRlTitil;
    private TextView mDistance;


    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            setUpMap();
        }


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
        setContentView(R.layout.trace_activity);
        mTvTitilName = (TextView) findViewById(R.id.title_name);
        mTvTitilBack = (TextView) findViewById(R.id.title_back);
        mRlTitil = (RelativeLayout) findViewById(R.id.title_rl);
        mDistance = (TextView) findViewById(R.id.distance);

        Bundle bundle = this.getIntent().getExtras();
        routeInfo = JSON.parseObject(bundle.getString("routeInfo"), AMapRouteInfo.class);
        title = routeInfo.getLoadAddress().getName() + '-' + routeInfo.getUnloadAddress().getName();
        backColor = bundle.getString("backColor");
        if (title != null ) {
            setTitle(title);
            mTvTitilName.setText(title);
        }
        backColor = backColor==null?"#000000":backColor;
        mRlTitil.setBackgroundColor(Color.parseColor(backColor));
        mDistance.setText((routeInfo.getDistance() / 1000) + " 公里") ;

        if (title != null ) {
            setTitle(title);
        }
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
//        aMap.setLocationSource(this);// 设置定位监听
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
//        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        addPolylineToMap();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent myIntent = new Intent();
//            myIntent = new Intent(TraceActivity.this, MainActivity.class);
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
     * 在地图上添加折现路径
     */
    private void addPolylineToMap() {

        String[] coArr = routeInfo.getPath().split(";");

        LatLng firstPoint = null;
        if (routeInfo.getLoadAddress() != null && routeInfo.getLoadAddress().getLongitude() > 0 && routeInfo.getLoadAddress().getLatitude() > 0) {
            firstPoint = new LatLng(routeInfo.getLoadAddress().getLatitude(), routeInfo.getLoadAddress().getLongitude());

            LatLng lastPoint = null;
            if (routeInfo.getUnloadAddress() != null && routeInfo.getUnloadAddress().getLongitude() > 0 && routeInfo.getUnloadAddress().getLatitude() > 0) {
                lastPoint = new LatLng(routeInfo.getUnloadAddress().getLatitude(), routeInfo.getUnloadAddress().getLongitude());
            }
            if (firstPoint != null && lastPoint != null) {
                moveCamera(firstPoint, lastPoint);
                insertLoadUnloadMarker(firstPoint, lastPoint);

            }

            PolylineOptions plineOptions = new PolylineOptions();
            if (coArr.length >= 1) {
                for (int i = 0; i < coArr.length; i++) {
                    String[] t = coArr[i].split(",");
                    if (t[0].equals("")) {
                        continue;
                    }
                    plineOptions.add(new LatLng(Double.valueOf(t[1]), Double.valueOf(t[0])));
                }
            }

            adjustLineStyle(plineOptions);
            aMap.addPolyline(plineOptions);
        }
    }

    private void moveCamera(LatLng... points){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i< points.length; i++){
            builder.include(points[i]);
        }
        LatLngBounds bounds = builder.build();
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,120));
    }


    private void insertLoadUnloadMarker(LatLng firstPoint,LatLng lastPoint) {

        MarkerOptions loadMarkerOptions = new MarkerOptions();
        loadMarkerOptions.icon(BitmapDescriptorFactory.fromView(AMapUtils.getImageViewForMarker(TraceActivity.this,AMapUtils.MarkerIcon.Start)))
                .anchor(0.5f, 0.8f)
                .position(firstPoint)
                .title("装货地");

        aMap.addMarker(loadMarkerOptions);

        MarkerOptions unloadMarkerOptions = new MarkerOptions();
        unloadMarkerOptions.icon(BitmapDescriptorFactory.fromView(AMapUtils.getImageViewForMarker(TraceActivity.this,AMapUtils.MarkerIcon.End)))
                .anchor(0.5f, 0.8f)
                .position(lastPoint)
                .title("卸货地");
        aMap.addMarker(unloadMarkerOptions);
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


}
