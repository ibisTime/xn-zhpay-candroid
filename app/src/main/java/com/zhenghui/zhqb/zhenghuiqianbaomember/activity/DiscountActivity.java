package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.DiscountAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.DiscountModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808265;

public class DiscountActivity extends MyBaseActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_discount)
    ListView listDiscount;
    @InjectView(R.id.txt_title)
    TextView txtTitle;

    private List<DiscountModel> list;
    private DiscountAdapter adapter;

    private int money = 0;
    private boolean isShop;
    private String storeCode = "";

    private int page = 1;
    private int pageSize = 10;
    private SharedPreferences userInfoSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initListView();

        initEvent();
        getList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new DiscountAdapter(this, list);

        money = getIntent().getIntExtra("money",0);
        isShop = getIntent().getBooleanExtra("isShop", false);
        storeCode = getIntent().getStringExtra("storeCode");
        if (isShop) {
            txtTitle.setText("选择折扣券");
        }

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        setResult(0,new Intent().putExtra("ticketName","").putExtra("ticketCode","").putExtra("key1",0).putExtra("key2",0));
    }

    private void initListView() {
        listDiscount.setAdapter(adapter);
    }

    private void initEvent() {
        listDiscount.setOnItemClickListener(this);
    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        setResult(0,new Intent().putExtra("ticketName","").putExtra("ticketCode","").putExtra("key1",0).putExtra("key2",0));
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (isShop) {
            System.out.println("money="+money);
            System.out.println("list.get(i).getStoreTicket().getKey1()="+list.get(i).getStoreTicket().getKey1());

            if(money >= list.get(i).getStoreTicket().getKey1()){
                String ticketName = "满"+(list.get(i).getStoreTicket().getKey1()/1000)+"减"+(list.get(i).getStoreTicket().getKey2()/1000);
                setResult(0,new Intent().putExtra("ticketName",ticketName)
                        .putExtra("key1",list.get(i).getStoreTicket().getKey1())
                        .putExtra("key2",list.get(i).getStoreTicket().getKey2())
                        .putExtra("ticketCode",list.get(i).getCode()));
                finish();
            }else{
                Toast.makeText(this, "您的消费金额未达到此折扣券使用门槛哦", Toast.LENGTH_SHORT).show();
            }
        } else {

            startActivity(new Intent(DiscountActivity.this,DiscountDetailActivity.class)
                    .putExtra("name",list.get(i).getStore().getName())
                    .putExtra("phone",list.get(i).getStore().getBookMobile())
                    .putExtra("code",list.get(i).getTicketCode()));

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("onBackPressed");
    }

    private void getList() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("status", "");
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("orderColumn", "");
            object.put("orderDir", "");
            if(isShop){
                object.put("storeCode", storeCode);
            }else{
                object.put("storeCode", "");
            }
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808265, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Gson gson = new Gson();
                    List<DiscountModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<DiscountModel>>() {
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
                Toast.makeText(DiscountActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DiscountActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
