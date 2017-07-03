package com.zhenghui.zhqb.zhenghuiqianbaomember.util;

import android.content.Context;
import android.content.Intent;

import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.LoginActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;

public class LoginUtil {


    public static void toLogin(Context context){

        context.startActivity(new Intent(context, LoginActivity.class).putExtra("log","tip"));
    }

    public static void exitActivityAndLogin(Context context){
        MyApplication.getInstance().exit();
        context.startActivity(new Intent(context, LoginActivity.class).putExtra("log","tip"));
    }


}
