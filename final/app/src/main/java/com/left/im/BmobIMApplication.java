package com.left.im;

import android.app.Application;

import com.amap.api.location.AMapLocation;
import com.left.im.base.UniversalImageLoader;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;

/**
 * @author :smile
 * @project:BmobIMApplication
 * @date :2016-01-13-10:19
 */
public class BmobIMApplication extends Application {


    private static BmobIMApplication INSTANCE;
    private static AMapLocation current_user_location;

    public static BmobIMApplication INSTANCE() {
        return INSTANCE;
    }

    public static AMapLocation getCurrent_user_location() {
        return current_user_location;
    }

    public static void setCurrent_user_location(AMapLocation current_user_location) {
        BmobIMApplication.current_user_location = current_user_location;
    }

    private static void setBmobIMApplication(BmobIMApplication a) {
        BmobIMApplication.INSTANCE = a;
    }

    /**
     * 获取当前运行的进程名
     *
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setInstance(BmobIMApplication app) {
        setBmobIMApplication(app);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        //初始化
        Logger.init("smile");
        //只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())) {
            //im初始化
            BmobIM.init(this);
            //注册消息接收器
            BmobIM.registerDefaultMessageHandler(new MessageHandler(this));
        }
        //uil初始化
        UniversalImageLoader.initImageLoader(this);
    }
}
