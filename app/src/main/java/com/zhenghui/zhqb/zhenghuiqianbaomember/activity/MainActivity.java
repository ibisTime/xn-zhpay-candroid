package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.fragment.GiftFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.fragment.GoodFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.fragment.MyFragment2;
import com.zhenghui.zhqb.zhenghuiqianbaomember.fragment.ShopFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.PersonalModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.services.JewelRecordService;
import com.zhenghui.zhqb.zhenghuiqianbaomember.services.MyService;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.LoginUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_805056;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_807718;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.UpdateUtil.startWeb;

public class MainActivity extends MyBaseActivity {

    @InjectView(R.id.id_content)
    FrameLayout idContent;
    @InjectView(R.id.img_shop)
    ImageView imgShop;
    @InjectView(R.id.txt_shop)
    TextView txtShop;
    @InjectView(R.id.layout_shop)
    LinearLayout layoutShop;
    @InjectView(R.id.img_good)
    ImageView imgGood;
    @InjectView(R.id.txt_good)
    TextView txtGood;
    @InjectView(R.id.layout_good)
    LinearLayout layoutGood;
    @InjectView(R.id.img_gift)
    ImageView imgGift;
    @InjectView(R.id.txt_gift)
    TextView txtGift;
    @InjectView(R.id.layout_gift)
    LinearLayout layoutGift;
    @InjectView(R.id.img_my)
    ImageView imgMy;
    @InjectView(R.id.txt_my)
    TextView txtMy;
    @InjectView(R.id.layout_my)
    LinearLayout layoutMy;

    // 内容fragment
    private Fragment shopFragment;
    private Fragment goodFragment;
    private Fragment giftFragment;
    private Fragment myFragment;


    public static MainActivity instance;

    public boolean isShake = true;

    private PersonalModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        setSelect(0);

        if (userInfoSp.getString("userId", null) !=null){
            getData();
        }
        getVersion();
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

        // 启动后台定时上传经纬度
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
                if (shopFragment == null) {
                    shopFragment = new ShopFragment();
                    transaction.add(R.id.id_content, shopFragment);
                } else {
                    transaction.show(shopFragment);
                }
                txtShop.setTextColor(getResources().getColor(R.color.fontColor_orange));
                imgShop.setImageResource(R.mipmap.shop_orange);
                break;
            case 1:
                if (goodFragment == null) {
                    goodFragment = new GoodFragment();
                    transaction.add(R.id.id_content, goodFragment);
                } else {
                    transaction.show(goodFragment);

                }
                txtGood.setTextColor(getResources().getColor(R.color.fontColor_orange));
                imgGood.setImageResource(R.mipmap.good_orange);
                break;

            case 2:
                if (giftFragment == null) {
                    giftFragment = new GiftFragment();
                    transaction.add(R.id.id_content, giftFragment);
                } else {
                    transaction.show(giftFragment);
                }
                txtGift.setTextColor(getResources().getColor(R.color.fontColor_orange));
                imgGift.setImageResource(R.mipmap.shake_orange);
                break;

            case 3:
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
        if (shopFragment != null) {
            transaction.hide(shopFragment);
        }
        if (goodFragment != null) {
            transaction.hide(goodFragment);
        }
        if (giftFragment != null) {
            transaction.hide(giftFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    @OnClick({R.id.layout_shop, R.id.layout_good, R.id.layout_gift, R.id.layout_my})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_shop:
                isShake = false;

                resetImgs();
                setSelect(0);
                break;

            case R.id.layout_good:
                isShake = false;

                resetImgs();
                setSelect(1);
                break;

            case R.id.layout_gift:
                isShake = true;

                resetImgs();
                setSelect(2);
                break;


            case R.id.layout_my:
                if (userInfoSp.getString("userId", null) != null) {
                    isShake = false;

                    resetImgs();
                    setSelect(3);
                } else {
                    LoginUtil.toLogin(MainActivity.this);
                }

                break;
        }
    }

    /**
     * 切换图片至暗色
     */
    public void resetImgs() {
        txtShop.setTextColor(getResources().getColor(R.color.gray4d));
        txtGood.setTextColor(getResources().getColor(R.color.gray4d));
        txtGift.setTextColor(getResources().getColor(R.color.gray4d));
        txtMy.setTextColor(getResources().getColor(R.color.gray4d));

        imgShop.setImageResource(R.mipmap.shop_gray);
        imgGood.setImageResource(R.mipmap.good_gray);
        imgGift.setImageResource(R.mipmap.shake_gray);
        imgMy.setImageResource(R.mipmap.my_gray);
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            tip();
        }
        return false;
    }

    private void tip() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("您确定要退出"+getString(R.string.app_name)+"吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (userInfoSp.getString("userId", null) != null) {
                            logOut();
                        } else {
                            finish();
                            System.exit(0);
                        }
                    }
                }).setNegativeButton("取消", null).show();

//        update("下载","wwww.baidu.com","1");
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

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getJSONObject("data").getBoolean("isSuccess")) {
                        SharedPreferences.Editor editor = userInfoSp.edit();
                        editor.putString("userId", null);
                        editor.putString("token", null);
                        editor.commit();

                        finish();
                        System.exit(0);
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

    /**
     * 获取用户详情
     */
    private void getData() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(CODE_805056, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<PersonalModel>() {
                    }.getType());

                    SharedPreferences.Editor editor = userInfoSp.edit();
                    editor.putString("mobile", model.getMobile());
                    editor.putString("realName", model.getRealName());
                    editor.putString("nickName", model.getNickname());
                    editor.putString("isGxz", model.getIsGxz());
                    editor.putString("identityFlag", model.getIdentityFlag());
                    editor.putString("tradepwdFlag", model.getTradepwdFlag());
                    editor.putString("userRefereeName", model.getUserRefereeName());
                    if (null != model.getUserExt().getPhoto()) {
                        editor.putString("photo", model.getUserExt().getPhoto());
                    }
                    String address = model.getUserExt().getProvince() + model.getUserExt().getCity() + model.getUserExt().getArea();
                    editor.putString("address", address);
                    editor.commit();


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

    private void getVersion() {
        JSONObject object = new JSONObject();
        try {
            object.put("type", "android_c");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_807718, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    String note = jsonObject.getString("note");
                    String url = jsonObject.getString("downloadUrl");
                    String force = jsonObject.getString("forceUpdate");
                    int versionCode = Integer.parseInt(jsonObject.getString("version"));

                    if (versionCode > getVersionCode()) {
                        update(note, url, force);
                    }

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

    private void update(String msg, final String url, String force) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("提示")
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        startWeb(MainActivity.this,url);

                        finish();
                        System.exit(0);

                    }
                })
                .setCancelable(false);


        if(force.equals("1")){ // 强制更新
            builder.show();
        }else {
            builder.setNegativeButton("取消", null).show();
        }
    }




}
