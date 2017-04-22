package com.indoor.im.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.indoor.im.R;
import com.indoor.im.bean.User;
import com.indoor.im.config.BmobConstants;
import com.indoor.im.util.CommonUtils;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.List;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterActivity extends BaseActivity {

    Button btn_register;
    EditText et_username, et_password, et_email;
    BmobChatUser currentUser;
    CircleProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initTopBarForLeft("注册");

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);
        progress = (CircleProgressBar) findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                register();
            }
        });
        //checkUser();
    }


    private void checkUser() {
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("username", et_username.getText().toString());
        query.findObjects(this, new FindListener<User>() {

            @Override
            public void onError(int arg0, String arg1) {

            }

            @Override
            public void onSuccess(List<User> arg0) {
                if (arg0 != null && arg0.size() > 0) {
                    User user = arg0.get(0);
                    user.setPassword(et_email.getText().toString());
                    user.update(RegisterActivity.this, new UpdateListener() {

                        @Override
                        public void onSuccess() {
                            userManager.login(et_username.getText().toString(), et_email.getText().toString(), new SaveListener() {

                                @Override
                                public void onSuccess() {
                                    Log.i("smile", "登陆成功");
                                }

                                @Override
                                public void onFailure(int code, String msg) {
                                    Log.i("smile", "登陆失败：" + code + ".msg = " + msg);
                                }
                            });
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                        }
                    });
                }
            }
        });
    }

    private void register() {
        String name = et_username.getText().toString();
        String password = et_password.getText().toString();
        String pwd_again = et_email.getText().toString();

        if (TextUtils.isEmpty(name)) {
            showTag("用户名不能为空", Effects.thumbSlider, R.id.register);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showTag("密码不能为空", Effects.thumbSlider, R.id.register);
            return;
        }
        if (!pwd_again.equals(password)) {
            showTag("输入的两次密码不一致", Effects.thumbSlider, R.id.register);
            return;
        }

        boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
        if (!isNetConnected) {
            showTag("当前网络不可用,请检查您的网络!", Effects.thumbSlider, R.id.register);
            return;
        }

        progress.setVisibility(View.VISIBLE);
        //由于每个应用的注册所需的资料都不一样，故IM sdk未提供注册方法，用户可按照bmod SDK的注册方式进行注册。
        //注册的时候需要注意两点：1、User表中绑定设备id和type，2、设备表中绑定username字段
        final User bu = new User();
        bu.setUsername(name);
        bu.setPassword(password);
        //将user和设备id进行绑定aa
        bu.setSex(true);
        bu.setDeviceType("android");
        bu.setInstallId(BmobInstallation.getInstallationId(this));
        bu.signUp(RegisterActivity.this, new SaveListener() {

            @Override
            public void onSuccess() {
                progress.setVisibility(View.GONE);
                showTag("注册成功", Effects.thumbSlider, R.id.register);
                // 将设备与username进行绑定
                userManager.bindInstallationForRegister(bu.getUsername());
                //更新地理位置信息
                updateUserLocation();

                //发广播通知登陆页面退出
                sendBroadcast(new Intent(BmobConstants.ACTION_REGISTER_SUCCESS_FINISH));
                // 启动主页
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                BmobLog.i(arg1);
                showTag("注册失败:" + arg1, Effects.thumbSlider, R.id.register);
                progress.setVisibility(View.GONE);
            }
        });
    }

}
