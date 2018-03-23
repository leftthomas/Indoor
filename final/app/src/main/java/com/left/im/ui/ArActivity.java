package com.left.im.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import com.amap.api.navi.AMapHudView;
import com.amap.api.navi.AMapHudViewListener;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.left.im.BmobIMApplication;
import com.left.im.R;
import com.left.im.base.ParentWithNaviActivity;

import java.util.ArrayList;

/**
 * AR导航
 *
 * @author :left
 * @project:ArActivity
 * @date :2017-04-25-18:23
 */
public class ArActivity extends ParentWithNaviActivity implements AMapNaviListener, AMapHudViewListener, PoiSearch.OnPoiSearchListener {

    String poi;
    PoiResult result;
    PoiItem poi_item;
    Context context;
    private AMapHudView mAMapHudView;
    private AMapNavi mAMapNavi;


    @Override
    protected String title() {
        return poi;
    }

    @Override
    public Object right() {
        return R.drawable.base_action_bar_more_bg_selector;
    }

    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {
                mAMapNavi.stopNavi();
                finish();
            }

            @Override
            public void clickRight() {
                ArrayList<String> items = new ArrayList<>();
                for (PoiItem item : result.getPois()) {
                    items.add(item.getTitle() + "; " + item.getDistance() + "米");
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("选择你想去的地方");
                builder.setItems(items.toArray(new CharSequence[items.size()]), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        poi_item = result.getPois().get(item);
                        mAMapNavi.calculateWalkRoute(new NaviLatLng(BmobIMApplication
                                .getCurrent_user_location().getLatitude(), BmobIMApplication
                                .getCurrent_user_location().getLongitude()), new NaviLatLng(poi_item
                                .getLatLonPoint().getLatitude(), poi_item.getLatLonPoint().getLongitude()));
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAMapNavi = AMapNavi.getInstance(this);
        mAMapNavi.addAMapNaviListener(this);
        setContentView(R.layout.activity_ar);
        mAMapHudView = (AMapHudView) findViewById(R.id.hudview);
        mAMapHudView.setHudViewListener(this);
        context = this;
        poi = (String) getBundle().getSerializable("poi");
        initNaviView();
        PoiSearch.Query query = new PoiSearch.Query("", poi, BmobIMApplication.getCurrent_user_location().getCityCode());
        // 设置每页最多返回多少条poiitem
        query.setPageSize(30);
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(BmobIMApplication
                .getCurrent_user_location().getLatitude(), BmobIMApplication.getCurrent_user_location()
                .getLongitude()), 3000));//设置周边搜索的中心点以及半径
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        result = poiResult;
    }

    @Override
    public void onCalculateRouteSuccess() {
        //显示路径或开启导航
        AMapNavi.getInstance(this).startNavi(NaviType.GPS);
    }


    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onInitNaviSuccess() {
    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onHudViewCancel() {
        mAMapNavi.stopNavi();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapHudView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapHudView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapHudView.onDestroy();
        mAMapHudView = null;
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
    }

    /**
     * 返回键监听
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mAMapNavi.stopNavi();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
