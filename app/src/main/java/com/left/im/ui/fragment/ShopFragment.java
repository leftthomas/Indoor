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
 * 购物
 *
 * @author :left
 * @project:SetFragment
 * @date :2017-04-23-18:23
 */
public class ShopFragment extends ParentWithNaviFragment {

    //默认展示热门商品
    private String goodsCategory = "热门";
    private CharSequence[] items = {"热门", "智能设备", "食品", "玩具", "体育用品", "家居用品", "箱包", "化妆品", "配饰", "家用电器"};

    public ShopFragment() {
    }

    public static ShopFragment newInstance() {
        ShopFragment fragment = new ShopFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String title() {
        return "购物";
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
                builder.setTitle("选择商品类别");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        goodsCategory = items[item].toString();
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
        rootView = inflater.inflate(R.layout.fragment_shop, container, false);
        initNaviView();
        ButterKnife.bind(this, rootView);
        return rootView;
    }

}
