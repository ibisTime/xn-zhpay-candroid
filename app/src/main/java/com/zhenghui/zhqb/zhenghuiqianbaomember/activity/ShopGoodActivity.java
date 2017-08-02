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
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.GoodAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.GoodsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.LoginUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808028;


public class ShopGoodActivity extends MyBaseActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_good)
    ListView listGood;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;

    private String owner;

    private List<GoodsModel> list;
    private GoodAdapter adapter;

    private int page = 1;
    private int pageIndex = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_good);
        ButterKnife.inject(this);

        inits();
        initRefreshLayout();
        initListView();

        getDatas();

    }

    private void inits() {
        owner = getIntent().getStringExtra("owner");

        list = new ArrayList<>();
        adapter = new GoodAdapter(this, list);
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
        listGood.setAdapter(adapter);
        listGood.setOnItemClickListener(this);
    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }

    private void getDatas() {

        JSONObject object = new JSONObject();
        try {
            object.put("category", "");
            object.put("type", "");
            object.put("name", "");
            object.put("status", "3");
            object.put("location", "");
            object.put("start", page+"");
            object.put("limit", pageIndex+"");
            object.put("orderDir", "");
            object.put("orderColumn", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", owner);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808028, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);


                    Gson gson = new Gson();
                    ArrayList<GoodsModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<GoodsModel>>() {
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
                Toast.makeText(ShopGoodActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ShopGoodActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (userInfoSp.getString("userId", null) != null) {
            startActivity(new Intent(ShopGoodActivity.this, GoodDetailsActivity.class).putExtra("code", list.get(position).getCode()));
        } else {
            LoginUtil.toLogin(ShopGoodActivity.this);
        }
    }
}
