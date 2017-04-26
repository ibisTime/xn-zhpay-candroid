package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment.BuyTreeFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment.MyTreeFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TreeActivity extends MyBaseActivity {

    @InjectView(R.id.layout_fragment)
    LinearLayout lyaoutFragment;

    private MyTreeFragment myTreeFragment;
    private BuyTreeFragment buyTreeFragment;

    private boolean isBuy;
    private String treeDetail;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyTree();
    }

    private void inis() {
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);


    }

    public void getMyTree(){
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("615118", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("[]")){
                    isBuy = false;
                    initFragment();
                    return;
                }else {
                    isBuy = true;
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        treeDetail = jsonArray.get(0).toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    initFragment();
                    return;
                }

            }

            @Override
            public void onTip(String tip) {

                Toast.makeText(TreeActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(TreeActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        if (isBuy) { // 已买摇钱树
            if(myTreeFragment == null){
                myTreeFragment = MyTreeFragment.newInstance(treeDetail);
                transaction.add(R.id.layout_fragment, myTreeFragment);
            }else{
                transaction.show(myTreeFragment);
            }

        } else { // 未买摇钱树
            if(buyTreeFragment == null){
                buyTreeFragment = new BuyTreeFragment();
                transaction.add(R.id.layout_fragment, buyTreeFragment);
            }else{
                transaction.show(buyTreeFragment);
            }

        }

        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction){
        if (myTreeFragment != null) {
            transaction.hide(myTreeFragment);
        }
        if (buyTreeFragment != null) {
            transaction.hide(buyTreeFragment);
        }
    }



}
