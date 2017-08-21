package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.JewelAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.JewelRecordModel;
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

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_615025;

public class JewelRecordActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_record)
    ListView listRecord;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;

    private int page = 1;
    private int pageSize = 10;

    private JewelAdapter adapter;
    private ArrayList<JewelRecordModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jewel_record);
        ButterKnife.inject(this);

        inits();
        initListView();
        initRefreshLayout();

        getJewelRecord();
    }


    private void inits() {
        list = new ArrayList<>();
        adapter = new JewelAdapter(this, list);
    }

    private void initListView() {
        listRecord.setAdapter(adapter);
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
    }

    private void getJewelRecord() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", "");
            object.put("jewelCode", "");
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("status", "123");
            object.put("orderDir", "");
            object.put("orderColumn", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_615025, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    List<JewelRecordModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<JewelRecordModel>>() {
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
                Toast.makeText(JewelRecordActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(JewelRecordActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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
                getJewelRecord();

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
                getJewelRecord();
            }
        }, 1500);
    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }
}
