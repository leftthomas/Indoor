package com.indoor.im.ui;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.gitonway.niftydialogeffects.widget.niftydialogeffects.Effectstype;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;
import com.indoor.im.R;
import com.indoor.im.adapter.NewFriendAdapter;

import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.db.BmobDB;

/**
 * 新朋友
 *
 * @author smile
 * @ClassName: NewFriendActivity
 * @Description: TODO
 * @date 2014-6-6 下午4:28:09
 */
public class NewFriendActivity extends ActivityBase implements OnItemLongClickListener {

    ListView listview;

    NewFriendAdapter adapter;

    String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        from = getIntent().getStringExtra("from");
        initView();
    }

    private void initView() {
        initTopBarForLeft("新朋友");
        listview = (ListView) findViewById(R.id.list_newfriend);
        listview.setOnItemLongClickListener(this);
        adapter = new NewFriendAdapter(this, BmobDB.create(this).queryBmobInviteList());
        listview.setAdapter(adapter);
        if (from == null) {//若来自通知栏的点击，则定位到最后一条
            listview.setSelection(adapter.getCount());
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
                                   long arg3) {
        // TODO Auto-generated method stub
        BmobInvitation invite = (BmobInvitation) adapter.getItem(position);
        showDeleteDialog(position, invite);
        return true;
    }

    public void showDeleteDialog(final int position, final BmobInvitation invite) {
        final NiftyDialogBuilder dialogBuilder = new NiftyDialogBuilder(this, R.style.dialog_untran);
        dialogBuilder
                .withTitle(invite.getFromname())                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage("删除好友请求")                             //.withMessage(null)  no Msg
                .withMessageColor("#FFFFFF")                                //def
                .withIcon(getResources().getDrawable(R.drawable.icon))
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(700)                                          //def
                .withEffect(Effectstype.Newspager)                                         //def Effectstype.Slidetop
                .withButton1Text("确定")                                      //def gone
                .withButton2Text("取消")                                  //def gone
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteInvite(position, invite);
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

    /**
     * 删除请求
     * deleteRecent
     *
     * @param @param recent
     * @return void
     * @throws
     */
    private void deleteInvite(int position, BmobInvitation invite) {
        adapter.remove(position);
        BmobDB.create(this).deleteInviteMsg(invite.getFromid(), Long.toString(invite.getTime()));
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (from == null) {
            startAnimActivity(MainActivity.class);
        }
    }


}
