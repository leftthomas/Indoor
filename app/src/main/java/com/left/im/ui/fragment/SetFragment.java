package com.left.im.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.left.im.R;
import com.left.im.base.ParentWithNaviFragment;
import com.left.im.bean.User;
import com.left.im.model.UserModel;
import com.left.im.ui.AboutUsActivity;
import com.left.im.ui.BlackListActivity;
import com.left.im.ui.LoginActivity;
import com.left.im.ui.UserInfoActivity;
import com.left.im.ui.WallPaperActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobUser;

/**
 * 设置
 *
 * @author :smile
 * @project:SetFragment
 * @date :2016-01-25-18:23
 */
public class SetFragment extends ParentWithNaviFragment {

    @Bind(R.id.tv_set_name)
    TextView tv_set_name;

    @Bind(R.id.layout_info)
    RelativeLayout layout_info;

    @Bind(R.id.iv_open_notification)
    ImageView iv_open_notification;
    @Bind(R.id.iv_close_notification)
    ImageView iv_close_notification;
    @Bind(R.id.iv_open_voice)
    ImageView iv_open_voice;
    @Bind(R.id.iv_close_voice)
    ImageView iv_close_voice;
    @Bind(R.id.iv_open_vibrate)
    ImageView iv_open_vibrate;
    @Bind(R.id.iv_close_vibrate)
    ImageView iv_close_vibrate;
    @Bind(R.id.rl_switch_voice)
    RelativeLayout rl_switch_voice;
    @Bind(R.id.rl_switch_vibrate)
    RelativeLayout rl_switch_vibrate;
    @Bind(R.id.view1)
    View view1;
    @Bind(R.id.view2)
    View view2;

    public SetFragment() {
    }

    public static SetFragment newInstance() {
        SetFragment fragment = new SetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String title() {
        return "设置";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_set, container, false);
        initNaviView();
        ButterKnife.bind(this, rootView);
        String username = UserModel.getInstance().getCurrentUser().getUsername();
        tv_set_name.setText(TextUtils.isEmpty(username) ? "" : username);
        return rootView;
    }

    @OnClick(R.id.layout_info)
    public void onInfoClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("u", BmobUser.getCurrentUser(getActivity(), User.class));
        startActivity(UserInfoActivity.class, bundle);
    }

    @OnClick(R.id.layout_blacklist)
    public void onBlacklistClick(View view) {
        startActivity(BlackListActivity.class, null);
    }

    @OnClick(R.id.layout_wallpaper)
    public void onWallpaperClick(View view) {
        startActivity(WallPaperActivity.class, null);
    }

    @OnClick(R.id.rl_switch_notification)
    public void onSwitchnotificationClick(View view) {
        if (iv_open_notification.getVisibility() == View.VISIBLE) {
            iv_open_notification.setVisibility(View.INVISIBLE);
            iv_close_notification.setVisibility(View.VISIBLE);
            rl_switch_voice.setVisibility(View.GONE);
            rl_switch_vibrate.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            // TODO: 2017/4/23
        } else {
            iv_close_notification.setVisibility(View.INVISIBLE);
            iv_open_notification.setVisibility(View.VISIBLE);
            rl_switch_voice.setVisibility(View.VISIBLE);
            rl_switch_vibrate.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
            // TODO: 2017/4/23
        }

    }

    @OnClick(R.id.rl_switch_voice)
    public void onSwitchvoiceClick(View view) {
        if (iv_open_voice.getVisibility() == View.VISIBLE) {
            iv_open_voice.setVisibility(View.INVISIBLE);
            iv_close_voice.setVisibility(View.VISIBLE);
            // TODO: 2017/4/23
        } else {
            iv_close_voice.setVisibility(View.INVISIBLE);
            iv_open_voice.setVisibility(View.VISIBLE);
            // TODO: 2017/4/23
        }
    }

    @OnClick(R.id.rl_switch_vibrate)
    public void onSwitchvibrateClick(View view) {
        if (iv_open_vibrate.getVisibility() == View.VISIBLE) {
            iv_open_vibrate.setVisibility(View.INVISIBLE);
            iv_close_vibrate.setVisibility(View.VISIBLE);
            // TODO: 2017/4/23
        } else {
            iv_close_vibrate.setVisibility(View.INVISIBLE);
            iv_open_vibrate.setVisibility(View.VISIBLE);
            // TODO: 2017/4/23
        }
    }

    @OnClick(R.id.btn_about_us)
    public void onAboutusClick(View view) {
        startActivity(AboutUsActivity.class, null);
    }

    @OnClick(R.id.btn_logout)
    public void onLogoutClick(View view) {
        UserModel.getInstance().logout();
        //可断开连接
        BmobIM.getInstance().disConnect();
        getActivity().finish();
        startActivity(LoginActivity.class, null);
    }
}
