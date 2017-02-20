package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.MyDuoBaoNumModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MyDuoBaoNumActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_name)
    TextView txtName;
    @InjectView(R.id.txt_info)
    TextView txtInfo;
    @InjectView(R.id.gridview_number)
    GridView gridviewNumber;

    String code;
    String name;
    private SharedPreferences userInfoSp;

    private MyDuoBaoNumModel model;

    private List<Map<String, Object>> data_list;
    private SimpleAdapter adapter;

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_duo_bao_num);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        getDatas();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        code = getIntent().getStringExtra("code");
        name = getIntent().getStringExtra("name");

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        data_list = new ArrayList<Map<String, Object>>();
        //新建适配器
        String[] from = {"number"};
        int[] to = {R.id.txt_number};
        adapter = new SimpleAdapter(this, data_list, R.layout.item_duobao_num, from, to);
        gridviewNumber.setAdapter(adapter);

    }

    private void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("recordCode", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("808314", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<MyDuoBaoNumModel>() {
                    }.getType());

                    setView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(MyDuoBaoNumActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(MyDuoBaoNumActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {

        txtName.setText(name);

        for (int i = 0; i < model.getJewelRecordNumberList().size(); i++) {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("number", model.getJewelRecordNumberList().get(i).getNumber());
            data_list.add(map);

        }

        adapter.notifyDataSetChanged();
    }

}
