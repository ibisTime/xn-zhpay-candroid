package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.JewelNumberModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.TargetModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.R.id.txt_buy;

public class MyDuoBaoNumActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_time)
    TextView txtTime;
    @InjectView(R.id.txt_name)
    TextView txtName;
    @InjectView(R.id.bar_schedule)
    ProgressBar barSchedule;
    @InjectView(R.id.txt_sum)
    TextView txtSum;
    @InjectView(R.id.txt_residue)
    TextView txtResidue;
    @InjectView(R.id.txt_price)
    TextView txtPrice;
    @InjectView(R.id.layout_bg)
    LinearLayout layoutBg;
    @InjectView(R.id.txt_winer)
    TextView txtWiner;
    @InjectView(R.id.layout_winer)
    LinearLayout layoutWiner;
    @InjectView(R.id.txt_winNumber)
    TextView txtWinNumber;
    @InjectView(R.id.layout_winNumber)
    LinearLayout layoutWinNumber;
    @InjectView(R.id.txt_winTime)
    TextView txtWinTime;
    @InjectView(R.id.layout_winTime)
    LinearLayout layoutWinTime;
    @InjectView(R.id.txt_times)
    TextView txtTimes;
    @InjectView(R.id.gridview_number)
    GridView gridviewNumber;
    @InjectView(R.id.txt_buy)
    TextView txtBuy;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;


    private String code;
    private String winer;
    private String winTime;
    private String winNumber;
    private TargetModel model;

    private SimpleAdapter adapter;
    private List<JewelNumberModel> list;
    private List<Map<String, Object>> data_list;

    private int page = 1;
    private int pageSize = 30;

    private String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_duo_bao_num);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        getData();
        initRefreshLayout();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        code = getIntent().getStringExtra("code");
        color = getIntent().getStringExtra("color");
        winer = getIntent().getStringExtra("winer");
        winTime = getIntent().getStringExtra("winTime");
        winNumber = getIntent().getStringExtra("winNumber");
        model = (TargetModel) getIntent().getSerializableExtra("model");

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        list = new ArrayList<>();
        data_list = new ArrayList<Map<String, Object>>();
        //新建适配器
        String[] from = {"number"};
        int[] to = {R.id.txt_number};
        adapter = new SimpleAdapter(this, data_list, R.layout.item_duobao_num, from, to);
        gridviewNumber.setAdapter(adapter);

    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
    }

    private void getData() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("jewelCode", code);
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("orderColumn", "");
            object.put("orderDir", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("615028", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();

                    List<JewelNumberModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<JewelNumberModel>>() {
                    }.getType());

                    if(page == 1){
                        list.clear();
                    }
                    list.addAll(lists);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setView();

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(MyDuoBaoNumActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(MyDuoBaoNumActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {

        if (winNumber != null) {
            if (!winNumber.equals("")) {
                layoutWinNumber.setVisibility(View.VISIBLE);
                txtWinNumber.setText(winNumber);

                layoutWiner.setVisibility(View.VISIBLE);
                txtWiner.setText(winer);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(winTime);
                layoutWinTime.setVisibility(View.VISIBLE);
                txtWinTime.setText(format.format(date));

                txtBuy.setVisibility(View.GONE);
            } else {
                txtBuy.setVisibility(View.VISIBLE);
            }
        }

        txtName.setText((model.getToAmount() / 1000) + getCurrency(model.getToCurrency()));
        txtTime.setText("第" + model.getPeriods() + "期");
        txtSum.setText(model.getTotalNum() + "");
        txtResidue.setText((model.getTotalNum() - model.getInvestNum()) + "");
        txtPrice.setText(MoneyUtil.moneyFormatDouble(model.getFromAmount()) + getCurrency(model.getFromCurrency()));
        layoutBg.setBackgroundResource(R.mipmap.target_bg_orange);

        switch (color) {
            case "orange":
                layoutBg.setBackgroundResource(R.mipmap.target_bg_orange);
                barSchedule.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_orange));
                break;

            case "yellow":
                layoutBg.setBackgroundResource(R.mipmap.target_bg_yellow);
                barSchedule.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_yellow));
                break;

            case "green":
                layoutBg.setBackgroundResource(R.mipmap.target_bg_green);
                barSchedule.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_green));
                break;
        }
        barSchedule.setMax((int) (model.getTotalNum()));
        barSchedule.setProgress(model.getInvestNum());

        txtTimes.setText(list.size() + "");

        for (int i = 0; i < list.size(); i++) {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("number", list.get(i).getNumber());
            data_list.add(map);

        }

        adapter.notifyDataSetChanged();
    }

    private String getCurrency(String currency) {
        String type = "";

        switch (currency) {
            case "FRB":
                type = "分润";
                break;

            case "GXJL":
                type = "贡献奖励";
                break;

            case "GWB":
                type = "购物币";
                break;

            case "QBB":
                type = "钱包币";
                break;

            case "HBB":
                type = "红包";
                break;

            case "HBYJ":
                type = "红包业绩";
                break;

            case "CNY":
                type = "人民币";
                break;
        }


        return type;
    }

    @OnClick({R.id.layout_back, txt_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case txt_buy:
                startActivity(new Intent(MyDuoBaoNumActivity.this, JewelActivity.class).putExtra("code", code));
                break;
        }
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

    @Override
    public void onLoad() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setLoading(false);
                page = page + 1;
                getData();
            }
        }, 1500);
    }
}
