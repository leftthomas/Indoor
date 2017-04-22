package com.indoor.im.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.indoor.im.R;

@SuppressLint("InflateParams")
public class Mark extends LinearLayout {

    private View MarkView;//MarkView的布局

    public Mark(Context context) {
        super(context);
        initView(context);
    }

    public Mark(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @SuppressLint("NewApi")
    public Mark(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    //初始化界面，添加布局文件到Mark中
    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        MarkView = inflater.inflate(R.layout.mark_view, null);
        this.addView(MarkView);
    }

    public void setUsername(String name) {
        TextView textView = (TextView) findViewById(R.id.username);
        textView.setText(name);
    }
}
