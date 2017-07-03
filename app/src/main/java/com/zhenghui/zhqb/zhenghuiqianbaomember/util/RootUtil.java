package com.zhenghui.zhqb.zhenghuiqianbaomember.util;

import android.provider.DocumentsContract;

import java.io.File;


public class RootUtil {
    private static String LOG_TAG = DocumentsContract.Root.class.getName();

    public static boolean isDeviceRooted() {
        if (checkRootMethod1()){return true;}
        if (checkRootMethod2()){return true;}
        if (checkRootMethod3()){return true;}
        return false;
    }
    public static boolean checkRootMethod1(){
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }
        return false;
    }
    public static boolean checkRootMethod2(){
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e) { }
        return false;
    }

    public static boolean checkRootMethod3() {
        if (new ExecShell().executeCommand(ExecShell.SHELL_CMD.check_su_binary) != null){
            return true;
        }else{
            return false;
        }
    }
}

