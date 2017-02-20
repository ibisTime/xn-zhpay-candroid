package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.MyDuoBaoDetailAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.MyDuoBaoDetailModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.MyDuoBaoModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MyDuoBaoDetailActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_name)
    TextView txtName;
    @InjectView(R.id.txt_lucky)
    TextView txtLucky;
    @InjectView(R.id.txt_numberame)
    TextView txtNumberame;
    @InjectView(R.id.list_duobao)
    ListView listDuobao;

    MyDuoBaoModel model;

    private int page = 1;
    private int pageSize = 10;

    private List<MyDuoBaoDetailModel> list;
    private MyDuoBaoDetailAdapter adapter;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_duo_bao_detail);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initListView();

        setView();
        getDatas();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {

        model = (MyDuoBaoModel) getIntent().getSerializableExtra("model");

        list = new ArrayList<>();
        adapter = new MyDuoBaoDetailAdapter(this,list);

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    private void initListView() {
        listDuobao.setAdapter(adapter);
    }


    private void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("jewelCode",model.getJewel().getCode());
            object.put("status","");
            object.put("systemCode",appConfigSp.getString("systemCode",null));
            object.put("start", page+"");
            object.put("limit", pageSize+"");
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
                    ArrayList<MyDuoBaoDetailModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<MyDuoBaoDetailModel>>() {
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
                Toast.makeText(MyDuoBaoDetailActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(MyDuoBaoDetailActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {

        try{
            txtName.setText(model.getJewel().getName());

            txtNumberame.setText(model.getMyInvestTimes()+"");

            if(model.getJewel().getWinNumber() == null){
                txtLucky.setText("未揭晓");
            }else{
                txtLucky.setText(model.getJewel().getWinNumber());
            }
        }catch (Exception e){
            e.printStackTrace();
        }




    }


    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }
}
