package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.GiveAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.GiveModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class GiveHistoryActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.list_give)
    ListView listGive;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;
    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;

    private GiveAdapter adapter;
    private List<GiveModel> list;

    private int page = 1;
    private int pageSize = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_history);
        ButterKnife.inject(this);

        inits();
        initRefreshLayout();
        initListView();

        getData();

    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new GiveAdapter(this, list);

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
        wxShareSp = getSharedPreferences("wxShare", Context.MODE_PRIVATE);
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
    }

    private void initListView() {
        listGive.setAdapter(adapter);
    }

    private void getData() {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());

        JSONObject object = new JSONObject();
        try {
            object.put("owner", userInfoSp.getString("userId", null));
            object.put("receiver", "");
            object.put("status", "");
            object.put("createDatetimeStart", "");
            object.put("createDatetimeEnd", yesterday);
            object.put("receiveDatetimeStart", "");
            object.put("receiveDatetimeEnd", "");
            object.put("orderDir", "");
            object.put("orderColumn", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808476", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonObject = new JSONArray(result);

                    Gson gson = new Gson();
                    ArrayList<GiveModel> lists = gson.fromJson(jsonObject.toString(), new TypeToken<ArrayList<GiveModel>>() {
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
                Toast.makeText(GiveHistoryActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(GiveHistoryActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }
}
