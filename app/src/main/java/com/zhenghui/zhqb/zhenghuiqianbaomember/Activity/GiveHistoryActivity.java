package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.GiveAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.GiveModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class GiveHistoryActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener {

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
        listGive.setOnItemClickListener(this);
    }

    private void getData() {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());

        JSONObject object = new JSONObject();
        try {
            object.put("hzbCode", "");
            object.put("owner", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));
            object.put("receiver", "");
            object.put("status", "");
            object.put("createDatetimeStart", "");
            object.put("createDatetimeEnd", yesterday);
            object.put("receiveDatetimeStart", "");
            object.put("receiveDatetimeEnd", "");
            object.put("start","1");
            object.put("limit","5");
            object.put("orderDir", "");
            object.put("orderColumn", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("615135", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<GiveModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<GiveModel>>() {
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (list.get(i).getStatus().equals("1")) { // 已发送,待领取
            tip2();
        } else if (list.get(i).getStatus().equals("2")) { // 已领取
            tip3(i);
        }
    }

    private void tip2() {
        final AlertDialog alertDialog = new AlertDialog.Builder(GiveHistoryActivity.this).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_give);
        TextView txtTitle = (TextView) window.findViewById(R.id.txt_title);
        TextView txtContent3 = (TextView) window.findViewById(R.id.txt_content3);
        TextView txtOk = (TextView) window.findViewById(R.id.txt_ok);

        txtTitle.setText("该红包还未被领取");
        txtContent3.setText("快通知小伙伴领取红包吧，不然要过期了...");

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void tip3(int position) {
        final AlertDialog alertDialog = new AlertDialog.Builder(GiveHistoryActivity.this).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_give);
        TextView txtTitle = (TextView) window.findViewById(R.id.txt_title);
        TextView txtContent1 = (TextView) window.findViewById(R.id.txt_content1);
        TextView txtContent2 = (TextView) window.findViewById(R.id.txt_content2);
        TextView txtContent3 = (TextView) window.findViewById(R.id.txt_content3);
        TextView txtOk = (TextView) window.findViewById(R.id.txt_ok);

        txtContent1.setVisibility(View.VISIBLE);
        txtContent2.setVisibility(View.VISIBLE);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(list.get(position).getReceiveDatetime());

        txtTitle.setText("该红包已经被领取");
        txtContent1.setText("领取人：" + list.get(position).getReceiverUser().getMobile());
        txtContent2.setText("领取时间：" + format.format(date));
        txtContent3.setText("您的红包业绩已经到账，请注意查收");

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
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
