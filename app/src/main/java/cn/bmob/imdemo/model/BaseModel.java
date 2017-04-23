package cn.bmob.imdemo.model;

import android.content.Context;

import cn.bmob.imdemo.BmobIMApplication;

/**
 * @author :smile
 * @project:BaseModel
 * @date :2016-01-23-10:37
 */
public abstract class BaseModel {

    public static final int DEFAULT_LIMIT = 20;
    public static int CODE_NOT_EQUAL = 1001;
    public int CODE_NULL = 1000;

    public Context getContext() {
        return BmobIMApplication.INSTANCE();
    }
}
