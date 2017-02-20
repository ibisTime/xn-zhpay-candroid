package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

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
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.MyDuoBaoAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.MyDuoBaoModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MyDuoBaoActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.line_all)
    View lineAll;
    @InjectView(R.id.layout_all)
    LinearLayout layoutAll;
    @InjectView(R.id.line_underway)
    View lineUnderway;
    @InjectView(R.id.layout_underway)
    LinearLayout layoutUnderway;
    @InjectView(R.id.line_end)
    View lineEnd;
    @InjectView(R.id.layout_end)
    LinearLayout layoutEnd;
    @InjectView(R.id.line_lucky)
    View lineLucky;
    @InjectView(R.id.layout_lucky)
    LinearLayout layoutLucky;
    @InjectView(R.id.list_shop)
    ListView listShop;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;


    private ArrayList<MyDuoBaoModel> list;
    private MyDuoBaoAdapter adapter;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private String status = "all";

    private int page = 1;
    private int pageSize = 10;

    private boolean isLucky = false;
    private int luckyPage = 1;
    private int luckyPageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_duo_bao);
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
        adapter = new MyDuoBaoAdapter(this, list, isLucky);

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    private void initListView() {
        listShop.setOnItemClickListener(this);
        listShop.setAdapter(adapter);
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
    }


    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                if(isLucky){
                    luckyPage = 1;
                    getLucky();
                }else{
                    page = 1;
                    getDatas();
                }



            }
        }, 1500);
    }

    @Override
    public void onLoad() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setLoading(false);
                if(isLucky){
                    luckyPage = luckyPage + 1;
                    getLucky();
                }else{
                    page = page + 1;
                    getDatas();
                }


            }
        }, 1500);
    }

    @OnClick({R.id.layout_back, R.id.layout_all, R.id.layout_underway, R.id.layout_end, R.id.layout_lucky})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_all:
                initBtn();
                lineAll.setVisibility(View.VISIBLE);
                layoutAll.setBackgroundColor(getResources().getColor(R.color.white));
                page = 1;
                status = "all";
                isLucky = false;
                getDatas();
                break;

            case R.id.layout_underway:
                initBtn();
                lineUnderway.setVisibility(View.VISIBLE);
                layoutUnderway.setBackgroundColor(getResources().getColor(R.color.white));
                page = 1;
                status = "3";
                isLucky = false;
                getDatas();
                break;

            case R.id.layout_end:
                initBtn();
                lineEnd.setVisibility(View.VISIBLE);
                layoutEnd.setBackgroundColor(getResources().getColor(R.color.white));
                page = 1;
                status = "7";
                isLucky = false;
                getDatas();
                break;

            case R.id.layout_lucky:
                initBtn();
                lineLucky.setVisibility(View.VISIBLE);
                layoutLucky.setBackgroundColor(getResources().getColor(R.color.white));

                luckyPage = 1;
                isLucky = true;
                getLucky();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(isLucky){
            if (list.get(i).getStatus().equals("6")){ // 已签收
                evaluate(i);
                return;
            }

            startActivity(new Intent(MyDuoBaoActivity.this, DuoBaoWinActivity.class).putExtra("code",list.get(i).getCode()));
        }
    }

    private void initBtn() {
        layoutEnd.setBackgroundColor(getResources().getColor(R.color.grayfa));
        layoutAll.setBackgroundColor(getResources().getColor(R.color.grayfa));
        layoutLucky.setBackgroundColor(getResources().getColor(R.color.grayfa));
        layoutUnderway.setBackgroundColor(getResources().getColor(R.color.grayfa));

        lineAll.setVisibility(View.INVISIBLE);
        lineEnd.setVisibility(View.INVISIBLE);
        lineLucky.setVisibility(View.INVISIBLE);
        lineUnderway.setVisibility(View.INVISIBLE);
    }

    private void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("jewelStatus", status);
            object.put("orderDir", "");
            object.put("orderColumn", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808313", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<MyDuoBaoModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<MyDuoBaoModel>>() {
                    }.getType());

                    if (page == 1) {
                        list.clear();
                    }

                    list.addAll(lists);
                    adapter = new MyDuoBaoAdapter(MyDuoBaoActivity.this, list, false);
                    listShop.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(MyDuoBaoActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(MyDuoBaoActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getLucky() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("start", luckyPage);
            object.put("limit", luckyPageSize);
            object.put("orderDir", "");
            object.put("orderColumn", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808316", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<MyDuoBaoModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<MyDuoBaoModel>>() {
                    }.getType());

                    if (luckyPage == 1) {
                        list.clear();
                    }

                    list.addAll(lists);
                    adapter = new MyDuoBaoAdapter(MyDuoBaoActivity.this, list, true);
                    listShop.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(MyDuoBaoActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(MyDuoBaoActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void evaluate(int position) {

        JSONObject object = new JSONObject();
        try {
            object.put("interacter",userInfoSp.getString("userId",null));
            object.put("jewelCode",list.get(position).getJewel().getCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808071",object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(MyDuoBaoActivity.this, "评价成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(MyDuoBaoActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(MyDuoBaoActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
