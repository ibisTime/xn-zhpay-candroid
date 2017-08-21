package com.zhenghui.zhqb.zhenghuiqianbaomember.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.xutils.BuildConfig;
import org.xutils.x;

import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {

    private List<Activity> activityList = new LinkedList<Activity>();

    public static Context applicationContext;
    private static MyApplication instance;

    private Activity activity;

    private SharedPreferences preferences;

    public MyApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
        initAppConfig();

        initXUtil();
        initJpush();
        initZXing();

    }



    @Override
    public void onTerminate() {
        // 程序终止的时候执行

        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initAppConfig() {
        preferences = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();// 获取编辑器
        editor.putString("systemCode", "CD-CZH000001");
        editor.commit();
    }

    /**
     * 初始化xUtil
     */
    private void initXUtil() {
        x.Ext.init(this);
        // 开启debug会影响性能
        x.Ext.setDebug(BuildConfig.DEBUG);
    }

    /**
     * 初始化极光
     */
    private void initJpush(){
        JPushInterface.init(applicationContext);
        JPushInterface.setDebugMode(true);
        JPushInterface.setLatestNotificationNumber(this, 3);
    }

    private void initZXing() {
        ZXingLibrary.initDisplayOpinion(this);
    }

    // 单例模式中获取唯一的MyApplication实例
    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;

    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 添加Activity到容器中
    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }


    /**
     * 在每一个Activity中的onCreate方法里添加该Activity到MyApplication对象实例容器中
     *
     * MyApplication.getInstance().addActivity(this);
     *
     * 在需要结束所有Activity的时候调用exit方法
     *
     * MyApplication.getInstance().exit();
     */
    public void exit() {
        // 遍历所有Activity并finish
        for (Activity activity : activityList) {
            if(null != activity){
                activity.finish();
            }
        }

    }

}
