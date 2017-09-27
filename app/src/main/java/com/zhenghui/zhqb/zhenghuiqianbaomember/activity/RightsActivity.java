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
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.RightsAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.RightsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.WalletModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.R.id.txt_get;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_802503;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808417;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808418;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808419;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808917;

public class RightsActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener,RefreshLayout.OnLoadListener,AdapterView.OnItemClickListener {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_rights)
    ListView listRights;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;


    TextView txtFhq;
    TextView txtGet;
    TextView txtPool;
    TextView txtFenrun;
    TextView txtWithdrawal;
    TextView txtEarnings;
    TextView txtTurnover;
    LinearLayout layoutGet;
    LinearLayout layoutPool;

    List<RightsModel> list;
    RightsAdapter adapter;

    private double accountAmount;
    private String accountNumber;

    private View headView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rights);
        ButterKnife.inject(this);

        inits();
        initHeadView();
        initsListView();
        initRefreshLayout();

        getData();
        getLimit();
        getProperty();
        getIsShow();
        getMoney();
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new RightsAdapter(this,list);
    }

    private void initHeadView() {
        headView = LayoutInflater.from(this).inflate(R.layout.head_rights,null);

        txtFhq = (TextView) headView.findViewById(R.id.txt_fhq);
        txtGet = (TextView) headView.findViewById(txt_get);
        txtPool = (TextView) headView.findViewById(R.id.txt_pool);
        txtFenrun = (TextView) headView.findViewById(R.id.txt_fenrun);
        txtWithdrawal = (TextView) headView.findViewById(R.id.txt_withdrawal);
        txtEarnings = (TextView) headView.findViewById(R.id.txt_earnings);
        txtTurnover = (TextView) headView.findViewById(R.id.txt_turnover);

        layoutGet = (LinearLayout) headView.findViewById(R.id.layout_get);
        layoutPool = (LinearLayout) headView.findViewById(R.id.layout_pool);
    }

    private void initsListView() {
        listRights.addHeaderView(headView);
        listRights.setAdapter(adapter);
        listRights.setOnItemClickListener(this);

        txtWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userInfoSp.getString("identityFlag", null).equals("1")){ //identityFlag 实名认证标示 1有 0 无

                    if(userInfoSp.getString("tradepwdFlag", null).equals("1")){ // tradepwdFlag 支付密码标示 1有 0 无

                        startActivity(new Intent(RightsActivity.this, WithdrawalsActivity.class)
                                .putExtra("balance", accountAmount)
                                .putExtra("accountNumber", accountNumber));

                    } else {

                        Toast.makeText(RightsActivity.this, "请先设置支付密码", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RightsActivity.this, ModifyTradeActivity.class).putExtra("isModify",false));

                    }

                }else{
                    Toast.makeText(RightsActivity.this, "请先实名认证", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RightsActivity.this, AuthenticateActivity.class));
                }
            }
        });
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnLoadListener(this);
        swipeContainer.setOnRefreshListener(this);
    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }

    private void getIsShow() {
        JSONObject object = new JSONObject();
        try {
            object.put("key", "POOL_VISUAL");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808917, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if(jsonObject.getString("cvalue").equals("1")){ // 1显示
                        layoutGet.setVisibility(View.GONE);
                        layoutPool.setVisibility(View.VISIBLE);
                    }else {
                        layoutPool.setVisibility(View.GONE);
                        layoutGet.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(RightsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RightsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProperty() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808419, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    txtFhq.setText(jsonObject.getInt("stockCount")+"");

                    txtPool.setText(MoneyUtil.moneyFormatDouble(jsonObject.getDouble("poolAmount")));
                    txtGet.setText(MoneyUtil.moneyFormatDouble(jsonObject.getDouble("backProfitAmount")));
                    txtEarnings.setText(MoneyUtil.moneyFormatDouble(jsonObject.getDouble("unbackProfitAmount")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(RightsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RightsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 查询所需消费额
     */
    private void getLimit() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808418, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    txtTurnover.setText(MoneyUtil.moneyFormatDouble(500000 - jsonObject.getDouble("costAmount")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(RightsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RightsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808417, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new Gson();
                    ArrayList<RightsModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<RightsModel>>() {
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
                Toast.makeText(RightsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RightsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getMoney() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_802503, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    Gson gson = new Gson();
                    List<WalletModel> lists = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<WalletModel>>() {
                    }.getType());

                    setMoney(lists);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(RightsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RightsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMoney(List<WalletModel> lists) {
        for (WalletModel model : lists) {
            switch (model.getCurrency()) {

                case "FRB": // 分润
                    txtFenrun.setText(MoneyUtil.moneyFormatDouble(model.getAmount()));
                    accountAmount = model.getAmount();
                    accountNumber = model.getAccountNumber();
                    break;


            }
        }
    }

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
//                page = 1;
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
//                page = page + 1;
                getData();


            }
        }, 1500);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(i > 0){
            startActivity(new Intent(RightsActivity.this, RightsListActivity.class)
                    .putExtra("received", MoneyUtil.moneyFormatDouble(list.get(i-1).getBackAmount()))
                    .putExtra("unclaimed", MoneyUtil.moneyFormatDouble(list.get(i-1).getProfitAmount() - list.get(i-1).getBackAmount()))
                    .putExtra("date",list.get(i-1).getCreateDatetime())
                    .putExtra("code",list.get(i-1).getCode())
                    .putExtra("type","FHQ"));
        }
    }
}
