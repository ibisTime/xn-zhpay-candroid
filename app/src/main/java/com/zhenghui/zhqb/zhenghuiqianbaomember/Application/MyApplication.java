package com.zhenghui.zhqb.zhenghuiqianbaomember.Application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.xutils.BuildConfig;
import org.xutils.x;

import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by dell1 on 2016/12/12.
 */

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
        initEaseUI();
        initEMChat_kefu();

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



    private void initEaseUI() {
        EaseUI.getInstance().init(this,null);
        EMClient.getInstance().setDebugMode(true);
        EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                return null;
            }
        });
    }

    private void initEMChat_kefu() {
//        ChatClient.Options options = new ChatClient.Options();
//        options.setAppkey("tianleios#zh-dev");//appkey获取地址：kefu.easemob.com，“管理员模式 > 渠道管理 > 手机APP”页面的关联的“AppKey”
//        options.setTenantId("32920");//tenantId获取地址：kefu.easemob.com，“管理员模式 > 设置 > 企业信息”页面的“租户ID”
//
//        // Kefu SDK 初始化
//        if (!ChatClient.getInstance().init(this, options)){
//            return;
//        }
//        // Kefu EaseUI的初始化
//        UIProvider.getInstance().init(this);
//        //后面可以设置其他属性
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

    // 遍历所有Activity并finish
        /*
         * 在每一个Activity中的onCreate方法里添加该Activity到MyApplication对象实例容器中
         *
         * MyApplication.getInstance().addActivity(this);
         *
         * 在需要结束所有Activity的时候调用exit方法
         *
         * MyApplication.getInstance().exit();
         */
    public void exit() {

        for (Activity activity : activityList) {
            if(null != activity){
                activity.finish();
            }
        }

    }

}
