package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Context;
import android.content.Intent;
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
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.BillAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.BillModel;
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

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_802524;

public class BillActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_bill)
    ListView listBill;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;
    @InjectView(R.id.txt_balance)
    TextView txtBalance;
    @InjectView(R.id.txt_beGx)
    TextView txtBeGx;
    @InjectView(R.id.txt_btn)
    TextView txtBtn;
    @InjectView(R.id.txt_title)
    TextView txtTitle;

    private List<BillModel> list;
    private BillAdapter adapter;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private double accountAmount;

    private String accountName;
    private String accountNumber;

    private int page = 1;
    private int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initButton();
        initListView();
        initRefreshLayout();

    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new BillAdapter(this, list);

        accountName = getIntent().getStringExtra("accountName");
        accountNumber = getIntent().getStringExtra("accountNumber");
        accountAmount = getIntent().getDoubleExtra("accountAmount", 0.00);

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);

    }

    private void initButton() {
        txtBalance.setText(MoneyUtil.moneyFormatDouble(accountAmount));

        switch (accountName) {
            case "gxb":
                txtBeGx.setVisibility(View.GONE);
                txtBtn.setText("消费");
                txtBtn.setVisibility(View.GONE);
                txtTitle.setText("贡献值流水");
                break;

            case "frb":
                txtBeGx.setVisibility(View.VISIBLE);
                txtBeGx.setText("转账");
                txtBtn.setVisibility(View.VISIBLE);
                txtBtn.setText("提现");
                txtTitle.setText("分润流水");
                break;

            case "lpq":
                txtBeGx.setVisibility(View.GONE);
                txtBtn.setVisibility(View.GONE);
                txtTitle.setText("礼品券流水");
                break;

            case "qbb":
                txtBeGx.setVisibility(View.GONE);
                txtBtn.setText("消费");
                txtBtn.setVisibility(View.GONE);
                txtTitle.setText("钱包币流水");
                break;

            case "szjf":
                txtBeGx.setVisibility(View.GONE);
                txtBtn.setVisibility(View.GONE);
                txtTitle.setText("数字积分流水");
                break;

            case "lmq":
                txtBeGx.setVisibility(View.GONE);
                txtBtn.setVisibility(View.GONE);
                txtTitle.setText("联盟币流水");
                break;
        }
    }

    private void initListView() {
        listBill.setAdapter(adapter);
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
            object.put("token", userInfoSp.getString("token", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("accountNumber", accountNumber);
            object.put("status","");
            object.put("start", page);
            object.put("limit", pageSize);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_802524, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    List<BillModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<BillModel>>() {
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
                Toast.makeText(BillActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(BillActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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
                getData();
            }
        }, 1500);
    }

    @OnClick({R.id.txt_beGx, R.id.txt_btn, R.id.layout_back, R.id.txt_history})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_beGx:

                switch (accountName){
                    case "hbyj":
                        startActivity(new Intent(BillActivity.this, TransFenRunActivity.class)
                                .putExtra("type", "HBYJ")
                                .putExtra("balance", accountAmount));
                        break;

                    case "hbb":
                        startActivity(new Intent(BillActivity.this, TransFenRunActivity.class)
                                .putExtra("type", "HBB")
                                .putExtra("balance", accountAmount));
                        break;

                    case "frb":
                        if(userInfoSp.getString("tradepwdFlag", null).equals("1")){ // tradepwdFlag 支付密码标示 1有 0 无

                            startActivity(new Intent(BillActivity.this, TransferActivity.class)
                                    .putExtra("balance", accountAmount));

                        } else {

                            Toast.makeText(this, "请先设置支付密码", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(BillActivity.this, ModifyTradeActivity.class).putExtra("isModify",false));

                        }

                        break;
                }

                break;

            case R.id.txt_btn:
                if(txtBtn.getText().equals("消费")){

                    // 跳到小目标

                }else if(txtBtn.getText().equals("转分润")){

//                    switch (accountName){
//                        case "hbyj":
//                            startActivity(new Intent(BillActivity.this, TransFenRunActivity.class)
//                                    .putExtra("type", "52")
//                                    .putExtra("balance", accountAmount));
//                            break;
//
//                        case "hbb":
//                            startActivity(new Intent(BillActivity.this, TransFenRunActivity.class)
//                                    .putExtra("type", "50")
//                                    .putExtra("balance", accountAmount));
//                            break;
//                    }

                }else if(txtBtn.getText().equals("提现")){

                    if(userInfoSp.getString("identityFlag", null).equals("1")){ //identityFlag 实名认证标示 1有 0 无

                        if(userInfoSp.getString("tradepwdFlag", null).equals("1")){ // tradepwdFlag 支付密码标示 1有 0 无

                            startActivity(new Intent(BillActivity.this, WithdrawalsActivity.class)
                                    .putExtra("balance", accountAmount)
                                    .putExtra("accountNumber", accountNumber));

                        } else {

                            Toast.makeText(this, "请先设置支付密码", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(BillActivity.this, ModifyTradeActivity.class).putExtra("isModify",false));

                        }

                    }else{
                        Toast.makeText(this, "请先实名认证", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BillActivity.this, AuthenticateActivity.class));
                    }

                }
                break;

            case R.id.txt_history:
                startActivity(new Intent(BillActivity.this, BillHistoryActivity.class)
                        .putExtra("code", accountNumber));
                break;
        }
    }
}
