package com.left.im.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.webkit.WebView;

import com.left.im.R;
import com.left.im.base.ParentWithNaviActivity;
import com.left.im.bean.DailyArticle;
import com.left.im.util.CircleProgressBar;
import com.left.im.util.GestureListener;

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
    @Bind(R.id.progress)
    CircleProgressBar progress;

    String describe = "";   //内容
    int skipnum = 0;

    @Override
    protected String title() {
        return "每日一文";
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
                finish();
            }

            @Override
            public void clickRight() {
                Snackbar.make(article, "随机一篇", Snackbar.LENGTH_SHORT).show();
                getCount();
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_article);
        initNaviView();
        //setLongClickable是必须的
        article.setLongClickable(true);
        article.setOnTouchListener(new MyGestureListener(this));
        initData();
    }

    public void initData() {

        BmobQuery<DailyArticle> query = new BmobQuery<DailyArticle>();
        if (skipnum < 0) {
            Snackbar.make(article, "已是最新一篇！", Snackbar.LENGTH_SHORT).show();
            skipnum = 0;
        } else {
            progress.setVisibility(View.VISIBLE);
            query.setSkip(skipnum);
            query.order("-createdAt");
            //执行查询方法
            query.findObjects(this, new FindListener<DailyArticle>() {
                @Override
                public void onSuccess(List<DailyArticle> essay) {
                    if (essay.size() > 0) {
                        describe = essay.get(0).getEssay();
                        //加载富文本
                        article.loadDataWithBaseURL("", describe, "text/html", "UTF-8", "");
                        article.setHorizontalScrollBarEnabled(false);//水平不显示滚动条
                        progress.setVisibility(View.GONE);
                    } else {
                        Snackbar.make(article, "没有更多文章了！", Snackbar.LENGTH_SHORT).show();
                        skipnum--;
                        progress.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onError(int code, String msg) {
                    Snackbar.make(article, "请确认网络是否连接！", Snackbar.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            });
        }
    }

    private void getCount() {
        //查询DailyArticle表中总记录数并返回所有记录信息
        String bql = "select count(*),* from DailyArticle";
        new BmobQuery<DailyArticle>().doSQLQuery(this, bql, new SQLQueryListener<DailyArticle>() {

            @Override
            public void done(BmobQueryResult<DailyArticle> result, BmobException e) {
                if (e == null) {
                    Random random = new Random();
                    //因为序号从1开始编起，所以要+1
                    skipnum = random.nextInt(result.getCount()) + 1;
                    initData();
                } else {
                    Snackbar.make(article, "网络错误，请确认网络是否连通！", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 继承GestureListener，重写left和right方法
     */
    private class MyGestureListener extends GestureListener {
        public MyGestureListener(Context context) {
            super(context);
        }

        @Override
        public boolean left() {
            //往后查
            skipnum--;
            initData();
            return super.left();
        }

        @Override
        public boolean right() {
            //查询历史文章，往前查
            skipnum++;
            initData();
            return super.right();
        }
    }

}
