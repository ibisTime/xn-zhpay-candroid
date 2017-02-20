package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.ShopDetailsAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.ShopDetailsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ShopDetailsActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_title)
    TextView txtTitle;
    @InjectView(R.id.list_shop)
    ListView listShop;

    ImageView imgShopPic;
    ImageView imgDiscount;
    TextView txtPay;
    TextView txtName;
    TextView txtPhone;
    TextView txtPrice;
    TextView txtSlogan;
    TextView txtAddress;
    TextView txtDiscount;
    TextView txtDescription;
    TextView txtDiscountTime;
    LinearLayout layoutCall;
    LinearLayout layoutAddress;
    RelativeLayout layoutDiscount;
    LinearLayout layoutDiscountVisible;


    private View headView;

    private List<String> list;
    private ShopDetailsAdapter adapter;

    private String code;

    private ShopDetailsModel model;
    private SharedPreferences userInfoSp;

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initHeadView();
        initEvent();
        initListView();

        getDatas();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        code = getIntent().getStringExtra("code");

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        list = new ArrayList<>();
        adapter = new ShopDetailsAdapter(this,list);

    }

    private void initHeadView() {
        headView = LayoutInflater.from(this).inflate(R.layout.head_shop_details, null);

        imgShopPic = (ImageView) headView.findViewById(R.id.img_shopPic);
        imgDiscount = (ImageView) headView.findViewById(R.id.img_discount);

        txtPay = (TextView) headView.findViewById(R.id.txt_pay);
        txtName = (TextView) headView.findViewById(R.id.txt_name);
        txtPhone = (TextView) headView.findViewById(R.id.txt_phone);
        txtPrice = (TextView) headView.findViewById(R.id.txt_price);
        txtSlogan = (TextView) headView.findViewById(R.id.txt_slogan);
        txtAddress = (TextView) headView.findViewById(R.id.txt_address);
        txtDiscount = (TextView) headView.findViewById(R.id.txt_discount);
        txtDescription = (TextView) headView.findViewById(R.id.txt_description);
        txtDiscountTime = (TextView) headView.findViewById(R.id.txt_discountTime);

        layoutCall = (LinearLayout) headView.findViewById(R.id.layout_call);
        layoutAddress = (LinearLayout) headView.findViewById(R.id.layout_address);
        layoutDiscount = (RelativeLayout) headView.findViewById(R.id.layout_discount);
        layoutDiscountVisible = (LinearLayout) headView.findViewById(R.id.layout_discountVisible);
    }

    private void initEvent() {
        txtPay.setOnClickListener(new HeaderViewOnClickListener());
        imgShopPic.setOnClickListener(new HeaderViewOnClickListener());
        layoutCall.setOnClickListener(new HeaderViewOnClickListener());
        layoutAddress.setOnClickListener(new HeaderViewOnClickListener());
        layoutDiscount.setOnClickListener(new HeaderViewOnClickListener());
    }

    private void initListView() {
        listShop.addHeaderView(headView);
        listShop.setAdapter(adapter);
    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }

    /**
     * 获取商家详情
     */
    private void getDatas(){
        JSONObject object = new JSONObject();
        try {
            object.put("fromUser","");
            object.put("code",code);
            object.put("token",userInfoSp.getString("token",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("808209", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<ShopDetailsModel>(){}.getType());


                    String[] pic =  model.getPic().split("\\|\\|");
                    for (int i = 0; i<pic.length; i++){
                        list.add(pic[i]);
                    }

                    adapter.notifyDataSetChanged();
                    setView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(ShopDetailsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(ShopDetailsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setView() {
        txtTitle.setText(model.getName());
        txtName.setText(model.getName());
        txtAddress.setText(model.getCity()+model.getArea()+model.getAddress());
        txtPhone.setText(model.getBookMobile());

        ImageUtil.glide(model.getAdPic(),imgShopPic,this);

        if(model.getStoreTickets().size() > 0){
            layoutDiscountVisible.setVisibility(View.VISIBLE);
            txtPrice.setText(((int)model.getStoreTickets().get(0).getKey2()/1000)+"");
            txtSlogan.setText("满" + MoneyUtil.moneyFormatDouble(model.getStoreTickets().get(0).getKey1())
                    + "减" + MoneyUtil.moneyFormatDouble(model.getStoreTickets().get(0).getKey2()));
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            Date d5 = new Date(model.getStoreTickets().get(0).getValidateEnd());
            txtDiscountTime.setText(s.format(d5));
        }else{
            layoutDiscountVisible.setVisibility(View.GONE);
        }


        txtDescription.setText(model.getDescription());
    }



    public void inputMoney(View view){

        // 一个自定义的布局，作为显示的内容
        View viewPopup = LayoutInflater.from(this).inflate(
                R.layout.popup_money, null);

        final EditText edtMoney = (EditText) viewPopup.findViewById(R.id.edt_money);
        TextView txtConfirm = (TextView) viewPopup.findViewById(R.id.txt_confirm);

        popupWindow = new PopupWindow(viewPopup,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);
//        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtMoney.getText().toString().toString().equals("")){
//                    creatOrder(Integer.parseInt(edtMoney.getText().toString().trim())*1000);
                }else{
                    Toast.makeText(ShopDetailsActivity.this, "请输入消费金额", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.corners_layout));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

    private class HeaderViewOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.txt_pay:
//                    inputMoney(view);
                    if(userInfoSp.getString("userId",null) == null){
                        Toast.makeText(ShopDetailsActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ShopDetailsActivity.this,LoginActivity.class));
                    }else{
                        startActivity(new Intent(ShopDetailsActivity.this, ShopPayActivity.class).putExtra("code",code));
                    }
                    break;

                case R.id.img_shopPic:
                    startActivity(new Intent(ShopDetailsActivity.this,WebActivity.class).putExtra("webURL","www.baidu.com"));
                    break;

                case R.id.layout_call:
                    Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + model.getBookMobile()));
                    startActivity(phoneIntent);
                    break;

                case R.id.layout_address:
                    startActivity(new Intent(ShopDetailsActivity.this, MapActivity.class)
                            .putExtra("latitude",model.getLatitude())
                            .putExtra("longitude",model.getLongitude()));
                    break;

                case R.id.layout_discount:
                    startActivity(new Intent(ShopDetailsActivity.this, DiscountStoreActivity.class)
                            .putExtra("code",code)
                            .putExtra("name",model.getName())
                            .putExtra("phone",model.getBookMobile()));
                    break;
            }
        }
    }

}
