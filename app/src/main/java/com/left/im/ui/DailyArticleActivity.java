package com.left.im.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.webkit.WebView;

import com.gc.materialdesign.widgets.SnackBar;
import com.left.im.R;
import com.left.im.base.ParentWithNaviActivity;
import com.left.im.bean.DailyArticle;

import java.util.List;
import java.util.Random;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;

/**
 * 每日一文
 *
 * @author :left
 * @project:DailyArticleActivity
 * @date :2017-04-25-18:23
 */
public class DailyArticleActivity extends ParentWithNaviActivity {

    @Bind(R.id.edit_describe)
    WebView article;
    @Bind(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    //内容
    String describe;
    int skipnum = 0;
    Context context;
    Activity activity;

    @Override
    protected String title() {
        return "每日一文";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_article);
        initNaviView();
        context = this;
        activity = this;
        sw_refresh.setEnabled(true);
        setListener();
    }

    private void setListener() {
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new SnackBar(activity, "随机一篇", "", null).show();
                query();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sw_refresh.setRefreshing(true);
        query();
    }

    /**
     * 查询每日文章
     */
    public void query() {
        //查询DailyArticle表中总记录数并返回所有记录信息
        String bql = "select count(*),* from DailyArticle";
        new BmobQuery<DailyArticle>().doSQLQuery(this, bql, new SQLQueryListener<DailyArticle>() {
            @Override
            public void done(BmobQueryResult<DailyArticle> result, BmobException e) {
                if (e == null) {
                    Random random = new Random();
                    //因为序号从1开始编起，所以要+1
                    skipnum = random.nextInt(result.getCount()) + 1;
                    BmobQuery<DailyArticle> query = new BmobQuery<DailyArticle>();
                    query.setSkip(skipnum);
                    query.order("-createdAt");
                    //执行查询方法
                    query.findObjects(context, new FindListener<DailyArticle>() {
                        @Override
                        public void onSuccess(List<DailyArticle> essay) {
                            if (essay.size() > 0) {
                                describe = essay.get(0).getEssay();
                                //加载富文本
                                article.loadDataWithBaseURL("", describe, "text/html", "UTF-8", "");
                                article.setHorizontalScrollBarEnabled(false);//水平不显示滚动条
                            } else {
                                new SnackBar(activity, "没有更多文章了！", "", null).show();
                            }
                        }

                        @Override
                        public void onError(int code, String msg) {
                            new SnackBar(activity, "请确认网络是否连接！", "", null).show();
                        }
                    });
                } else {
                    new SnackBar(activity, "网络错误，请确认网络是否连通！", "", null).show();
                }
            }
        });
        sw_refresh.setRefreshing(false);
    }
}
