package com.indoor.im.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.gitonway.lee.niftynotification.lib.Configuration;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.Effectstype;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;
import com.indoor.im.CustomApplcation;
import com.indoor.im.R;
import com.indoor.im.bean.User;
import com.indoor.im.util.CollectionUtils;
import com.indoor.im.view.HeaderLayout;
import com.indoor.im.view.HeaderLayout.HeaderStyle;
import com.indoor.im.view.HeaderLayout.onLeftImageButtonClickListener;
import com.indoor.im.view.HeaderLayout.onRightImageButtonClickListener;

import java.util.List;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 基类
 *
 * @author smile
 * @ClassName: BaseActivity
 * @Description: TODO
 * @date 2014-6-13 下午5:05:38
 */
public class BaseActivity extends FragmentActivity {

    protected HeaderLayout mHeaderLayout;
    protected int mScreenWidth;
    protected int mScreenHeight;
    BmobUserManager userManager;
    BmobChatManager manager;
    CustomApplcation mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userManager = BmobUserManager.getInstance(this);
        manager = BmobChatManager.getInstance(this);
        mApplication = CustomApplcation.getInstance();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
    }

    //显示提示消息
    public void showTag(String msg, Effects effect, int layoutid) {
        Configuration cfg = new Configuration.Builder()
                .setAnimDuration(700)
                .setDispalyDuration(1500)
                .setBackgroundColor("#FFFFFFFF")
                .setTextColor("#FF444444")
                .setIconBackgroundColor("#FFFFFFFF")
                .setTextPadding(5)                      //dp
                .setViewHeight(48)                      //dp
                .setTextLines(2)                        //You had better use setViewHeight and setTextLines together
                .setTextGravity(Gravity.CENTER)         //only text def  Gravity.CENTER,contain icon Gravity.CENTER_VERTICAL
                .build();
        NiftyNotificationView.build(this, msg, effect, layoutid, cfg)
                .setIcon(R.drawable.ic_launcher)               //remove this line ,only text
                .show();
    }

    /**
     * 打Log
     * ShowLog
     *
     * @return void
     * @throws
     */
    public void ShowLog(String msg) {
        Log.i("life", msg);
    }

    /**
     * 只有title initTopBarLayoutByTitle
     *
     * @throws
     * @Title: initTopBarLayoutByTitle
     */
    public void initTopBarForOnlyTitle(String titleName) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderStyle.DEFAULT_TITLE);
        mHeaderLayout.setDefaultTitle(titleName);
    }

    /**
     * 初始化标题栏-带左右按钮
     *
     * @return void
     * @throws
     */
    public void initTopBarForBoth(String titleName, int rightDrawableId, String text,
                                  onRightImageButtonClickListener listener) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                new OnLeftButtonClickListener());
        mHeaderLayout.setTitleAndRightButton(titleName, rightDrawableId, text,
                listener);
    }

    public void initTopBarForBoth(String titleName, int rightDrawableId,
                                  onRightImageButtonClickListener listener) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                new OnLeftButtonClickListener());
        mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
                listener);
    }

    /**
     * 只有左边按钮和Title initTopBarLayout
     *
     * @throws
     */
    public void initTopBarForLeft(String titleName) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.drawable.base_action_bar_back_bg_selector,
                new OnLeftButtonClickListener());
    }

    /**
     * 显示下线的对话框
     * showOfflineDialog
     *
     * @return void
     * @throws
     */
    public void showOfflineDialog(final Context context) {
        final NiftyDialogBuilder dialogBuilder = new NiftyDialogBuilder(this, R.style.dialog_untran);
        dialogBuilder
                .withTitle("下线通知")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage("您的账号已在其他设备上登录!")                             //.withMessage(null)  no Msg
                .withMessageColor("#FFFFFF")                                //def
                .withIcon(getResources().getDrawable(R.drawable.icon))
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                .withDuration(700)                                          //def
                .withEffect(Effectstype.Fliph)                                         //def Effectstype.Slidetop
                .withButton1Text("重新登录")                                      //def gone
                .withButton2Text("下线")                                  //def gone
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomApplcation.getInstance().logout();
                        startActivity(new Intent(context, LoginActivity.class));
                        finish();
                        dialogBuilder.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomApplcation.getInstance().logout();
                        startActivity(new Intent(context, LoginActivity.class));
                        finish();
                        dialogBuilder.dismiss();
                    }
                })
                .show();
    }

    public void startAnimActivity(Class<?> cla) {
        this.startActivity(new Intent(this, cla));
    }

    public void startAnimActivity(Intent intent) {
        this.startActivity(intent);
    }

    /**
     * 用于登陆或者自动登陆情况下的用户资料及好友资料的检测更新
     *
     * @param
     * @return void
     * @throws
     * @Title: updateUserInfos
     * @Description: TODO
     */
    public void updateUserInfos() {
        //更新地理位置信息
        updateUserLocation();
        //查询该用户的好友列表(这个好友列表是去除黑名单用户的哦),目前支持的查询好友个数为100，如需修改请在调用这个方法前设置BmobConfig.LIMIT_CONTACTS即可。
        //这里默认采取的是登陆成功之后即将好于列表存储到数据库中，并更新到当前内存中,
        userManager.queryCurrentContactList(new FindListener<BmobChatUser>() {

            @Override
            public void onError(int arg0, String arg1) {
                if (arg0 == BmobConfig.CODE_COMMON_NONE) {
                    ShowLog(arg1);
                } else {
                    ShowLog("查询好友列表失败：" + arg1);
                }
            }

            @Override
            public void onSuccess(List<BmobChatUser> arg0) {
                // 保存到application中方便比较
                CustomApplcation.getInstance().setContactList(CollectionUtils.list2map(arg0));
            }
        });
    }

    /**
     * 更新用户的经纬度信息
     *
     * @param
     * @return void
     * @throws
     * @Title: uploadLocation
     * @Description: TODO
     */
    public void updateUserLocation() {
        if (CustomApplcation.lastPoint != null) {
            String saveLatitude = mApplication.getLatitude();
            String saveLongtitude = mApplication.getLongtitude();
            String newLat = String.valueOf(CustomApplcation.lastPoint.getLatitude());
            String newLong = String.valueOf(CustomApplcation.lastPoint.getLongitude());
//			ShowLog("saveLatitude ="+saveLatitude+",saveLongtitude = "+saveLongtitude);
//			ShowLog("newLat ="+newLat+",newLong = "+newLong);
            if (!saveLatitude.equals(newLat) || !saveLongtitude.equals(newLong)) {//只有位置有变化就更新当前位置，达到实时更新的目的
                User u = (User) userManager.getCurrentUser(User.class);
                final User user = new User();
                user.setLocation(CustomApplcation.lastPoint);
                user.setObjectId(u.getObjectId());
                user.update(this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        CustomApplcation.getInstance().setLatitude(String.valueOf(user.getLocation().getLatitude()));
                        CustomApplcation.getInstance().setLongtitude(String.valueOf(user.getLocation().getLongitude()));
//						ShowLog("经纬度更新成功");
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
//						ShowLog("经纬度更新 失败:"+msg);
                    }
                });
            } else {
//				ShowLog("用户位置未发生过变化");
            }
        }
    }

    // 左边按钮的点击事件
    public class OnLeftButtonClickListener implements
            onLeftImageButtonClickListener {

        @Override
        public void onClick() {
            finish();
        }
    }

}
