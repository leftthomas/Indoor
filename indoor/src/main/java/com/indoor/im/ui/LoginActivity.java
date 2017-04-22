package com.indoor.im.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.Effectstype;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;
import com.indoor.im.R;
import com.indoor.im.bean.User;
import com.indoor.im.config.BmobConstants;
import com.indoor.im.util.CommonUtils;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author smile
 * @ClassName: LoginActivity
 * @Description: TODO
 * @date 2014-6-3 下午4:41:42
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

    EditText et_username, et_password;
    Button btn_login;
    TextView btn_register;
    CircleProgressBar progress;

    private MyBroadcastReceiver receiver = new MyBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        //注册退出广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(BmobConstants.ACTION_REGISTER_SUCCESS_FINISH);
        registerReceiver(receiver, filter);
//		showNotice();
    }

    public void showNotice() {
        final NiftyDialogBuilder dialogBuilder = new NiftyDialogBuilder(this, R.style.dialog_untran);
        dialogBuilder
                .withTitle("提示")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage("自V1.0.6版本开始，应用源码中将不再公开ApplicationId,开发者请自行到官网申请。申请成功之后在应用程序启动页(SplashActivity)进行初始化操作!")                             //.withMessage(null)  no Msg
                .withMessageColor("#FFFFFF")                                //def
                .withIcon(getResources().getDrawable(R.drawable.icon))
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(700)                                          //def
                .withEffect(Effectstype.Slideleft)                                         //def Effectstype.Slidetop
                .withButton1Text("确定")                                      //def gone
                .withButton2Text("取消")                                  //def gone
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .show();
    }

    private void init() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (TextView) findViewById(R.id.btn_register);
        progress = (CircleProgressBar) findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_register) {
            Intent intent = new Intent(LoginActivity.this,
                    RegisterActivity.class);
            startActivity(intent);
        } else {
            boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
            if (!isNetConnected) {
                showTag("当前网络不可用,请检查您的网络!", Effects.slideOnTop, R.id.login);
                return;
            }
            login();
        }
    }

    private void login() {
        String name = et_username.getText().toString();
        String password = et_password.getText().toString();

        if (TextUtils.isEmpty(name)) {
            showTag("用户名不能为空", Effects.slideOnTop, R.id.login);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showTag("密码不能为空", Effects.slideOnTop, R.id.login);
            return;
        }
        progress.setVisibility(View.VISIBLE);
        User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        userManager.login(user, new SaveListener() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //更新用户的地理位置以及好友的资料
                        updateUserInfos();
                        progress.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(int errorcode, String arg0) {
                progress.setVisibility(View.GONE);
                BmobLog.i(arg0);
                showTag(arg0, Effects.slideOnTop, R.id.login);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && BmobConstants.ACTION_REGISTER_SUCCESS_FINISH.equals(intent.getAction())) {
                finish();
            }
        }

    }

}
