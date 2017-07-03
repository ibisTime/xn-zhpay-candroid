package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

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
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.RightsAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.RightsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RightsActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener,RefreshLayout.OnLoadListener,AdapterView.OnItemClickListener {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_rights)
    ListView listRights;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;

    List<RightsModel> list;
    RightsAdapter adapter;

    private int page = 1;
    private int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rights);
        ButterKnife.inject(this);

        inits();
        initsListView();
        initRefreshLayout();

        getData();
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new RightsAdapter(this,list);
    }

    private void initsListView() {
        listRights.setAdapter(adapter);
        listRights.setOnItemClickListener(this);
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnLoadListener(this);
        swipeContainer.setOnRefreshListener(this);
    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }


    private void getData() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808417", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new Gson();
                    ArrayList<RightsModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<RightsModel>>() {
                    }.getType());

                    list.clear();
                    list.addAll(lists);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(RightsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RightsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
//                page = 1;
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
//                page = page + 1;
                getData();


            }
        }, 1500);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        startActivity(new Intent(RightsActivity.this, RightsListActivity.class).putExtra("code",list.get(i).getCode()));
    }
}
