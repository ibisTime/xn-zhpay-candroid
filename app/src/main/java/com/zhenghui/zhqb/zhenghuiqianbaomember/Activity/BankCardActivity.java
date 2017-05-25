package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.MyBankCardAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.MyBankCardModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class BankCardActivity extends MyBaseActivity implements AdapterView.OnItemClickListener {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_add)
    TextView txtAdd;
    @InjectView(R.id.list_bankCart)
    ListView listBankCart;
    @InjectView(R.id.img_bankCart2)
    ImageView imgBankCart2;
    @InjectView(R.id.txt_name2)
    TextView txtName2;
    @InjectView(R.id.txt_type2)
    TextView txtType2;
    @InjectView(R.id.img_bankCart3)
    ImageView imgBankCart3;
    @InjectView(R.id.txt_name3)
    TextView txtName3;
    @InjectView(R.id.txt_type3)
    TextView txtType3;
    @InjectView(R.id.txt_bankCard)
    TextView txtBankCard;
    private int page = 1;
    private int pageSize = 10;

    private List<MyBankCardModel> list;
    private MyBankCardAdapter adapter;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private boolean isWithdrawal;
    private String transAmount;
    private String accountNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        // 初始返回数据
        setResult(0,new Intent().putExtra("bankcardNumber","").putExtra("bankName",""));
        inits();
        initEvent();
        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new MyBankCardAdapter(this, list);

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);

        transAmount = getIntent().getStringExtra("transAmount");
        accountNumber = getIntent().getStringExtra("accountNumber");
        isWithdrawal = getIntent().getBooleanExtra("isWithdrawal",false);
        if(isWithdrawal){
            txtBankCard.setText("选择银行卡");
        }

    }

    private void initEvent() {
        listBankCart.setOnItemClickListener(this);
    }

    private void initListView() {
        listBankCart.setAdapter(adapter);
    }

    private void getList() {

        JSONObject object = new JSONObject();
        try {
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("token", userInfoSp.getString("token", null));
            object.put("bankcardNumber", "");
            object.put("bankName", "");
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("realName", "");
            object.put("type", "");
            object.put("status", "");
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("orderColumn", "");
            object.put("orderDir", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("802015", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<MyBankCardModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<MyBankCardModel>>() {
                    }.getType());

                    if (page == 1) {
                        list.clear();
                    }

                    list.addAll(lists);
                    if(list.size()>0){
                        txtAdd.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(BankCardActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(BankCardActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.layout_back, R.id.txt_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                setResult(0,new Intent().putExtra("bankcardNumber","").putExtra("bankName",""));
                finish();
                break;

            case R.id.txt_add:
                startActivity(new Intent(BankCardActivity.this, BindBankCardActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(isWithdrawal){
//            withdrawal();
            setResult(0,new Intent().putExtra("bankcardNumber",list.get(i).getBankcardNumber())
                    .putExtra("bankName",list.get(i).getBankName()));
            finish();
        }else{
            startActivity(new Intent(BankCardActivity.this, BindBankCardActivity.class)
                    .putExtra("code",list.get(i).getCode())
                    .putExtra("isModifi",true));
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("onBackPressed()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop()");
    }

}
