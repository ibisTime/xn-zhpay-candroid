package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.MyShopModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MyShopDetailsActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_title)
    TextView txtTitle;
    @InjectView(R.id.txt_orderId)
    TextView txtOrderId;
    @InjectView(R.id.txt_time)
    TextView txtTime;
    @InjectView(R.id.txt_status)
    TextView txtStatus;
    @InjectView(R.id.img_shopPic)
    ImageView imgShopPic;
    @InjectView(R.id.txt_shopName)
    TextView txtShopName;
    @InjectView(R.id.txt_discount)
    TextView txtDiscount;
    @InjectView(R.id.layout_discount)
    LinearLayout layoutDiscount;
    @InjectView(R.id.txt_price)
    TextView txtPrice;
    @InjectView(R.id.txt_real)
    TextView txtReal;
    @InjectView(R.id.txt_distance)
    TextView txtDistance;
    @InjectView(R.id.activity_my_shop_details)
    LinearLayout activityMyShopDetails;

    private MyShopModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop_details);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        setView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        model = (MyShopModel) getIntent().getSerializableExtra("model");
    }

    private void setView() {
        txtOrderId.setText("订单号:" + model.getCode());
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d5 = new Date(model.getCreateDatetime());
        txtTime.setText("下单时间:"+s.format(d5));

        ImageUtil.glide(model.getStore().getAdvPic(), imgShopPic, this);

        txtShopName.setText(model.getStore().getName());

        if (model.getStatus().equals("0")) {
            txtStatus.setText("订单状态:待支付");
        } else if (model.getStatus().equals("1")) {
            txtStatus.setText("订单状态:已支付");
        } else {
            txtStatus.setText("订单状态:已取消");
        }

        txtDiscount.setText(model.getStore().getSlogan());

//        if (model.getStoreTicket() == null) {
//            layoutDiscount.setVisibility(View.INVISIBLE);
//        } else {
//            double key1 = model.getStoreTicket().getKey1();
//            double key2 = model.getStoreTicket().getKey2();
//            txtDiscount.setText("满" + MoneyUtil.moneyFormatDouble(key1) + "减" + MoneyUtil.moneyFormatDouble(key2));
//        }

        txtPrice.setText("消费: ¥" + MoneyUtil.moneyFormatDouble(model.getPrice()));

//        if(model.getPayType().equals("1")){
//            txtReal.setText("实付:"+
//                    MoneyUtil.moneyFormatDouble(model.getPayAmount2()
//                            +model.getPayAmount3()));
//        }else {
//            txtReal.setText("实付:"+ MoneyUtil.moneyFormatDouble(model.getPayAmount1()));
//        }
//        if (model.getPayType().equals("1")) {
//            txtReal.setText("实付:" + MoneyUtil.moneyFormatDouble(model.getAmount2() + model.getAmount3()));
//        } else {
//            txtReal.setText("实付:" + MoneyUtil.moneyFormatDouble(model.getAmount1()));
//        }

    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }
}
