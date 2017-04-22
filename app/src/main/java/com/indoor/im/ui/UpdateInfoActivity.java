package com.indoor.im.ui;

import android.os.Bundle;
import android.widget.EditText;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.indoor.im.R;
import com.indoor.im.bean.User;
import com.indoor.im.view.HeaderLayout.onRightImageButtonClickListener;

import cn.bmob.v3.listener.UpdateListener;

/**
 * 设置昵称和性别
 *
 * @author smile
 * @ClassName: SetNickAndSexActivity
 * @Description: TODO
 * @date 2014-6-7 下午4:03:40
 */
public class UpdateInfoActivity extends ActivityBase {

    EditText edit_nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_updateinfo);
        initView();
    }

    private void initView() {
        initTopBarForBoth("修改昵称", R.drawable.base_action_bar_true_bg_selector,
                new onRightImageButtonClickListener() {

                    @Override
                    public void onClick() {
                        // TODO Auto-generated method stub
                        String nick = edit_nick.getText().toString();
                        if (nick.equals("")) {
                            showTag("请填写昵称!", Effects.flip, R.id.setupdateinfo);
                            return;
                        }
                        updateInfo(nick);
                    }
                });
        edit_nick = (EditText) findViewById(R.id.edit_nick);
    }

    /**
     * 修改资料
     * updateInfo
     *
     * @return void
     * @throws
     * @Title: updateInfo
     */
    private void updateInfo(String nick) {
        final User user = userManager.getCurrentUser(User.class);
        User u = new User();
        u.setNick(nick);
        //u.setHight(110);
        u.setObjectId(user.getObjectId());
        u.update(this, new UpdateListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                //final User c = userManager.getCurrentUser(User.class);
                //ShowToast("修改成功:"+c.getNick()+",height = "+c.getHight());
                finish();
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                showTag("onFailure:" + arg1, Effects.flip, R.id.setupdateinfo);
            }
        });
    }
}
