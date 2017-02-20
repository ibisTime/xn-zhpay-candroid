package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

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
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.ProductAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.AddressModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.ProductModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
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

public class CommitOrderActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_consignee)
    TextView txtConsignee;
    @InjectView(R.id.txt_phone)
    TextView txtPhone;
    @InjectView(R.id.txt_address)
    TextView txtAddress;
    @InjectView(R.id.layout_address)
    RelativeLayout layoutAddress;
    @InjectView(R.id.layout_add)
    LinearLayout layoutAdd;
    @InjectView(R.id.view_line)
    View viewLine;
    @InjectView(R.id.layout_noAddress)
    LinearLayout layoutNoAddress;
    @InjectView(R.id.list_commodity)
    ListView listCommodity;
    @InjectView(R.id.txt_buy)
    TextView txtBuy;

    TextView txtRmb1;
    TextView txtRmb2;
    TextView txtRmb3;
    TextView txtGwb1;
    TextView txtGwb2;
    TextView txtGwb3;
    TextView txtQbb1;
    TextView txtQbb2;

    private String orderType;

    private ProductAdapter adapter;

    private List<ProductModel> productList;
    private List<AddressModel> addressList;
    private SharedPreferences userInfo;
    private SharedPreferences appConfigSp;

    private View footView;
    private TextView txtMoney;
    private EditText edtEnjoin;


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
        orderType = getIntent().getStringExtra("orderType");

        addressList = new ArrayList<>();
        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);


        userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);

    }

    private void initFootView() {
        footView = LayoutInflater.from(this).inflate(R.layout.foot_order, null);
        txtMoney = (TextView) footView.findViewById(R.id.txt_money);
        edtEnjoin = (EditText) footView.findViewById(R.id.edt_enjoin);

        txtRmb1 = (TextView) footView.findViewById(R.id.txt_rmb_1);
        txtRmb2 = (TextView) footView.findViewById(R.id.txt_rmb_2);
        txtRmb3 = (TextView) footView.findViewById(R.id.txt_rmb_3);
        txtGwb1 = (TextView) footView.findViewById(R.id.txt_gwb_1);
        txtGwb2 = (TextView) footView.findViewById(R.id.txt_gwb_2);
        txtGwb3 = (TextView) footView.findViewById(R.id.txt_gwb_3);
        txtQbb1 = (TextView) footView.findViewById(R.id.txt_qbb_1);
        txtQbb2 = (TextView) footView.findViewById(R.id.txt_qbb_2);
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
        for (int i=0; i<productList.size(); i++){
            rmb = sums(rmb,(productList.get(i).getPrice1()*productList.get(i).getProductNumber()));
            gwb = sums(gwb, (productList.get(i).getPrice2()*productList.get(i).getProductNumber()));
            qbb = sums(qbb, (productList.get(i).getPrice3()*productList.get(i).getProductNumber()));
        }

        if(rmb == 0){
            txtRmb1.setVisibility(View.GONE);
            txtRmb2.setVisibility(View.GONE);
            txtRmb3.setVisibility(View.GONE);
        }else{
            txtRmb1.setVisibility(View.VISIBLE);
            txtRmb2.setVisibility(View.VISIBLE);
            txtRmb3.setVisibility(View.VISIBLE);

            txtRmb2.setText(MoneyUtil.moneyFormatDouble(rmb));
            if(gwb == 0 && qbb == 0){
                txtRmb3.setText("");
            }
        }

        if(gwb == 0){
            txtGwb1.setVisibility(View.GONE);
            txtGwb2.setVisibility(View.GONE);
            txtGwb3.setVisibility(View.GONE);
        }else{
            txtGwb1.setVisibility(View.VISIBLE);
            txtGwb2.setVisibility(View.VISIBLE);
            txtGwb3.setVisibility(View.VISIBLE);

            txtGwb2.setText(MoneyUtil.moneyFormatDouble(gwb));
            if(qbb == 0){
                txtGwb3.setText("");
            }
        }

        if(qbb == 0){
            txtQbb1.setVisibility(View.GONE);
            txtQbb2.setVisibility(View.GONE);
        }else{
            txtQbb1.setVisibility(View.VISIBLE);
            txtQbb2.setVisibility(View.VISIBLE);

            txtQbb2.setText(MoneyUtil.moneyFormatDouble(qbb));
        }
    }


    /**
     * double 相加
     * @param d1
     * @param d2
     * @return
     */
    public double sums(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }


    @OnClick({R.id.layout_back, R.id.layout_address, R.id.layout_add, R.id.layout_noAddress, R.id.txt_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_address: // 有地址的时候
                startActivity(new Intent(CommitOrderActivity.this, AddressSelectActivity.class));
                break;

            case R.id.layout_add:
                startActivity(new Intent(CommitOrderActivity.this, AddAddressActivity.class));
                break;

            case R.id.txt_buy:
                if(addressList.size() == 0){
                    Toast.makeText(this, "请先选择收货地址", Toast.LENGTH_SHORT).show();
                }else{
                    if (orderType.equals("now")) {
                        buyNow();
                    } else {
                        buyShoppingCart();
                    }
                }


                break;
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
        } else {
            layoutAddress.setVisibility(View.VISIBLE);
            layoutNoAddress.setVisibility(View.GONE);

            txtConsignee.setText(addressList.get(0).getAddressee());
            txtPhone.setText(addressList.get(0).getMobile());
            txtAddress.setText("收货地址：" + addressList.get(0).getProvince() + " " + addressList.get(0).getCity() + " " + addressList.get(0).getDistrict() + "" + addressList.get(0).getDetailAddress());
        }

    }

    private void buyNow() {
        AddressModel model = addressList.get(0);
        String wholeAddress = model.getProvince() + " " + model.getCity() + " " + model.getDistrict() + " " + model.getDetailAddress();

        JSONObject object = new JSONObject();
        try {
            object.put("productCode", productList.get(0).getProductCode());
            object.put("quantity", productList.get(0).getProductNumber());
            object.put("receiver", addressList.get(0).getAddressee());
            object.put("reMobile", addressList.get(0).getMobile());
            object.put("reAddress", wholeAddress);
            object.put("applyUser", userInfo.getString("userId", null));
            object.put("applyNote", edtEnjoin.getText().toString().trim());
            object.put("receiptType", "");
            object.put("receiptTitle", "");
            object.put("token", userInfo.getString("token", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808050", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(CommitOrderActivity.this, "下单成功", Toast.LENGTH_SHORT).show();
                try {
//                    JSONArray jsonArray = new JSONArray(result);
//                    System.out.println("jsonArray.get(0).toString()="+jsonArray.get(0).toString());

                    startActivity(new Intent(CommitOrderActivity.this, GoodPayActivity.class)
                            .putExtra("rmb",rmb)
                            .putExtra("gwb",gwb)
                            .putExtra("qbb",qbb)
                            .putExtra("code", result));
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

    private void buyShoppingCart() {
        AddressModel model = addressList.get(0);
        String wholeAddress = model.getProvince() + " " + model.getCity() + " " + model.getDistrict() + " " + model.getDetailAddress();

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < productList.size(); i++) {
            jsonArray.put(productList.get(i).getProductCode());
        }

        JSONObject object = new JSONObject();
        try {
            object.put("receiver", addressList.get(0).getAddressee());
            object.put("reMobile", addressList.get(0).getMobile());
            object.put("reAddress", wholeAddress);
            object.put("applyUser", userInfo.getString("userId", null));
            object.put("applyNote", "");
            object.put("receiptType", "");
            object.put("receiptTitle", "");
            object.put("cartCodeList", jsonArray);
            object.put("token", userInfo.getString("token", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808051", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(CommitOrderActivity.this, "下单成功", Toast.LENGTH_SHORT).show();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("codeList");
                    System.out.println("jsonArray.get(0).toString()="+jsonArray.get(0).toString());

                    ArrayList<String> codeList = new ArrayList<String>();
                    for(int i=0; i<jsonArray.length(); i++){
                        codeList.add(jsonArray.get(i).toString());
                    }

                    startActivity(new Intent(CommitOrderActivity.this, GoodPayActivity.class)
                            .putStringArrayListExtra("codeList", codeList)
                            .putExtra("rmb",rmb)
                            .putExtra("gwb",gwb)
                            .putExtra("qbb",qbb)
                            .putExtra("shopCart",true));
                    finish();
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
