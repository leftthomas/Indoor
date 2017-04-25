package com.left.im.ui;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.left.im.R;
import com.left.im.base.ParentWithNaviActivity;

import butterknife.Bind;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * 地图页
 *
 * @author :left
 * @project:BlackListActivity
 * @date :2017-04-25-18:23
 */
public class MapActivity extends ParentWithNaviActivity {

    @Bind(R.id.map)
    MapView mMapView = null;

    AMap aMap = null;

    @Override
    protected String title() {
        return "地图详情";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_distribution);
        initNaviView();
        BmobGeoPoint location = (BmobGeoPoint) getBundle().getSerializable("location");
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        aMap = mMapView.getMap();
        // 设置当前地图显示为当前位置
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.mipmap.location_marker));
        aMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).icon(bitmapDescriptor));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        if (mMapView != null)
            mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        if (mMapView != null)
            mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        if (mMapView != null)
            mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        if (mMapView != null)
            mMapView.onSaveInstanceState(outState);
    }
}
