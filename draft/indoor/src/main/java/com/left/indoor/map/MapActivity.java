package com.left.indoor.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.left.indoor.bean.IndoorUser;
import com.left.indoor.setting.MapsettingActivity;
import com.left.indoor.utils.MyApplication;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;

public class MapActivity extends Activity {
	 
	public MapView mMapView = null;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    public MyLocationData locData;
    BaiduMap mBaiduMap;
	ImageView mapsetting;
	ImageView locate;
	ImageView zoomin;
    Intent mapsettingIntent;
    boolean what=true;
	IndoorUser cuser;
	BmobInstallation myBmobInstallation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要在setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        setContentView(R.layout.activity_map);
		MyApplication.getInstance().addActivity(this);
		cuser = BmobUser.getCurrentUser(this,IndoorUser.class);
        // 使用推送服务时的初始化操作
        myBmobInstallation=BmobInstallation.getCurrentInstallation(this);
		myBmobInstallation.save();
        // 启动推送服务
        BmobPush.startWork(this, "dfd76835fa9208696db407291e5eab51");
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //普通地图  (地图类型设置)
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层 
        mBaiduMap.setMyLocationEnabled(true); 
		LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(900);//设置发起定位请求的间隔时间为900ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
        mLocationClient.start();//开始定位
        mLocationClient.requestLocation();
        mapsetting=(ImageView) findViewById(R.id.mapsetting);
        locate = (ImageView) findViewById(R.id.locate);
        zoomin = (ImageView) findViewById(R.id.zoomout);
        mapsettingIntent=new Intent(MapActivity.this,MapsettingActivity.class);
        mapsetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
                mLocationClient.stop();//停止定位
                startActivity(mapsettingIntent);
			}
		});
        locate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				locate.setImageResource(R.drawable.locate);
				zoomin.setImageResource(R.drawable.zoomout);
				what=true;
                mLocationClient.start();//开始定位
                mLocationClient.requestLocation();
			}	
		});
        zoomin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				zoomin.setImageResource(R.drawable.zoomin);
				locate.setImageResource(R.drawable.unlocate);
				what=false;
                mLocationClient.start();//开始定位
                mLocationClient.requestLocation();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
        mLocationClient.stop();//停止定位
        // 当不需要定位图层时关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();  
	}
	@Override
	protected void onPause() {
		super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();  
	}
	@Override
	protected void onResume() {
		super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();  
	}
	
	@Override
	protected void onStop() {
		super.onStop();
        mLocationClient.stop();//停止定位
    }

    //定位监听的接口
    public class MyLocationListener implements BDLocationListener{
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
		            return ;
            //将当前位置坐标信息存到本地
            String position=location.getAddrStr();
	    	cuser.setPosition(position);
            //保存数据
            cuser.update(MapActivity.this);
            // 构造定位数据
            locData = new MyLocationData.Builder()
			    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
			    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            MyLocationConfiguration config = new MyLocationConfiguration(null, true, null);
			mBaiduMap.setMyLocationConfigeration(config);  
			float zoomf;
			if(what==true)
			    zoomf=17;
			else
				zoomf=4;
            //设定中心点坐标
            LatLng cenpt = new LatLng(location.getLatitude(),location.getLongitude());
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder()
		        .target(cenpt)
		        .zoom(zoomf)
		        .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
		}
	}
}


