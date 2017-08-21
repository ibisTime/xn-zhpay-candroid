package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.AddressAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.AddressModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_805165;

public class AddressSelectActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_address)
    ListView listAddress;
    @InjectView(R.id.txt_add)
    TextView txtAdd;

    private SharedPreferences preferences;

    private List<AddressModel> list;
    private AddressAdapter adapter;

    private boolean isFirst;

    public static AddressSelectActivity instances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_select);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        instances = this;

        inits();
        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        list = new ArrayList<>();
        adapter = new AddressAdapter(this,list);

    }

    private void initListView() {
        listAddress.setAdapter(adapter);
    }

    @OnClick({R.id.layout_back, R.id.txt_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_add:
                startActivity(new Intent(AddressSelectActivity.this,AddAddressActivity.class).putExtra("isFirst",isFirst));
                break;
        }
    }

    public void getData(final boolean isFinish) {

        JSONObject object = new JSONObject();
        try {
            object.put("userId",preferences.getString("userId",null));
            object.put("code","");
            object.put("isDefault","");
            object.put("token",preferences.getString("token",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_805165, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray jsonObject = new JSONArray(result);
                    Gson gson = new Gson();
                    ArrayList<AddressModel> lists = gson.fromJson(jsonObject.toString(), new TypeToken<ArrayList<AddressModel>>(){}.getType());

                    list.clear();
                    list.addAll(lists);
                    adapter.notifyDataSetChanged();

                    if(lists.size() == 0){
                        isFirst = true;
                    }else{
                        isFirst = false;
                    }

                    if(isFinish)
                        finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(AddressSelectActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(AddressSelectActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
