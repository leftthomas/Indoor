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
import com.left.im.BmobIMApplication;
import com.left.im.R;
import com.left.im.base.ParentWithNaviActivity;
import com.left.im.bean.Friend;
import com.left.im.model.UserModel;

import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.listener.FindListener;

/**
 * 朋友分布
 *
 * @author :left
 * @project:BlackListActivity
 * @date :2017-04-25-18:23
 */
public class FriendDistributionActivity extends ParentWithNaviActivity {

    @Bind(R.id.map)
    MapView mMapView = null;

    AMap aMap = null;

    @Override
    protected String title() {
        return "朋友分布";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_distribution);
        initNaviView();
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        aMap = mMapView.getMap();
        UserModel.getInstance().queryFriends(new FindListener<Friend>() {
            @Override
            public void onSuccess(List<Friend> list) {
                for (Friend friend : list) {
                    LatLng latLng = new LatLng(friend.getFriendUser().getLocation().getLatitude(),
                            friend.getFriendUser().getLocation().getLongitude());
                    aMap.addMarker(new MarkerOptions().position(latLng).title(friend.getFriendUser()
                            .getUsername()).snippet(friend.getFriendUser().getSex()));
                }
            }

            @Override
            public void onError(int i, String s) {
                log(s);
            }
        });
        // 设置当前地图显示为当前位置
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(BmobIMApplication
                .getCurrent_user_location().getLatitude(), BmobIMApplication
                .getCurrent_user_location().getLongitude()), 16));
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.mipmap.location_marker));
        aMap.addMarker(new MarkerOptions().position(new LatLng(BmobIMApplication
                .getCurrent_user_location().getLatitude(), BmobIMApplication
                .getCurrent_user_location().getLongitude())).icon(bitmapDescriptor));
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
