package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.DuoBaoAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.DuoBaoModel;
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

public class DuoBaoListActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.edt_search)
    EditText edtSearch;
    @InjectView(R.id.layout_search)
    LinearLayout layoutSearch;
    @InjectView(R.id.list_oneyuan)
    ListView listOneyuan;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;

    private List<DuoBaoModel> list;
    private DuoBaoAdapter adapter;

    private int page = 1;
    private int pageSize = 10;
    private SharedPreferences appConfigSp;
    private SharedPreferences userInfoSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_yuan);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initRefreshLayout();
        initListView();
        initEvent();
        getList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }


    private void inits() {
        list = new ArrayList<>();
        adapter = new DuoBaoAdapter(this, list);
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
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
        listOneyuan.setAdapter(adapter);
        listOneyuan.setOnItemClickListener(this);
    }

    private void initEvent() {
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((actionId == 0 || actionId == EditorInfo.IME_ACTION_SEARCH) && event != null) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(DuoBaoListActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    page = 1;
                    getList();

                }
                return false;

            }

        });
    }

    @OnClick({R.id.layout_back, R.id.layout_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_search:

                break;
        }
    }

    private void getList() {
        // 0 待审批 1 审批通过 2 审批不通过 3 夺宝进行中 4 强制下架 5 到期流标 6 夺宝成功，待开奖 7 已开奖，待发货 8 已发货，待收货 9 已收货

        // 0:待支付 1：待开奖 2：已中奖 3：未中奖 4：已发货 5：强制退款

        JSONObject object = new JSONObject();
        try {
            object.put("storeCode", "");
            object.put("name", edtSearch.getText().toString().trim());
            object.put("dateStart", "");
            object.put("dateEnd", "");
            object.put("status", "3");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("start", page + "");
            object.put("limit", pageSize + "");
            object.put("orderDir", "");
            object.put("orderColumn", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808310", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<DuoBaoModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<DuoBaoModel>>() {
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
                Toast.makeText(DuoBaoListActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DuoBaoListActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        if (userInfoSp.getString("userId", null) != null) {
            startActivity(new Intent(DuoBaoListActivity.this, DuoBaoActivity.class).putExtra("code", list.get(i).getCode()));
//        } else {
//            LoginUtil.toLogin(DuoBaoListActivity.this);
//        }

    }
}
