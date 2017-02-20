package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.DiscountShopAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.ShopDetailsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DiscountStoreActivity extends MyBaseActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_discount)
    ListView listDiscount;

    private String code;
    private SharedPreferences userInfoSp;

    private ShopDetailsModel model;
    private List<ShopDetailsModel.StoreTicketsBean> list;
    private DiscountShopAdapter adapter;

    private String name = "";
    private String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_store);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDatas();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        list = new ArrayList<>();


        code = getIntent().getStringExtra("code");
        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

    }

    private void initEvent() {
        listDiscount.setOnItemClickListener(this);
    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }

    private void getDatas(){
        JSONObject object = new JSONObject();
        try {
            object.put("type","");
            object.put("name","");
            object.put("storeCode",code);
            object.put("status","1");
            object.put("token",userInfoSp.getString("token",null));
            object.put("userId", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("808225", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray jsonObject = new JSONArray(result);

                    Gson gson = new Gson();
                    List<ShopDetailsModel.StoreTicketsBean> lists = gson.fromJson(jsonObject.toString(), new TypeToken<ArrayList<ShopDetailsModel.StoreTicketsBean>>(){}.getType());

                    list.clear();
                    list.addAll(lists);
                    adapter= new DiscountShopAdapter(DiscountStoreActivity.this,list,name,phone);
                    listDiscount.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(DiscountStoreActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DiscountStoreActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(list.get(i).getStatus().equals("1")){
            startActivity(new Intent(DiscountStoreActivity.this, DiscountDetailActivity.class)
                    .putExtra("isStore",true)
                    .putExtra("isBuy",true)
                    .putExtra("name",name)
                    .putExtra("phone",phone)
                    .putExtra("code",list.get(i).getCode()));
        }else{
            startActivity(new Intent(DiscountStoreActivity.this, DiscountDetailActivity.class)
                    .putExtra("isStore",true)
                    .putExtra("isBuy",false)
                    .putExtra("name",name)
                    .putExtra("phone",phone)
                    .putExtra("code",list.get(i).getCode()));
        }
    }


}
