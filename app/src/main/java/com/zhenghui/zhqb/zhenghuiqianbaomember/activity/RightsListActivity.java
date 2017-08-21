package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.RightsListAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.RightsListModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808425;

public class RightsListActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_rights)
    ListView listRights;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;
    @InjectView(R.id.txt_history)
    TextView txtHistory;

    List<RightsListModel> list;
    RightsListAdapter adapter;

    private String code;
    private String date;
    private String received;
    private String unclaimed;

    private int page = 1;
    private int pageSize = 10;

    private View headView;
    private TextView txtCode;
    private TextView txtReceived;
    private TextView txtUnclaimed;
    private TextView txtDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rights_list_main);
        ButterKnife.inject(this);

        inits();
        initHeadView();
        initsListView();
        initRefreshLayout();

        getData();
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new RightsListAdapter(this, list);

        code = getIntent().getStringExtra("code");
        date = getIntent().getStringExtra("date");
        received = getIntent().getStringExtra("received");
        unclaimed = getIntent().getStringExtra("unclaimed");

    }

    private void initHeadView() {
        headView = LayoutInflater.from(this).inflate(R.layout.head_rights_list, null);
        txtCode = (TextView) headView.findViewById(R.id.txt_code);
        txtReceived = (TextView) headView.findViewById(R.id.txt_received);
        txtUnclaimed = (TextView) headView.findViewById(R.id.txt_unclaimed);
        txtDate = (TextView) headView.findViewById(R.id.txt_date);

        txtCode.setText("FHQID" + code.substring(code.length() - 6, code.length()));
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date != null) {
            Date d5 = new Date(date);
            txtDate.setText(s.format(d5));
        }

        txtReceived.setText(received);
        txtUnclaimed.setText(unclaimed);
    }

    private void initsListView() {
        listRights.addHeaderView(headView);
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

    @OnClick({R.id.layout_back, R.id.txt_history})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_history:
                startActivity(new Intent(RightsListActivity.this,RightsHistoryActivity.class).putExtra("code",code));
                break;
        }
    }

    private void getData() {

        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("fundCode", "");
            object.put("stockCode", code);
            object.put("toUser", "");
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("orderColumn", "");
            object.put("orderDir", "");
            object.put("companyCode", appConfigSp.getString("systemCode", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808425, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<RightsListModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<RightsListModel>>() {
                    }.getType());

                    if(page == 1){
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
                Toast.makeText(RightsListActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RightsListActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }
}
