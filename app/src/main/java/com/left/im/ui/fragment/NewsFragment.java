package com.left.im.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.left.im.R;
import com.left.im.base.ParentWithNaviActivity;
import com.left.im.base.ParentWithNaviFragment;

import butterknife.ButterKnife;

/**
 * 新闻界面
 *
 * @author :left
 * @project:SetFragment
 * @date :2017-04-23-18:23
 */
public class NewsFragment extends ParentWithNaviFragment {

    //默认展示头条新闻
    private String newsCategory = "头条";
    private CharSequence[] items = {"头条", "娱乐", "科技", "影视", "体育", "军事",
            "财经", "游戏", "旅游", "房产", "政务"};

    public NewsFragment() {
    }

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String title() {
        return "新闻";
    }

    @Override
    public Object right() {
        return R.drawable.base_action_bar_more_bg_selector;
    }

    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {

            }

            @Override
            public void clickRight() {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("选择新闻类别");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        newsCategory = items[item].toString();
//                        initData();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news, container, false);
        initNaviView();
        ButterKnife.bind(this, rootView);

        return rootView;
    }

}
