package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.overlay.DrivingRouteOverlay;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.R.id.mapView;

public class MapRouteOverlayActivity extends MyBaseActivity implements LocationSource,AMapLocationListener,RouteSearch.OnRouteSearchListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(mapView)
    MapView mMapView;

    // 地图对象
    AMap aMap;

    //
    RouteSearch routeSearch;

    LatLonPoint to;
    LatLonPoint from;

    // 定位
    OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;

    private DriveRouteResult driveRouteResult;// 驾车模式查询结果

    private Double latitude;
    private Double longitude;

    private boolean locationFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_route_overlay);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);

        inits();

        initAMap();
    }

    private void initRoute() {
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);

        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(from,to);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_DEFAULT, null, null, "");
        routeSearch.calculateDriveRouteAsyn(query);
    }

    private void inits() {

        latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));
        longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));

        to = new LatLonPoint(latitude,longitude);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    private void initAMap() {
        //初始化地图变量
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
//        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
//        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);

        aMap.getUiSettings().setCompassEnabled(true);
        aMap.getUiSettings().setZoomControlsEnabled(false);

    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }
    }

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
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                if(locationFlag){
                    locationFlag = false;
                    from = new LatLonPoint(amapLocation.getLatitude(),amapLocation.getLongitude());
                    initRoute();
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
        System.out.println("rCode="+rCode);
        if (rCode == 1000) {
            if (result != null && result.getPaths() != null
                    && result.getPaths().size() > 0) {
                driveRouteResult = result;
                DrivePath drivePath = driveRouteResult.getPaths().get(0);
                aMap.clear();// 清理地图上的所有覆盖物
                DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                        this, aMap, drivePath, driveRouteResult.getStartPos(),
                        driveRouteResult.getTargetPos(),null);
                drivingRouteOverlay.removeFromMap();
                drivingRouteOverlay.addToMap();
                drivingRouteOverlay.zoomToSpan();
            } else {
                Toast.makeText(MapRouteOverlayActivity.this, "对不起，没查询到结果", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (rCode == 1804) {
            Toast.makeText(MapRouteOverlayActivity.this, "net error", Toast.LENGTH_SHORT).show();
        } else if (rCode == 2003) {
            Toast.makeText(MapRouteOverlayActivity.this, "key error", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MapRouteOverlayActivity.this, "other error", Toast.LENGTH_SHORT).show();
        }
        System.out.println("========onDriveRouteSearched()=========");
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
