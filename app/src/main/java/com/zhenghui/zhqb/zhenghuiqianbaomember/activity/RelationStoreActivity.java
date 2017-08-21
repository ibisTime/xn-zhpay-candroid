package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.RelationShopAdapter;
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

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808222;

public class RelationStoreActivity extends MyBaseActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_title)
    TextView txtTitle;
    @InjectView(R.id.list_shop)
    ListView listShop;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;

    private int page = 1;
    private int pageIndex = 10;

    private RelationShopAdapter adapter;
    private List<ShopModel> shopList;

    private String userReferee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ralation_store);
        ButterKnife.inject(this);

        init();
        initListView();
        initRefreshLayout();

        getDatas();

    }

    private void init(){
        userReferee = getIntent().getStringExtra("userReferee");
        
        shopList = new ArrayList<>();
        adapter = new RelationShopAdapter(this, shopList);
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

    @OnClick({R.id.layout_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

        }
    }

    private void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("userReferee", userReferee);
            object.put("longitude", userInfoSp.getString("longitude", null));
            object.put("latitude", userInfoSp.getString("latitude", null));
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

        new Xutil().post(CODE_808222, object.toString(), new Xutil.XUtils3CallBackPost() {
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
                Toast.makeText(RelationStoreActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RelationStoreActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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
        if (shopList.get(i).getStatus().equals("2")){
            startActivity(new Intent(RelationStoreActivity.this, ShopDetailsActivity.class).putExtra("code", shopList.get(i).getCode()));
        }else {
            Toast.makeText(this, "店铺未营业", Toast.LENGTH_SHORT).show();
        }

    }
}
