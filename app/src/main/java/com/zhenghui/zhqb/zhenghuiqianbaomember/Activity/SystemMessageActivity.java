package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.SystemMessageAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.MessageModel;
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

public class SystemMessageActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_message)
    ListView listMessage;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;
    private List<MessageModel> list;
    private SystemMessageAdapter adapter;

    private int page = 1;
    private int pageSize = 10;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initListView();
        initRefreshLayout();
        getList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }


    private void inits() {
        list = new ArrayList();
        adapter = new SystemMessageAdapter(this, list);


        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    private void initListView() {
        listMessage.setAdapter(adapter);
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
    }

    private void getList() {

        JSONObject object = new JSONObject();
        try {
            object.put("fromSystemCode", appConfigSp.getString("systemCode",null));
            object.put("channelType", "4");
            object.put("pushType", "");
            object.put("toSystemCode", appConfigSp.getString("systemCode",null));
            object.put("toKind", "1");
            object.put("toMobile", "");
            object.put("status", "1");
            object.put("smsType", "");
            object.put("start", page);
            object.put("limit", pageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("804040", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<MessageModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<MessageModel>>() {
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
                Toast.makeText(SystemMessageActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(SystemMessageActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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
                getList();
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
                getList();
            }
        }, 1500);
    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }
}
