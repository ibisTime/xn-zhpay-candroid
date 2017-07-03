package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.MyShopAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.MyShopModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.hyphenate.easeui.model.EaseDefaultEmojiconDatas.getData;

public class MyShopActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener,AdapterView.OnItemClickListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_shop)
    ListView listShop;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;

    private ArrayList<MyShopModel> list;
    private MyShopAdapter adapter;

    private SharedPreferences appConfigSp;
    private SharedPreferences userInfoSp;

    private int page = 1;
    private int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initListView();
        initRefreshLayout();

        getDatas();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new MyShopAdapter(this,list);

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    private void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("storeCode", "");
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("start", page+"");
            object.put("limit", pageSize+"");
            object.put("status", "1");
            object.put("orderDir", "");
            object.put("orderColumn", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808245", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<MyShopModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<MyShopModel>>() {
                    }.getType());

                    if(page == 1){
                        list.clear();
                    }

                    list.addAll(lists);
                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(MyShopActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(MyShopActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initListView() {
        listShop.setAdapter(adapter);
        listShop.setOnItemClickListener(this);
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
    }




    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                page = 1;
                getData();
                // 更新数据
                // 更新完后调用该方法结束刷新

            }
        }, 1500);
    }

    @Override
    public void onLoad() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setLoading(false);
                page = page + 1;
                getData();
            }
        }, 1500);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        startActivity(new Intent(MyShopActivity.this,MyShopDetailsActivity.class).putExtra("model",list.get(i)));
    }
}
