package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.AddressModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.DuoBaoWinModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class DuoBaoWinActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_consignee)
    TextView txtConsignee;
    @InjectView(R.id.txt_phone)
    TextView txtPhone;
    @InjectView(R.id.txt_address)
    TextView txtAddress;
    @InjectView(R.id.img_more)
    ImageView imgMore;
    @InjectView(R.id.layout_address)
    RelativeLayout layoutAddress;
    @InjectView(R.id.layout_add)
    LinearLayout layoutAdd;
    @InjectView(R.id.view_line)
    View viewLine;
    @InjectView(R.id.layout_noAddress)
    LinearLayout layoutNoAddress;
    @InjectView(R.id.img_pic)
    ImageView imgPic;
    @InjectView(R.id.txt_name)
    TextView txtName;
    @InjectView(R.id.txt_number)
    TextView txtNumber;
    @InjectView(R.id.txt_times)
    TextView txtTimes;
    @InjectView(R.id.txt_time)
    TextView txtTime;
    @InjectView(R.id.txt_confirm)
    TextView txtConfirm;

    String code;

    String receiver;
    String reMobile;
    String reAddress;

    DuoBaoWinModel model;
    SharedPreferences userInfoSp;

    private String[] bank = {"好评!", "中评!", "差评!"};

    ArrayList<AddressModel> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duo_bao_win);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        getDatas();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getDefaultAddress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        code = getIntent().getStringExtra("code");

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        addressList = new ArrayList<>();
    }

    @OnClick({R.id.layout_back, R.id.layout_add, R.id.txt_confirm, R.id.layout_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_add:
                startActivityForResult(new Intent(DuoBaoWinActivity.this, AddAddressActivity.class).putExtra("isDuoBao", true), 0);
                break;

            case R.id.txt_confirm:

                if (txtConfirm.getText().equals("确认收货地址")) {
                    if (receiver == null || reMobile == null || reAddress == null) {
                        Toast.makeText(this, "请添加收货地址", Toast.LENGTH_SHORT).show();
                    } else {
                        addDuoBaoAddress();
                    }
                } else if (txtConfirm.getText().equals("确认收货")) {
                    addOk();
                }

                break;

            case R.id.layout_address:

                if (model.getStatus().equals("2")) { // 选择收货地址
                    startActivity(new Intent(DuoBaoWinActivity.this, AddressSelectActivity.class));
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (!data.getStringExtra("receiver").equals("")) {

                receiver = data.getStringExtra("receiver");
                reMobile = data.getStringExtra("reMobile");
                reAddress = data.getStringExtra("reAddress");

                txtConsignee.setText(receiver);
                txtPhone.setText(reMobile);
                txtAddress.setText("收货地址：" + reAddress);

                layoutAddress.setVisibility(View.VISIBLE);
                layoutNoAddress.setVisibility(View.GONE);

            }
        }

    }

    private void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808317", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<DuoBaoWinModel>() {
                    }.getType());

                    setView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(DuoBaoWinActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DuoBaoWinActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {

        if (model.getStatus().equals("2")) { // 选择收货地址
            txtConfirm.setText("确认收货地址");
            txtConfirm.setVisibility(View.VISIBLE);
            imgMore.setVisibility(View.VISIBLE);

        } else if (model.getStatus().equals("4")) {
            txtConsignee.setText(model.getReceiver());
            txtPhone.setText(model.getReMobile());
            txtAddress.setText("收货地址：" + model.getReAddress());

            imgMore.setVisibility(View.GONE);
            txtConfirm.setVisibility(View.GONE);
            txtConfirm.setText("待发货");

        } else if (model.getStatus().equals("5")) {
            txtConsignee.setText(model.getReceiver());
            txtPhone.setText(model.getReMobile());
            txtAddress.setText("收货地址：" + model.getReAddress());

            imgMore.setVisibility(View.GONE);
            txtConfirm.setVisibility(View.VISIBLE);
            txtConfirm.setText("确认收货");

        } else if (model.getStatus().equals("6")) {
            txtConsignee.setText(model.getReceiver());
            txtPhone.setText(model.getReMobile());
            txtAddress.setText("收货地址：" + model.getReAddress());

            imgMore.setVisibility(View.GONE);
            txtConfirm.setVisibility(View.VISIBLE);
            txtConfirm.setText("已签收");
        }

        ImageUtil.glide(model.getJewel().getAdvPic(), imgPic, this);
        txtName.setText(model.getJewel().getName());
        txtNumber.setText(model.getJewel().getWinNumber());
        txtTimes.setText(model.getMyInvestTimes() + "人次");

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d5 = new Date(model.getJewel().getApproveDatetime());
        txtTime.setText(s.format(d5));

    }

    /**
     * 新增收货地址
     */
    private void addDuoBaoAddress() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", model.getCode());
            object.put("receiver", receiver);
            object.put("reMobile", reMobile);
            object.put("reAddress", reAddress);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808307", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(DuoBaoWinActivity.this, "确认成功", Toast.LENGTH_SHORT).show();
                getDatas();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(DuoBaoWinActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DuoBaoWinActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getDefaultAddress() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("code", "");
            object.put("isDefault", "1");
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("805165", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray jsonObject = new JSONArray(result);

                    Gson gson = new Gson();

                    ArrayList<AddressModel> lists = gson.fromJson(jsonObject.toString(), new TypeToken<ArrayList<AddressModel>>() {
                    }.getType());
                    addressList.clear();
                    addressList.addAll(lists);

                    setAddress();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(DuoBaoWinActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DuoBaoWinActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setAddress() {
        if (addressList.size() == 0) {
            layoutAddress.setVisibility(View.GONE);
            layoutNoAddress.setVisibility(View.VISIBLE);
        } else {
            layoutAddress.setVisibility(View.VISIBLE);
            layoutNoAddress.setVisibility(View.GONE);

            receiver = addressList.get(0).getAddressee();
            reMobile = addressList.get(0).getMobile();
            reAddress = addressList.get(0).getProvince() + addressList.get(0).getCity() + addressList.get(0).getDistrict() + addressList.get(0).getDetailAddress();

            txtConsignee.setText(addressList.get(0).getAddressee());
            txtPhone.setText(addressList.get(0).getMobile());
            txtAddress.setText("收货地址：" + addressList.get(0).getProvince() + " " + addressList.get(0).getCity() + " " + addressList.get(0).getDistrict() + "" + addressList.get(0).getDetailAddress());
        }

    }

    /**
     * 确认收货
     */
    private void addOk() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", model.getCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808308", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                evaluateTip();
//                Toast.makeText(DuoBaoWinActivity.this, "签收成功", Toast.LENGTH_SHORT).show();
                getDatas();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(DuoBaoWinActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DuoBaoWinActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void evaluateTip() {
        new AlertDialog.Builder(this).setTitle("宝贝收到了给个评价吧").setItems(
                bank, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            evaluate("A");
                        } else if (which == 1) {
                            evaluate("B");
                        } else {
                            evaluate("C");
                        }

                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 评价
     */
    private void evaluate(String evaluateType) {
        JSONObject object = new JSONObject();
        try {
            object.put("orderCode", model.getCode());
            object.put("jewelCode", model.getJewelCode());
            object.put("evaluateType", evaluateType);
            object.put("interacter", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808320", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(DuoBaoWinActivity.this, "评价成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(DuoBaoWinActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DuoBaoWinActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
