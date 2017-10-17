package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.ProductAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.AddressModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.ProductModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_002051;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_805165;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808050;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808088;

public class CommitOrderActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_commodity)
    ListView listCommodity;
    @InjectView(R.id.textView2)
    TextView textView2;
    @InjectView(R.id.txt_total_currency)
    TextView txtTotalCurrency;
    @InjectView(R.id.txt_total_price)
    TextView txtTotalPrice;
    @InjectView(R.id.txt_yunfei)
    TextView txtYunfei;
    @InjectView(R.id.txt_pay)
    TextView txtPay;

    private String yunfei;
    private String currency;
    private String orderType;

    private ProductAdapter adapter;

    private List<ProductModel> productList;
    private List<AddressModel> addressList;
    private SharedPreferences userInfo;
    private SharedPreferences appConfigSp;

    private View footView;
    private TextView txtMoney;

    private EditText edtEnjoin;

    private TextView txtPhone;
    private TextView txtAddress;
    private TextView txtConsignee;

    private LinearLayout layoutAdd;
    private RelativeLayout layoutAddress;
    private LinearLayout layoutNoAddress;

    private double rate = 1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_now);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initFootView();
        setView();
        initListView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getDefaultAddress();

    }

    private void inits() {
        // 获取订单类型
        currency = getIntent().getStringExtra("currency");
        orderType = getIntent().getStringExtra("orderType");

        addressList = new ArrayList<>();
        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);


        userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);

    }

    private void initFootView() {
        footView = LayoutInflater.from(this).inflate(R.layout.foot_order, null);
        edtEnjoin = (EditText) footView.findViewById(R.id.edt_enjoin);

        txtPhone = (TextView) footView.findViewById(R.id.txt_phone);
        txtAddress = (TextView) footView.findViewById(R.id.txt_address);
        txtConsignee = (TextView) footView.findViewById(R.id.txt_consignee);

        layoutAdd = (LinearLayout) footView.findViewById(R.id.layout_add);
        layoutAddress = (RelativeLayout) footView.findViewById(R.id.layout_address);
        layoutNoAddress = (LinearLayout) footView.findViewById(R.id.layout_noAddress);

        layoutAdd.setOnClickListener(new FootViewOnClickListener());
        layoutAddress.setOnClickListener(new FootViewOnClickListener());
    }

    private void setView() {
        if (orderType.equals("now")) {
            ProductModel productModel = (ProductModel) getIntent().getSerializableExtra("productModel");
            productList.add(productModel);
        } else {
            List<ProductModel> productModelList = (List<ProductModel>) getIntent().getSerializableExtra("productModel");
            productList.addAll(productModelList);
        }

        initTotalPrice();
    }

    private void initListView() {
        listCommodity.addFooterView(footView);
        listCommodity.setAdapter(adapter);
    }

    // 人名币总价
    double rmb = 0;
    // 购物币总价
    double gwb = 0;
    // 钱包币总价
    double qbb = 0;

    private void initTotalPrice() {
        for (int i = 0; i < productList.size(); i++) {
            rmb = sums(rmb, (productList.get(i).getPrice1() * productList.get(i).getProductNumber()));
        }

        if (productList.get(0).getType().equals("G01")){
            txtTotalCurrency.setText("礼品券");
        }else {
            if (productList.get(0).getPayCurrency().equals("4")){
                txtTotalCurrency.setText("钱包币");
            }else{
                txtTotalCurrency.setText("¥");
            }
        }
        txtTotalPrice.setText(MoneyUtil.moneyFormatDouble(rmb));

    }


    /**
     * double 相加
     *
     * @param d1
     * @param d2
     * @return
     */
    public double sums(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }


    @OnClick({R.id.layout_back, R.id.txt_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_pay:
                if (addressList.size() == 0) {
                    Toast.makeText(this, "请先选择收货地址", Toast.LENGTH_SHORT).show();
                } else {
//                    if (orderType.equals("now")) {
                    buyNow();
//                    } else {
//                        buyShoppingCart();
//                    }
                }


                break;
        }
    }

    private class FootViewOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_address: // 有地址的时候
                    startActivity(new Intent(CommitOrderActivity.this, AddressSelectActivity.class));
                    break;

                case R.id.layout_add:
                    startActivity(new Intent(CommitOrderActivity.this, AddressSelectActivity.class));
                    break;
            }
        }
    }

    public void getDefaultAddress() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfo.getString("userId", null));
            object.put("code", "");
            object.put("isDefault", "1");
            object.put("token", userInfo.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_805165, object.toString(), new Xutil.XUtils3CallBackPost() {
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
                Toast.makeText(CommitOrderActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(CommitOrderActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setAddress() {
        if (addressList.size() == 0) {
            layoutAddress.setVisibility(View.GONE);
            layoutNoAddress.setVisibility(View.VISIBLE);

            txtYunfei.setText("0");
        } else {
            layoutAddress.setVisibility(View.VISIBLE);
            layoutNoAddress.setVisibility(View.GONE);

            txtConsignee.setText(addressList.get(0).getAddressee());
            txtPhone.setText(addressList.get(0).getMobile());
            txtAddress.setText("收货地址：" + addressList.get(0).getProvince() + " " + addressList.get(0).getCity() + " " + addressList.get(0).getDistrict() + "" + addressList.get(0).getDetailAddress());

            if (productList.get(0).getType().equals("G01")) {
                getRate();
            } else {
                getYunfei(addressList.get(0).getProvince());
            }
        }

    }

    private void buyNow() {
        AddressModel model = addressList.get(0);
        String wholeAddress = model.getProvince() + " " + model.getCity() + " " + model.getDistrict() + " " + model.getDetailAddress();

        JSONObject object = new JSONObject();
        try {
            object.put("productSpecsCode", productList.get(0).getProductSpecsCode());
            object.put("quantity", productList.get(0).getProductNumber());
            object.put("receiver", addressList.get(0).getAddressee());
            object.put("reMobile", addressList.get(0).getMobile());
            object.put("reAddress", wholeAddress);
            object.put("applyUser", userInfo.getString("userId", null));
            object.put("applyNote", edtEnjoin.getText().toString().trim());
            object.put("token", userInfo.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808050, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(CommitOrderActivity.this, "下单成功", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
//                    System.out.println("jsonArray.get(0).toString()="+jsonArray.get(0).toString());

                    startActivity(new Intent(CommitOrderActivity.this, GoodPayActivity.class)
                            .putExtra("rmb", rmb)
                            .putExtra("gwb", gwb)
                            .putExtra("qbb", qbb)
                            .putExtra("currency", currency)
                            .putExtra("type", productList.get(0).getType())
                            .putExtra("payCurrency", productList.get(0).getPayCurrency())
                            .putExtra("yunfei", Double.parseDouble(yunfei) * 1000)
                            .putExtra("code", jsonObject.getString("code")));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(CommitOrderActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(CommitOrderActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private void buyShoppingCart() {
//        AddressModel model = addressList.get(0);
//        String wholeAddress = model.getProvince() + " " + model.getCity() + " " + model.getDistrict() + " " + model.getDetailAddress();
//
//        JSONArray jsonArray = new JSONArray();
//        for (int i = 0; i < productList.size(); i++) {
//            jsonArray.put(productList.get(i).getProductCode());
//        }
//
//        JSONObject pojo = new JSONObject();
//        try {
//            pojo.put("receiver", addressList.get(0).getAddressee());
//            pojo.put("reMobile", addressList.get(0).getMobile());
//            pojo.put("reAddress", wholeAddress);
//            pojo.put("applyUser", userInfo.getString("userId", null));
//            pojo.put("applyNote", edtEnjoin.getText().toString().trim());
//            pojo.put("receiptType", "");
//            pojo.put("receiptTitle", "");
//            pojo.put("systemCode", appConfigSp.getString("systemCode", null));
//            pojo.put("companyCode", appConfigSp.getString("systemCode", null));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        JSONObject object = new JSONObject();
//        try {
//            object.put("toUser", "");
//            object.put("pojo", pojo);
//            object.put("cartCodeList", jsonArray);
//            object.put("token", userInfo.getString("token", null));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        new Xutil().post("808051", object.toString(), new Xutil.XUtils3CallBackPost() {
//            @Override
//            public void onSuccess(String result) {
//                Toast.makeText(CommitOrderActivity.this, "下单成功", Toast.LENGTH_SHORT).show();
//
//                JSONArray jsonArray = null;
//                try {
//                    jsonArray = new JSONArray(result);
//                    System.out.println("jsonArray.get(0).toString()="+jsonArray.get(0).toString());
//
//                    ArrayList<String> codeList = new ArrayList<String>();
//                    for(int i=0; i<jsonArray.length(); i++){
//                        codeList.add(jsonArray.get(i).toString());
//                    }
//
//                    startActivity(new Intent(CommitOrderActivity.this, GoodPayActivity.class)
//                            .putStringArrayListExtra("codeList", codeList)
//                            .putExtra("rmb",rmb)
//                            .putExtra("gwb",gwb)
//                            .putExtra("qbb",qbb)
//                            .putExtra("yunfei",Double.parseDouble(yunfei)*(1000 * jsonArray.length()))
//                            .putExtra("shopCart",true));
//                    finish();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//            @Override
//            public void onTip(String tip) {
//                Toast.makeText(CommitOrderActivity.this, tip, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(String error, boolean isOnCallback) {
//                Toast.makeText(CommitOrderActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

    public void getYunfei(String endPoint) {
        JSONObject object = new JSONObject();
        try {
            object.put("startPoint", productList.get(0).getStartPoin());
            object.put("endPoint", endPoint);
            object.put("token", userInfoSp.getString("token", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808088, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    yunfei = MoneyUtil.moneyFormatDouble(jsonObject.getDouble("price") * rate);

                    if (productList.get(0).getType().equals("G01")) {
                        txtYunfei.setText(yunfei);
                    } else {
                        txtYunfei.setText(yunfei);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(CommitOrderActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(CommitOrderActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getRate() {
        JSONObject object = new JSONObject();
        try {
            object.put("fromCurrency", "CNY");
            object.put("toCurrency", "LPQ");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_002051, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    rate = jsonObject.getDouble("rate");
                    getYunfei(addressList.get(0).getProvince());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(CommitOrderActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(CommitOrderActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

}