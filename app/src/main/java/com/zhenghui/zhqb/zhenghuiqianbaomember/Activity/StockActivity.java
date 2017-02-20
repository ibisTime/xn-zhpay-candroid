package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment.BuyStockFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment.MyStockFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StockActivity extends MyBaseActivity {

    @InjectView(R.id.layout_fragment)
    LinearLayout layoutFragment;

    private MyStockFragment myStockFragment;
    private BuyStockFragment buyStockFragment;

    private String code;
    private boolean isBuy;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inis();
        getMyStock();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inis() {
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);

    }

    public void getMyStock(){
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808406", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("{}") ){
//                    Toast.makeText(StockActivity.this, "用户没有购买过福利月卡", Toast.LENGTH_SHORT).show();
                    isBuy = false;
                    initFragment();
                }else{
                    isBuy = true;
                    initFragment();
                }

                System.out.println("result="+result);

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(StockActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(StockActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        if (isBuy) { // 已买股票
            if(myStockFragment == null){
                code = getIntent().getStringExtra("code");
                myStockFragment = MyStockFragment.newInstance(code);
                transaction.add(R.id.layout_fragment,myStockFragment);
            }else{
                transaction.show(myStockFragment);
            }


        }else{ // 未买股票
            if(buyStockFragment == null){
                buyStockFragment = new BuyStockFragment();
                transaction.add(R.id.layout_fragment,buyStockFragment);
            }else{
                transaction.show(buyStockFragment);
            }

        }

        transaction.commit();

    }

    private void hideFragment(FragmentTransaction transaction){
        if (myStockFragment != null) {
            transaction.hide(myStockFragment);
        }
        if (buyStockFragment != null) {
            transaction.hide(buyStockFragment);
        }
    }
}
