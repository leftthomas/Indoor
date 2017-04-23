package com.left.im.ui;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.left.im.R;
import com.left.im.base.ParentWithNaviActivity;
import com.left.im.bean.Goods;

import butterknife.Bind;

/**
 * 商品
 *
 * @author :left
 * @project:GoodActivity
 * @date :2017-04-25-18:23
 */
public class GoodActivity extends ParentWithNaviActivity {

    @Bind(R.id.good_describe)
    WebView good_describe;
    Goods good;

    @Override
    protected String title() {
        return good.getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good);
        good = (Goods) getBundle().getSerializable("good");
        initNaviView();
        //加载富文本
        good_describe.loadDataWithBaseURL("", good.getDescribe(), "text/html", "UTF-8", "");
        good_describe.setHorizontalScrollBarEnabled(false);//水平不显示滚动条
        good_describe.setVerticalScrollBarEnabled(false); //垂直不显示滚动条
        //使能JavaScript
        good_describe.getSettings().setJavaScriptEnabled(true);
        //使其html中的链接点击不采用系统浏览器或第三方浏览器进行跳转，而采用自身打开
        good_describe.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
    }

}
