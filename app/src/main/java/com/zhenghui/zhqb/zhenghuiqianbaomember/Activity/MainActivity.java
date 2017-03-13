package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment.ChatFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment.GiveFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment.MyFragment2;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment.ShakeFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment.TargetFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.FriendModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.PersonalModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.services.JewelRecordService;
import com.zhenghui.zhqb.zhenghuiqianbaomember.services.MyService;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.LoginUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends MyBaseActivity implements EMMessageListener {


    @InjectView(R.id.id_content)
    FrameLayout idContent;
    @InjectView(R.id.img_target)
    ImageView imgTarget;
    @InjectView(R.id.txt_target)
    TextView txtTarget;
    @InjectView(R.id.layout_target)
    LinearLayout layoutTarget;
    @InjectView(R.id.img_give)
    ImageView imgGive;
    @InjectView(R.id.txt_give)
    TextView txtGive;
    @InjectView(R.id.layout_give)
    LinearLayout layoutGive;
    @InjectView(R.id.img_shake)
    ImageView imgShake;
    @InjectView(R.id.txt_shake)
    TextView txtShake;
    @InjectView(R.id.layout_shake)
    LinearLayout layoutShake;
    @InjectView(R.id.img_chat)
    ImageView imgChat;
    @InjectView(R.id.txt_point)
    TextView txtPoint;
    @InjectView(R.id.txt_chat)
    TextView txtChat;
    @InjectView(R.id.layout_chat)
    LinearLayout layoutChat;
    @InjectView(R.id.img_my)
    ImageView imgMy;
    @InjectView(R.id.txt_my)
    TextView txtMy;
    @InjectView(R.id.layout_my)
    LinearLayout layoutMy;

    // 内容fragment
    private Fragment targetFragment;
    private Fragment giveFragment;
    private Fragment shakeFragment;
    private ChatFragment chatFragment;
    private Fragment myFragment;

    private SharedPreferences userInfoSp;

    public static MainActivity instance;

    public boolean isShake = true;

    private boolean logoutFlag = false;
    private boolean anotherDeviceFlag = true;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:

                    if (anotherDeviceFlag) {
                        SharedPreferences.Editor editor = userInfoSp.edit();
                        editor.putString("userId", null);
                        editor.putString("token", null);
                        editor.commit();

                        stopService(new Intent(MainActivity.this, MyService.class));

                        // 实例化Intent
                        Intent intent = new Intent();
                        // 设置Intent的action属性
                        intent.setAction("com.zhenghui.zhqb.zhenghuiqianbaomember.receiver.LogoutReceiver");
                        // 发出广播
                        sendBroadcast(intent);
                        System.out.println("sendBroadcast");
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();

        setSelect(2);

        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
        stopService(new Intent(MainActivity.this, MyService.class));
        stopService(new Intent(MainActivity.this, JewelRecordService.class));
    }

    private void inits() {
        instance = this;

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        Intent startIntent = new Intent(this, MyService.class);
        startService(startIntent);
    }

    /**
     * 选择Fragment
     *
     * @param i
     */
    public void setSelect(int i) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        // 把图片设置为亮的
        // 设置内容区域
        switch (i) {
            case 0:
                if (targetFragment == null) {
                    targetFragment = new TargetFragment();
                    transaction.add(R.id.id_content, targetFragment);
                } else {
                    transaction.show(targetFragment);
                }
                txtTarget.setTextColor(getResources().getColor(R.color.fontColor_orange));
                imgTarget.setImageResource(R.mipmap.target_orange);
                break;
            case 1:
                if (giveFragment == null) {
                    giveFragment = new GiveFragment();
                    transaction.add(R.id.id_content, giveFragment);
                } else {
                    transaction.show(giveFragment);

                }
                txtGive.setTextColor(getResources().getColor(R.color.fontColor_orange));
                imgGive.setImageResource(R.mipmap.give_orange);
                break;
            case 2:
                if (shakeFragment == null) {
                    shakeFragment = new ShakeFragment();
                    transaction.add(R.id.id_content, shakeFragment);
                } else {
                    transaction.show(shakeFragment);
                }
                txtShake.setTextColor(getResources().getColor(R.color.fontColor_orange));
                imgShake.setImageResource(R.mipmap.shake_orange);
                break;

            case 3:
                if (chatFragment == null) {
                    chatFragment = new ChatFragment();
                    transaction.add(R.id.id_content, chatFragment);
                } else {
                    transaction.show(chatFragment);
                }
                txtChat.setTextColor(getResources().getColor(R.color.orange));
                imgChat.setImageResource(R.mipmap.chat_orange);
                break;


            case 4:
                if (myFragment == null) {
                    myFragment = new MyFragment2();
                    transaction.add(R.id.id_content, myFragment);
                } else {
                    transaction.show(myFragment);
                }
                txtMy.setTextColor(getResources().getColor(R.color.fontColor_orange));
                imgMy.setImageResource(R.mipmap.my_orange);
                break;

            default:
                break;
        }

        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (targetFragment != null) {
            transaction.hide(targetFragment);
        }
        if (giveFragment != null) {
            transaction.hide(giveFragment);
        }
        if (shakeFragment != null) {
            transaction.hide(shakeFragment);
        }
        if (chatFragment != null) {
            transaction.hide(chatFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    @OnClick({R.id.layout_target, R.id.layout_give, R.id.layout_shake, R.id.layout_chat, R.id.layout_my})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_target:
                isShake = false;

                resetImgs();
                setSelect(0);
                shakeFragment.onPause();
                break;

            case R.id.layout_give:
                if (userInfoSp.getString("userId", null) != null) {
                    isShake = false;

                    resetImgs();
                    setSelect(1);
                    shakeFragment.onPause();
                } else {
                    LoginUtil.toLogin(MainActivity.this);
                }
                break;

            case R.id.layout_shake:
                isShake = true;

                resetImgs();
                setSelect(2);
                shakeFragment.onResume();
                break;

            case R.id.layout_chat:
                if (userInfoSp.getString("userId", null) != null) {
                    isShake = false;

                    resetImgs();
                    setSelect(3);
                    shakeFragment.onPause();
                } else {
                    LoginUtil.toLogin(MainActivity.this);
                }
                break;

            case R.id.layout_my:

                if (userInfoSp.getString("userId", null) != null) {
                    isShake = false;

                    resetImgs();
                    setSelect(4);
                    shakeFragment.onPause();
                } else {
                    LoginUtil.toLogin(MainActivity.this);
                }

//                if(userInfoSp.getString("userId",null) == null){
//                    Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
//                }else{
//
//                }

                break;
        }
    }

    /**
     * 切换图片至暗色
     */
    public void resetImgs() {
        txtTarget.setTextColor(getResources().getColor(R.color.gray4d));
        txtGive.setTextColor(getResources().getColor(R.color.gray4d));
        txtShake.setTextColor(getResources().getColor(R.color.gray4d));
        txtChat.setTextColor(getResources().getColor(R.color.gray4d));
        txtMy.setTextColor(getResources().getColor(R.color.gray4d));

        imgTarget.setImageResource(R.mipmap.target_gray);
        imgGive.setImageResource(R.mipmap.give_gray);
        imgShake.setImageResource(R.mipmap.shake_gray);
        imgChat.setImageResource(R.mipmap.chat_gray);
        imgMy.setImageResource(R.mipmap.my_gray);
    }


    private PersonalModel model;


    public void getFriend() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805157", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new Gson();
                    List<FriendModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<FriendModel>>() {
                    }.getType());

                    checkUnread(lists);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(MainActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkUnread(List<FriendModel> lists) {

        for (FriendModel model : lists) {
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(model.getUserId());
            if (conversation != null) {
                if (conversation.getUnreadMsgCount() > 0) {
                    txtPoint.setVisibility(View.VISIBLE);
                } else {
                    txtPoint.setVisibility(View.GONE);
                }
            }

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userInfoSp.getString("userId", null) != null) {
            addMessageListener();
            getFriend();
        }

    }

    private void addMessageListener() {
        EaseUI.getInstance().pushActivity(this);
        EMClient.getInstance().chatManager().addMessageListener(this);
    }

    @Override
    public void onMessageReceived(List<EMMessage> list) {

        getFriend();
        if (chatFragment.getUserVisibleHint()) {
            chatFragment.serviceUnReade();
        }
        for (EMMessage message : list) {
//            EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
            EaseUI.getInstance().getNotifier().onNewMsg(message);
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    System.out.println("EMConnectionListener error=" + error);

                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                        Toast.makeText(MainActivity.this, "帐号已被移除", Toast.LENGTH_SHORT).show();
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        if (anotherDeviceFlag) {
                            anotherDeviceFlag = false;
                            // 显示帐号在其他设备登录
                            logout();
                        }
                    } else {

//                        if(userInfoSp.getString("userId",null) != null){
//
//                            System.out.println("NetUtils.hasNetwork(MainActivity.this)="+NetUtils.hasNetwork(MainActivity.this));
//
//                            if (NetUtils.hasNetwork(MainActivity.this)){
//                                //连接不到聊天服务器
//                                Toast.makeText(MainActivity.this, "连接不到聊天服务器", Toast.LENGTH_SHORT).show();
//                            } else {
//                                //当前网络不可用，请检查网络设置
//                                Toast.makeText(MainActivity.this, "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
//                            }
//                        }

                    }
                }
            });
        }
    }

    private void logout() {

        EMClient.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                if (anotherDeviceFlag) {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }

            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
                System.out.println("logout()------>onError()");
                System.out.println("code=" + code);
                System.out.println("message=" + message);
            }
        });
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            exitBy2Click(); //调用双击退出函数
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            if(userInfoSp.getString("userId",null) != null){
                logOut();
            }else{
                finish();
                System.exit(0);
            }

        }
    }

    /**
     * 退出登录
     */
    private void logOut() {

        RequestParams params = new RequestParams(Xutil.URL + Xutil.LOGOUT);
        params.addBodyParameter("token", userInfoSp.getString("token", null));

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {

                System.out.println("result="+result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getJSONObject("data").getBoolean("isSuccess")){
                        finish();
                        System.exit(0);
                    }else{

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }
}
