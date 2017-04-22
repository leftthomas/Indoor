package com.indoor.im.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.gitonway.niftydialogeffects.widget.niftydialogeffects.Effectstype;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;
import com.indoor.im.R;
import com.indoor.im.adapter.MessageRecentAdapter;
import com.indoor.im.ui.ChatActivity;
import com.indoor.im.view.ClearEditText;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.db.BmobDB;

/**
 * 最近会话
 *
 * @author smile
 * @ClassName: ConversationFragment
 * @Description: TODO
 * @date 2014-6-7 下午1:01:37
 */
public class RecentFragment extends FragmentBase implements OnItemClickListener, OnItemLongClickListener {

    ClearEditText mClearEditText;

    ListView listview;

    MessageRecentAdapter adapter;
    private boolean hidden;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recent, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        initTopBarForOnlyTitle("会话");
        listview = (ListView) findViewById(R.id.list);
        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);
        adapter = new MessageRecentAdapter(getActivity(), R.layout.item_conversation, BmobDB.create(getActivity()).queryRecents());
        listview.setAdapter(adapter);

        mClearEditText = (ClearEditText) findViewById(R.id.et_msg_search);
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    /**
     * 删除会话
     * deleteRecent
     *
     * @param @param recent
     * @return void
     * @throws
     */
    private void deleteRecent(BmobRecent recent) {
        adapter.remove(recent);
        BmobDB.create(getActivity()).deleteRecent(recent.getTargetid());
        BmobDB.create(getActivity()).deleteMessages(recent.getTargetid());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
                                   long arg3) {
        // TODO Auto-generated method stub
        BmobRecent recent = adapter.getItem(position);
        showDeleteDialog(recent);
        return true;
    }

    public void showDeleteDialog(final BmobRecent recent) {
        final NiftyDialogBuilder dialogBuilder = new NiftyDialogBuilder(getActivity(), R.style.dialog_untran);
        dialogBuilder
                .withTitle(recent.getUserName())                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage("删除会话")                             //.withMessage(null)  no Msg
                .withMessageColor("#FFFFFF")                                //def
                .withIcon(getResources().getDrawable(R.drawable.icon))
                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                .withDuration(700)                                          //def
                .withEffect(Effectstype.Fadein)                                         //def Effectstype.Slidetop
                .withButton1Text("确定")                                      //def gone
                .withButton2Text("取消")                                  //def gone
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteRecent(recent);
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

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        // TODO Auto-generated method stub
        BmobRecent recent = adapter.getItem(position);
        //重置未读消息
        BmobDB.create(getActivity()).resetUnread(recent.getTargetid());
        //组装聊天对象
        BmobChatUser user = new BmobChatUser();
        user.setAvatar(recent.getAvatar());
        user.setNick(recent.getNick());
        user.setUsername(recent.getUserName());
        user.setObjectId(recent.getTargetid());
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("user", user);
        startAnimActivity(intent);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            refresh();
        }
    }

    public void refresh() {
        try {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    adapter = new MessageRecentAdapter(getActivity(), R.layout.item_conversation, BmobDB
                            .create(getActivity()).queryRecents());
                    listview.setAdapter(adapter);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
        }
    }

}
