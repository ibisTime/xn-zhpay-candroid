package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

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
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.ShopAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.ShopModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808217;

public class ShopListActivity extends MyBaseActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_title)
    TextView txtTitle;
    @InjectView(R.id.edt_search)
    EditText edtSearch;
    @InjectView(R.id.list_shop)
    ListView listShop;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;
    @InjectView(R.id.layout_search)
    LinearLayout layoutSearch;

    private ShopModel model;
    private ShopAdapter adapter;
    private List<ShopModel> shopList;

    private SharedPreferences appConfigSp;
    private SharedPreferences userInfoSp;

    private int page = 1;
    private int pageIndex = 10;

    private String type;

    private String latitude = "";
    private String longitude = "";
    public String locatedCity = "";

    private Boolean isHideSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initEvent();
        initRefreshLayout();
        initListView();

//        getDatas();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void initEvent() {
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((actionId == 0 || actionId == EditorInfo.IME_ACTION_SEARCH) && event != null) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(ShopListActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    page = 1;
                    getDatas();


                }
                return false;

            }

        });

        if(isHideSearch){
            layoutSearch.setVisibility(View.GONE);
        }
    }

    private void inits() {
        type = getIntent().getStringExtra("type");
        txtTitle.setText(getIntent().getStringExtra("title"));
        isHideSearch = getIntent().getBooleanExtra("isHide",false);

        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        locatedCity = getIntent().getStringExtra("locatedCity");

        shopList = new ArrayList<>();
        adapter = new ShopAdapter(this, shopList);

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
        listShop.setAdapter(adapter);
        listShop.setOnItemClickListener(this);
    }

    @OnClick({R.id.layout_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

        }
    }

    /**
     * 获取商家列表
     */
    private void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("fromUser", "");
            object.put("name", edtSearch.getText().toString().trim());
            object.put("type", type);
            object.put("legalPersonName", "");
            object.put("userReferee", "");
            object.put("province", "");
//            object.put("city", locatedCity);
            object.put("city", "");
            object.put("area", "");
            object.put("status", "2");
            object.put("owner", "");
            object.put("longitude", longitude + "");
            object.put("latitude", latitude + "");
            object.put("start", page);
            object.put("limit", pageIndex);
            object.put("orderColumn", "");
            object.put("orderDir", "");
            object.put("token", userInfoSp.getString("token", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808217, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    List<ShopModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<ShopModel>>() {
                    }.getType());

                    if (page == 1) {
                        shopList.clear();
                    }

                    shopList.addAll(lists);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ShopListActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ShopListActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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
                getDatas();
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
                getDatas();
            }
        }, 1500);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        startActivity(new Intent(ShopListActivity.this, ShopDetailsActivity.class).putExtra("code", shopList.get(i).getCode()));
    }
}
