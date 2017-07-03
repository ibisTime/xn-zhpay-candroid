package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class BankCardListActivity extends AppCompatActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_bank)
    ListView listBank;

    private int page = 1;
    private int pageSize = 10;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card_list);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }

    private void getList() {

        JSONObject object = new JSONObject();
        try {
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("token", userInfoSp.getString("token", null));
            object.put("bankcardNumber", "");
            object.put("bankName", "");
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("realName", "");
            object.put("type", "");
            object.put("status", "1");
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("orderColumn", "");
            object.put("orderDir", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("802116", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {


//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//
//                    System.out.println("jsonObject.getJSONArray(\"list\").toString()="+jsonObject.getJSONArray("list").toString());
//
//                    Gson gson = new Gson();
//                    ArrayList<GoodsModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<GoodsModel>>() {
//                    }.getType());
//
//                    if (page == 1) {
//                        list.clear();
//                    }
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(BankCardListActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(BankCardListActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
