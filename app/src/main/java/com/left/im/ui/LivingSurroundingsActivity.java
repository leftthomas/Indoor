package com.left.im.ui;

import android.os.Bundle;
import android.view.View;

import com.left.im.R;
import com.left.im.base.ParentWithNaviActivity;

import butterknife.OnClick;

/**
 * 生活周边
 *
 * @author :left
 * @project:BlackListActivity
 * @date :2017-04-25-18:23
 */
public class LivingSurroundingsActivity extends ParentWithNaviActivity {

    @Override
    protected String title() {
        return "生活周边";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living_surroundings);
        initNaviView();
    }

    @OnClick(R.id.food)
    public void onFoodClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("poi", "餐饮服务");
        startActivity(ArActivity.class, bundle);
    }

    @OnClick(R.id.buy)
    public void onBugClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("poi", "购物服务");
        startActivity(ArActivity.class, bundle);
    }

    @OnClick(R.id.hotel)
    public void onHotelClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("poi", "住宿服务");
        startActivity(ArActivity.class, bundle);
    }

    @OnClick(R.id.cinema)
    public void onCinemaClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("poi", "影剧院");
        startActivity(ArActivity.class, bundle);
    }

    @OnClick(R.id.field)
    public void onFieldClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("poi", "运动场馆");
        startActivity(ArActivity.class, bundle);
    }

    @OnClick(R.id.hospital)
    public void onHospitalClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("poi", "医疗保健");
        startActivity(ArActivity.class, bundle);
    }

    @OnClick(R.id.car)
    public void onCarClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("poi", "停车场");
        startActivity(ArActivity.class, bundle);
    }

    @OnClick(R.id.wc)
    public void onWcClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("poi", "公共厕所");
        startActivity(ArActivity.class, bundle);
    }

    @OnClick(R.id.balance)
    public void onBalanceClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("poi", "娱乐场所");
        startActivity(ArActivity.class, bundle);
    }

    @OnClick(R.id.atm)
    public void onAtmClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("poi", "自动提款机");
        startActivity(ArActivity.class, bundle);
    }

    @OnClick(R.id.bank)
    public void onBankClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("poi", "银行");
        startActivity(ArActivity.class, bundle);
    }

    @OnClick(R.id.book)
    public void onBookClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("poi", "科教文化");
        startActivity(ArActivity.class, bundle);
    }

    @OnClick(R.id.park)
    public void onParkClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("poi", "风景名胜");
        startActivity(ArActivity.class, bundle);
    }

    @OnClick(R.id.police)
    public void onPolicelick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("poi", "政府机关");
        startActivity(ArActivity.class, bundle);
    }

    @OnClick(R.id.bus)
    public void onBusClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("poi", "公交车站");
        startActivity(ArActivity.class, bundle);
    }

}
