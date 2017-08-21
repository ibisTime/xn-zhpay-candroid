package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.ShoppingCartAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.ProductModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.ShoppingCartModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808045;

public class ShoppingCartActivity extends MyBaseActivity {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.list_shopcart)
    ListView listShopcart;
    @InjectView(R.id.img_allChoose)
    ImageView imgAllChoose;
    @InjectView(R.id.layout_allChoose)
    LinearLayout layoutAllChoose;
    @InjectView(R.id.txt_rmb_1)
    TextView txtRmb1;
    @InjectView(R.id.txt_rmb_2)
    TextView txtRmb2;
    @InjectView(R.id.txt_rmb_3)
    TextView txtRmb3;
    @InjectView(R.id.txt_gwb_1)
    TextView txtGwb1;
    @InjectView(R.id.txt_gwb_2)
    TextView txtGwb2;
    @InjectView(R.id.txt_gwb_3)
    TextView txtGwb3;
    @InjectView(R.id.txt_qbb_1)
    TextView txtQbb1;
    @InjectView(R.id.txt_qbb_2)
    TextView txtQbb2;
    @InjectView(R.id.txt_pay)
    TextView txtPay;
    private List<ShoppingCartModel> list;
    private ShoppingCartModel model;
    private ShoppingCartAdapter adapter;

    private SharedPreferences userInfoSp;

    private ShoppingCartActivity activity;

    // 购物车是否全选 全选true 未全选false
    private boolean isAllChoose = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
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
        getDatas();
    }

    private void inits() {
        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        list = new ArrayList<>();
        activity = this;
    }

    private void initListView() {
        adapter = new ShoppingCartAdapter(ShoppingCartActivity.this, list, activity);
        listShopcart.setAdapter(adapter);
    }


    @OnClick({R.id.layout_back, R.id.layout_allChoose, R.id.txt_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_allChoose:
                allChoose();
                break;

            case R.id.txt_pay:
                List<ProductModel> productModel = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {

                    if (list.get(i).getProduct().isChoose()) {
                        ProductModel model = new ProductModel();
                        model.setProductCode(list.get(i).getCode());
                        model.setProductName(list.get(i).getProduct().getName());
                        model.setProductImage(list.get(i).getProduct().getAdvPic());
                        model.setProductNumber(list.get(i).getQuantity());
                        model.setPrice1(list.get(i).getProduct().getPrice1());
                        model.setPrice2(list.get(i).getProduct().getPrice2());
                        model.setPrice3(list.get(i).getProduct().getPrice3());
                        productModel.add(model);
                    }

                }

                if(productModel.size() == 0){
                    Toast.makeText(ShoppingCartActivity.this, "请选择至少一件需要结算的货物", Toast.LENGTH_SHORT).show();
                }else{
                    startActivity(new Intent(ShoppingCartActivity.this, CommitOrderActivity.class)
                            .putExtra("orderType", "shoppingCart")
                            .putExtra("productModel", (Serializable) productModel));
                }

                break;
        }
    }

    public void getDatas() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("start", "0");
            object.put("limit", "10");
            object.put("orderColumn", "");
            object.put("orderDir", "");
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(CODE_808045, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();

                    List<ShoppingCartModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<ShoppingCartModel>>() {
                    }.getType());

                    list.clear();
                    list.addAll(lists);
                    adapter.notifyDataSetChanged();

                    getTotalPrice();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ShoppingCartActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ShoppingCartActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void allChoose() {
        if (isAllChoose) { // 全选时
            for (int i = 0; i < list.size(); i++) {
                list.get(i).getProduct().setChoose(false);
            }
            isAllChoose = false;
            imgAllChoose.setBackgroundResource(R.mipmap.shopcart_unchoose);
        } else { // 未全选时
            for (int i = 0; i < list.size(); i++) {
                list.get(i).getProduct().setChoose(true);
            }
            isAllChoose = true;
            imgAllChoose.setBackgroundResource(R.mipmap.shopcart_choose);
        }

        getTotalPrice();
        adapter.notifyDataSetChanged();
    }


    public void getTotalPrice() {
        // 人名币总价
        double rmb = 0;
        // 购物币总价
        int gwb = 0;
        // 钱包币总价
        int qbb = 0;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getProduct().isChoose()) {
                rmb = sums(rmb, (list.get(i).getProduct().getPrice1()*(list.get(i).getQuantity())));
                gwb = gwb + (list.get(i).getProduct().getPrice2()*(list.get(i).getQuantity()));
                qbb = qbb + (list.get(i).getProduct().getPrice3()*(list.get(i).getQuantity()));
            }
        }

        if (rmb == 0) {
            txtRmb1.setVisibility(View.GONE);
            txtRmb2.setVisibility(View.GONE);
            txtRmb3.setVisibility(View.GONE);
        } else {
            txtRmb1.setVisibility(View.VISIBLE);
            txtRmb2.setVisibility(View.VISIBLE);
            txtRmb3.setVisibility(View.VISIBLE);

            txtRmb2.setText(MoneyUtil.moneyFormatDouble(rmb));
            if (gwb == 0 && qbb == 0) {
                txtRmb3.setText("");
            }else{
                txtRmb3.setText(" + ");
            }
        }

        if (gwb == 0) {
            txtGwb1.setVisibility(View.GONE);
            txtGwb2.setVisibility(View.GONE);
            txtGwb3.setVisibility(View.GONE);
        } else {
            txtGwb1.setVisibility(View.VISIBLE);
            txtGwb2.setVisibility(View.VISIBLE);
            txtGwb3.setVisibility(View.VISIBLE);

            txtGwb2.setText((gwb / 1000) + "");
            if (qbb == 0) {
                txtGwb3.setText("");
            }else{
                txtGwb3.setText(" + ");
            }
        }

        if (qbb == 0) {
            txtQbb1.setVisibility(View.GONE);
            txtQbb2.setVisibility(View.GONE);
        } else {
            txtQbb1.setVisibility(View.VISIBLE);
            txtQbb2.setVisibility(View.VISIBLE);

            txtQbb2.setText((qbb / 1000) + "");
        }

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

}
