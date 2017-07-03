package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lljjcoder.citypickerview.widget.CityPicker;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddAddressActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.edt_name)
    EditText edtName;
    @InjectView(R.id.edt_phone)
    EditText edtPhone;
    @InjectView(R.id.txt_address)
    TextView txtAddress;
    @InjectView(R.id.edt_detailed)
    EditText edtDetailed;
    @InjectView(R.id.txt_confirm)
    TextView txtConfirm;

    private boolean isFirst;
    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private boolean isDuoBao;
    private Intent duoBaointent;

    private String code;
    private String isDefault;
    private boolean isModifi;

    private String mCurrentProviceName;
    private String mCurrentCityName;
    private String mCurrentDistrictName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inis();
        initModifi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inis() {
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);

        // 默认是第一次
        isFirst = getIntent().getBooleanExtra("isFirst", true);

        //
        isDuoBao = getIntent().getBooleanExtra("isDuoBao",false);

        // 编辑
        isModifi = getIntent().getBooleanExtra("isModifi",false);
        code = getIntent().getStringExtra("code");

        duoBaointent = new Intent();
        setResult(0,duoBaointent.putExtra("receiver","").putExtra("reMobile","").putExtra("reAddress",""));
    }

    private void initModifi() {
        if(isModifi){
            getAddressDetail();
        }
    }

    @OnClick({R.id.layout_back, R.id.txt_confirm, R.id.txt_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_address:
                cityPicker();
                break;

            case R.id.txt_confirm:
                if (edtName.getText().toString().trim().length() == 0) {
                    Toast.makeText(AddAddressActivity.this, "请填写您的姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtPhone.getText().toString().trim().length() != 11) {
                    Toast.makeText(AddAddressActivity.this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (txtAddress.getText().toString().trim().length() == 0) {
                    Toast.makeText(AddAddressActivity.this, "请选择省市区", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtDetailed.getText().toString().trim().length() == 0) {
                    Toast.makeText(AddAddressActivity.this, "请填写您的详细地址", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(isDuoBao){
                    duoBaointent.putExtra("receiver",edtName.getText().toString().trim());
                    duoBaointent.putExtra("reMobile",edtPhone.getText().toString().trim());
                    duoBaointent.putExtra("reAddress",mCurrentProviceName + mCurrentCityName + mCurrentDistrictName + edtDetailed.getText().toString().trim());
                    setResult(0,duoBaointent);
                    finish();
                }else{
                    if(isModifi){
                        modifiAddress();
                    }else{
                        setAddress();
                    }

                }
                break;
        }
    }

    /**
     * 新增收货地址
     */
    private void setAddress() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("addressee", edtName.getText().toString().trim());
            object.put("mobile", edtPhone.getText().toString().trim());
            object.put("province", mCurrentProviceName);
            object.put("city", mCurrentCityName);
            object.put("district", mCurrentDistrictName);
            object.put("detailAddress", edtDetailed.getText().toString().trim());
            if (isFirst) {
                object.put("isDefault", "1");
            } else {
                object.put("isDefault", "0");
            }
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805160", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(AddAddressActivity.this, "添加成功", Toast.LENGTH_SHORT).show();

                finish();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(AddAddressActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(AddAddressActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 新增收货地址
     */
    private void modifiAddress() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("addressee", edtName.getText().toString().trim());
            object.put("mobile", edtPhone.getText().toString().trim());
            object.put("province", mCurrentProviceName);
            object.put("city", mCurrentCityName);
            object.put("district", mCurrentDistrictName);
            object.put("detailAddress", edtDetailed.getText().toString().trim());
            object.put("isDefault", isDefault);
            object.put("token", userInfoSp.getString("token", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805162", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(AddAddressActivity.this, "添加成功", Toast.LENGTH_SHORT).show();

                finish();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(AddAddressActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(AddAddressActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getAddressDetail() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("token", userInfoSp.getString("token", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805166", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    mCurrentProviceName = jsonObject.getString("province");
                    mCurrentCityName = jsonObject.getString("city");
                    mCurrentDistrictName = jsonObject.getString("district");

                    edtName.setText(jsonObject.getString("addressee"));
                    edtPhone.setText(jsonObject.getString("mobile"));
                    txtAddress.setText(mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
                    edtDetailed.setText(jsonObject.getString("detailAddress"));

                    isDefault = jsonObject.getString("isDefault");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(AddAddressActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(AddAddressActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cityPicker(){
        CityPicker cityPicker = new CityPicker.Builder(AddAddressActivity.this)
                .textSize(18)
                .titleBackgroundColor("#ffffff")
                .titleTextColor("#ffffff")
                .backgroundPop(0xa0000000)
                .confirTextColor("#FE4332")
                .cancelTextColor("#FE4332")
                .province(userInfoSp.getString("province","北京市"))
                .city(userInfoSp.getString("city","北京市"))
                .district(userInfoSp.getString("district","昌平区"))
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();

        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                mCurrentProviceName = citySelected[0];
                //城市
                mCurrentCityName = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                mCurrentDistrictName = citySelected[2];
                //邮编
                String code = citySelected[3];

                txtAddress.setText(mCurrentProviceName + mCurrentCityName + mCurrentDistrictName);
            }
        });
    }


}
