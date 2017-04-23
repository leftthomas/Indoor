package com.left.im.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        startActivity(BlackListActivity.class, null);
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
