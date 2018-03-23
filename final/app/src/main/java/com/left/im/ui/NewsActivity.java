package com.left.im.ui;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.left.im.R;
import com.left.im.base.ParentWithNaviActivity;
import com.left.im.bean.News;

import butterknife.Bind;

/**
 * 新闻
 *
 * @author :left
 * @project:NewsActivity
 * @date :2017-04-25-18:23
 */
public class NewsActivity extends ParentWithNaviActivity {

    @Bind(R.id.news_details)
    WebView news_details;
    News news;

    @Override
    protected String title() {
        return news.getTitle();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        news = (News) getBundle().getSerializable("news");
        initNaviView();
        //加载富文本
        news_details.loadDataWithBaseURL("", news.getDetails(), "text/html", "UTF-8", "");
        news_details.setHorizontalScrollBarEnabled(false);//水平不显示滚动条
        news_details.setVerticalScrollBarEnabled(false); //垂直不显示滚动条
        //使能JavaScript
        news_details.getSettings().setJavaScriptEnabled(true);
        //使其html中的链接点击不采用系统浏览器或第三方浏览器进行跳转，而采用自身打开
        news_details.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
    }

}
