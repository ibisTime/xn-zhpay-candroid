package com.zhenghui.zhqb.zhenghuiqianbaomember.util;

import android.content.Context;
import android.content.Intent;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.LoginActivity;

/**
 * Created by dell1 on 2016/12/15.
 */

public class LoginUtil {


    public static void toLogin(Context context){

        context.startActivity(new Intent(context, LoginActivity.class).putExtra("log","tip"));
    }
}
