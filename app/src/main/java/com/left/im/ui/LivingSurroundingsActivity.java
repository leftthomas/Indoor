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

        startActivity(ArActivity.class, null);
    }

    @OnClick(R.id.buy)
    public void onBugClick(View view) {

        startActivity(ArActivity.class, null);
    }

    @OnClick(R.id.hotel)
    public void onHotelClick(View view) {

        startActivity(ArActivity.class, null);
    }

    @OnClick(R.id.cinema)
    public void onCinemaClick(View view) {

        startActivity(ArActivity.class, null);
    }

    @OnClick(R.id.field)
    public void onFieldClick(View view) {

        startActivity(ArActivity.class, null);
    }

    @OnClick(R.id.hospital)
    public void onHospitalClick(View view) {

        startActivity(ArActivity.class, null);
    }

    @OnClick(R.id.car)
    public void onCarClick(View view) {

        startActivity(ArActivity.class, null);
    }

    @OnClick(R.id.wc)
    public void onWcClick(View view) {

        startActivity(ArActivity.class, null);
    }

    @OnClick(R.id.balance)
    public void onBalanceClick(View view) {

        startActivity(ArActivity.class, null);
    }

    @OnClick(R.id.atm)
    public void onAtmClick(View view) {

        startActivity(ArActivity.class, null);
    }

    @OnClick(R.id.bank)
    public void onBankClick(View view) {

        startActivity(ArActivity.class, null);
    }

    @OnClick(R.id.book)
    public void onBookClick(View view) {

        startActivity(ArActivity.class, null);
    }

    @OnClick(R.id.park)
    public void onParkClick(View view) {

        startActivity(ArActivity.class, null);
    }

    @OnClick(R.id.police)
    public void onPolicelick(View view) {

        startActivity(ArActivity.class, null);
    }

    @OnClick(R.id.bus)
    public void onBusClick(View view) {

        startActivity(ArActivity.class, null);
    }

}
