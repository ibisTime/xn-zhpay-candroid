package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.ScheduleAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.DuoBaoModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.ScheduleModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ScheduleActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_rmb_total1)
    TextView txtRmbTotal1;
    @InjectView(R.id.txt_rmb_total2)
    TextView txtRmbTotal2;
    @InjectView(R.id.txt_rmb_total3)
    TextView txtRmbTotal3;
    @InjectView(R.id.txt_gwb_total1)
    TextView txtGwbTotal1;
    @InjectView(R.id.txt_gwb_total2)
    TextView txtGwbTotal2;
    @InjectView(R.id.txt_gwb_total3)
    TextView txtGwbTotal3;
    @InjectView(R.id.txt_qbb_total1)
    TextView txtQbbTotal1;
    @InjectView(R.id.txt_qbb_total2)
    TextView txtQbbTotal2;
    @InjectView(R.id.txt_rmb1)
    TextView txtRmb1;
    @InjectView(R.id.txt_rmb2)
    TextView txtRmb2;
    @InjectView(R.id.txt_rmb3)
    TextView txtRmb3;
    @InjectView(R.id.txt_gwb1)
    TextView txtGwb1;
    @InjectView(R.id.txt_gwb2)
    TextView txtGwb2;
    @InjectView(R.id.txt_gwb3)
    TextView txtGwb3;
    @InjectView(R.id.txt_qbb1)
    TextView txtQbb1;
    @InjectView(R.id.txt_qbb2)
    TextView txtQbb2;
    @InjectView(R.id.list_schedule)
    ListView listSchedule;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;

    private int page = 1;
    private int pageSize = 10;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private List<ScheduleModel> list;
    private ScheduleAdapter adapter;

    private DuoBaoModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initListView();
        getList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        model = (DuoBaoModel) getIntent().getSerializableExtra("model");

        list = new ArrayList<>();
        adapter = new ScheduleAdapter(this, list);

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    private void initListView() {
        listSchedule.setAdapter(adapter);
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }

    private void getList() {

        JSONObject object = new JSONObject();
        try {
            object.put("jewelCode", model.getCode());
            object.put("status", "payeds");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("start", page + "");
            object.put("limit", pageSize + "");
            object.put("orderDir", "");
            object.put("orderColumn", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808315", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<ScheduleModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<ScheduleModel>>() {
                    }.getType());

                    if (page == 1) {
                        list.clear();
                    }

                    setView();

                    list.addAll(lists);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ScheduleActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ScheduleActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {

        initTotalPrice();
        initPrice();

    }

    private void initPrice() {
        // 人名币总价
        double rmb = (model.getPrice1() * model.getInvestNum());
        // 购物币总价
        int gwb = (model.getPrice2() * model.getInvestNum());
        // 钱包币总价
        int qbb = (model.getPrice3() * model.getInvestNum());

        if (rmb == 0) {
            txtRmb1.setVisibility(View.GONE);
            txtRmb2.setVisibility(View.GONE);
            txtRmb3.setVisibility(View.GONE);
        } else {
            txtRmb1.setVisibility(View.VISIBLE);
            txtRmb2.setVisibility(View.VISIBLE);
            txtRmb3.setVisibility(View.VISIBLE);

            txtRmb2.setText(MoneyUtil.moneyFormatDouble(rmb));
            if (gwb == 0 && qbb == 0) {
                txtRmb3.setText("");
            }else{
                txtRmb3.setText(" + ");
            }
        }

        if (gwb == 0) {
            txtGwb1.setVisibility(View.GONE);
            txtGwb2.setVisibility(View.GONE);
            txtGwb3.setVisibility(View.GONE);
        } else {
            txtGwb1.setVisibility(View.VISIBLE);
            txtGwb2.setVisibility(View.VISIBLE);
            txtGwb3.setVisibility(View.VISIBLE);

            txtGwb2.setText((gwb / 1000) + "");
            if (qbb == 0) {
                txtGwb3.setText("");
            }else{
                txtGwb3.setText(" + ");
            }
        }

        if (qbb == 0) {
            txtQbb1.setVisibility(View.GONE);
            txtQbb2.setVisibility(View.GONE);
        } else {
            txtQbb1.setVisibility(View.VISIBLE);
            txtQbb2.setVisibility(View.VISIBLE);

            txtQbb2.setText((qbb / 1000) + "");
        }
    }

    private void initTotalPrice() {
        // 人名币总价
        double rmb = (model.getPrice1() * model.getTotalNum());
        // 购物币总价
        int gwb = (model.getPrice2() * model.getTotalNum());
        // 钱包币总价
        int qbb = (model.getPrice3() * model.getTotalNum());

        if (rmb == 0) {
            txtRmbTotal1.setVisibility(View.GONE);
            txtRmbTotal2.setVisibility(View.GONE);
            txtRmbTotal3.setVisibility(View.GONE);
        } else {
            txtRmbTotal1.setVisibility(View.VISIBLE);
            txtRmbTotal2.setVisibility(View.VISIBLE);
            txtRmbTotal3.setVisibility(View.VISIBLE);

            txtRmbTotal2.setText(MoneyUtil.moneyFormatDouble(rmb));
            if (gwb == 0 && qbb == 0) {
                txtRmbTotal3.setText("");
            }else{
                txtRmbTotal3.setText(" + ");
            }
        }

        if (gwb == 0) {
            txtGwbTotal1.setVisibility(View.GONE);
            txtGwbTotal2.setVisibility(View.GONE);
            txtGwbTotal3.setVisibility(View.GONE);
        } else {
            txtGwbTotal1.setVisibility(View.VISIBLE);
            txtGwbTotal2.setVisibility(View.VISIBLE);
            txtGwbTotal3.setVisibility(View.VISIBLE);

            txtGwbTotal2.setText((gwb / 1000) + "");
            if (qbb == 0) {
                txtGwbTotal3.setText("");
            }else{
                txtGwbTotal3.setText(" + ");
            }
        }

        if (qbb == 0) {
            txtQbbTotal1.setVisibility(View.GONE);
            txtQbbTotal2.setVisibility(View.GONE);
        } else {
            txtQbbTotal1.setVisibility(View.VISIBLE);
            txtQbbTotal2.setVisibility(View.VISIBLE);

            txtQbbTotal2.setText((qbb / 1000) + "");
        }
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
}
