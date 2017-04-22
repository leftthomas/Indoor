package com.indoor.im.ui;

import android.content.Intent;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.indoor.im.CustomApplcation;
import com.indoor.im.R;
import com.indoor.im.bean.User;
import com.indoor.im.util.CollectionUtils;
import com.indoor.im.view.Mark;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


public class FriendsMapActivity extends ActivityBase {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private BDLocationListener myListener;
    private MyLocationData locData;
    private List<User> allFriends;
    private int num;
    private LatLng point;
    //定义Maker坐标点
    private Mark mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_map);
        initTopBarForLeft("朋友分布");
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //普通地图  
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        initlocation();
    }

    private void initlocation() {
        myListener = new MyLocationListener();
        mLocationClient = new LocationClient(getApplicationContext());  //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        //option.setScanSpan(1000);//设置发起定位请求的间隔时间为1000ms,不设置则一次定位
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
        mLocationClient.start();//开始定位
        mLocationClient.requestLocation();
        //调用BaiduMap对象的setOnMarkerClickListener方法设置marker点击的监听
        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {

                //先进入好友的详细资料页面
                Intent intent = new Intent(FriendsMapActivity.this, SetMyInfoActivity.class);
                intent.putExtra("from", "other");
                intent.putExtra("username", arg0.getTitle());
                startAnimActivity(intent);
                return false;
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
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mMapView.onPause();
    }

    public void setFriendMask() {
        Map<String, BmobChatUser> users = CustomApplcation.getInstance().getContactList();
        final List<BmobChatUser> friends = CollectionUtils.map2list(users);
        allFriends = new ArrayList<User>();
        num = 0;
        for (int i = 0; i < friends.size(); i++) {
            BmobQuery<User> query = new BmobQuery<User>();
            query.addWhereEqualTo("objectId", friends.get(i).getObjectId());
            query.findObjects(this, new FindListener<User>() {
                @Override
                public void onSuccess(List<User> object) {
                    allFriends.add(object.get(0));
                    num++;
                    if (num == friends.size()) {
                        for (int j = 0; j < allFriends.size(); j++) {
                            point = new LatLng(allFriends.get(j).getLocation().getLatitude(),
                                    allFriends.get(j).getLocation().getLongitude());
                            //定义Maker坐标点
                            mark = new Mark(FriendsMapActivity.this);
                            //构建Marker图标
                            mark.setUsername(allFriends.get(j).getUsername());
                            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(mark);
                            //构建MarkerOption，用于在地图上添加Marker
                            OverlayOptions option = new MarkerOptions()
                                    .position(point)
                                    .zIndex(9)
                                    .icon(bitmap);
                            //在地图上添加Marker，并显示
                            Marker marker = (Marker) (mBaiduMap.addOverlay(option));
                            //Title与Username对应
                            marker.setTitle(allFriends.get(j).getUsername());
                        }
                    }
                }

                @Override
                public void onError(int code, String msg) {
                    showTag(msg, Effects.slideIn, R.id.bmapView);
                }
            });
        }
    }

    //定位监听的接口
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;
            //将当前位置坐标信息存到本地
            //String position=location.getAddrStr();
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
            //设定中心点坐标
            LatLng cenpt = new LatLng(location.getLatitude(), location.getLongitude());
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder()
                    .target(cenpt)
                    .zoom(15)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
            setFriendMask();
        }
    }

}
