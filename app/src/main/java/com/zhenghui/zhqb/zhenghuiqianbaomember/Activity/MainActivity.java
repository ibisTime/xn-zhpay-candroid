package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.app.AlertDialog;
import android.content.Context;
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
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.fragment.GoodFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.fragment.MyFragment2;
import com.zhenghui.zhqb.zhenghuiqianbaomember.fragment.ShakeFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.fragment.ShopFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.fragment.TargetFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.PersonalModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.services.JewelRecordService;
import com.zhenghui.zhqb.zhenghuiqianbaomember.services.MyService;
import com.zhenghui.zhqb.zhenghuiqianbaomember.services.UpdateService;
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
    @InjectView(R.id.img_shake)
    ImageView imgShake;
    @InjectView(R.id.txt_shake)
    TextView txtShake;
    @InjectView(R.id.layout_shake)
    LinearLayout layoutShake;
    @InjectView(R.id.img_target)
    ImageView imgTarget;
    @InjectView(R.id.txt_target)
    TextView txtTarget;
    @InjectView(R.id.layout_target)
    LinearLayout layoutTarget;
    @InjectView(R.id.img_my)
    ImageView imgMy;
    @InjectView(R.id.txt_my)
    TextView txtMy;
    @InjectView(R.id.layout_my)
    LinearLayout layoutMy;

    // 内容fragment
    private Fragment shopFragment;
    private Fragment goodFragment;
    private Fragment shakeFragment;
    private Fragment targetFragment;
    private Fragment myFragment;

    private SharedPreferences userInfoSp;

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

        setSelect(2);

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
                if (targetFragment == null) {
                    targetFragment = new TargetFragment();
                    transaction.add(R.id.id_content, targetFragment);
                } else {
                    transaction.show(targetFragment);
                }
                txtTarget.setTextColor(getResources().getColor(R.color.orange));
                imgTarget.setImageResource(R.mipmap.target_orange);
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
        if (shopFragment != null) {
            transaction.hide(shopFragment);
        }
        if (goodFragment != null) {
            transaction.hide(goodFragment);
        }
        if (shakeFragment != null) {
            transaction.hide(shakeFragment);
        }
        if (targetFragment != null) {
            transaction.hide(targetFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    @OnClick({R.id.layout_shop, R.id.layout_good, R.id.layout_shake, R.id.layout_target, R.id.layout_my})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_shop:
                isShake = false;

                resetImgs();
                setSelect(0);
                shakeFragment.onPause();
                break;

            case R.id.layout_good:
                isShake = false;

                resetImgs();
                setSelect(1);
                shakeFragment.onPause();
                break;

            case R.id.layout_shake:
                isShake = true;

                resetImgs();
                setSelect(2);
                shakeFragment.onResume();
                break;

            case R.id.layout_target:
                isShake = false;

                resetImgs();
                setSelect(3);
                shakeFragment.onPause();
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

                break;
        }
    }

    /**
     * 切换图片至暗色
     */
    public void resetImgs() {
        txtShop.setTextColor(getResources().getColor(R.color.gray4d));
        txtGood.setTextColor(getResources().getColor(R.color.gray4d));
        txtShake.setTextColor(getResources().getColor(R.color.gray4d));
        txtTarget.setTextColor(getResources().getColor(R.color.gray4d));
        txtMy.setTextColor(getResources().getColor(R.color.gray4d));

        imgShop.setImageResource(R.mipmap.shop_gray);
        imgGood.setImageResource(R.mipmap.good_gray);
        imgShake.setImageResource(R.mipmap.shake_gray);
        imgTarget.setImageResource(R.mipmap.target_gray);
        imgMy.setImageResource(R.mipmap.my_gray);
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            tip();
        }
        return false;
    }

    private void tip() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("您确定要退出正汇钱包吗?")
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
    }

    /**
     * 退出登录
     */
    private void logOut() {
        System.out.println("logOut()");
        RequestParams params = new RequestParams(Xutil.URL + Xutil.LOGOUT);
        params.addBodyParameter("token", userInfoSp.getString("token", null));

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {

                System.out.println("result=" + result);

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
                System.out.println("onError="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                System.out.println("logOut___________onFinished()");
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


        new Xutil().post("805056", object.toString(), new Xutil.XUtils3CallBackPost() {
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
            object.put("key", "cVersionCode");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("615917", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    int versionCode = Integer.parseInt(jsonObject.getString("cvalue"));

                    if (versionCode > getVersionCode()) {
                        update();
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

    private void update() {
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("发现新版本请及时更新")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        startService(new Intent(MainActivity.this, UpdateService.class)
                                .putExtra("appname", "zhqb-release")
                                .putExtra("appurl", "http://m.zhenghuijituan.com/app/zhqb-release.apk"));

                    }
                }).setNegativeButton("取消", null).show();
    }


}
