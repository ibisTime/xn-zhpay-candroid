package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.MyJewelHistoryAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.MyJewelHistoreModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MyJewelHistoryActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_histoty)
    ListView listHistoty;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;

    private MyJewelHistoryAdapter adapter;
    private List<MyJewelHistoreModel> list;

    private int page = 1;
    private int pageSize = 10;

    private String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jewel_history);
        ButterKnife.inject(this);
        inits();
        initRefreshLayout();
        initListView();
        getData();

    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new MyJewelHistoryAdapter(this, list);


    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
    }

    private void initListView() {
        listHistoty.setAdapter(adapter);
        listHistoty.setOnItemClickListener(this);
    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }

    private void getData() {

        JSONObject object = new JSONObject();
        try {
            object.put("jewelStatus", "");
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("orderDir", "");
            object.put("orderColumn", "");
            object.put("userId", userInfoSp.getString("userId", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808313", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    List<MyJewelHistoreModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<MyJewelHistoreModel>>() {
                    }.getType());

                    if (page == 1) {
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
                Toast.makeText(MyJewelHistoryActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(MyJewelHistoryActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                page = 1;
                getData();

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

        if(i > 2){
            if(i%3 == 0){
                color = "orange";
            }else if(i%3 == 1){
                color = "yellow";
            }else if(i%3 == 2) {
                color = "green";
            }
        }else {
            if(i == 0){
                color = "orange";
            }else if(i == 1){
                color = "yellow";
            }else if(i == 2) {
                color = "green";
            }
        }

        startActivity(new Intent(MyJewelHistoryActivity.this, MyDuoBaoNumActivity.class)
                .putExtra("winNumber", list.get(i).getJewel().getWinNumber())
                .putExtra("model", list.get(i).getJewel())
                .putExtra("color", color)
                .putExtra("code", list.get(i).getJewel().getCode()));

    }

}
