package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.ShopDetailsAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.ShopDetailsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.WxUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.R.id.layout_good;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808218;

public class ShopDetailsActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_title)
    TextView txtTitle;
    @InjectView(R.id.list_shop)
    ListView listShop;
    @InjectView(R.id.layout_share)
    LinearLayout layoutShare;
    @InjectView(R.id.txt_pay)
    TextView txtPay;
    @InjectView(R.id.txt_buy)
    TextView txtBuy;

    ImageView imgShopPic;
    ImageView imgDiscount;
    TextView txtName;
    TextView txtPhone;
    TextView txtPrice;
    TextView txtSlogan;
    TextView txtAddress;
    TextView txtAdvText;
    TextView txtDiscount;
    TextView txtDescription;
    TextView txtDiscountTime;
    LinearLayout layoutCall;
    LinearLayout layoutGood;
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

    private String shareURL;

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

    }

    @Override
    protected void onResume() {
        super.onResume();
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
        adapter = new ShopDetailsAdapter(this, list);

        shareURL = Xutil.SHARE_URL + Xutil.SHARE_PORT + "/share/store.html?code="+code;

    }

    private void initHeadView() {
        headView = LayoutInflater.from(this).inflate(R.layout.head_shop_details, null);

        imgShopPic = (ImageView) headView.findViewById(R.id.img_shopPic);
        imgDiscount = (ImageView) headView.findViewById(R.id.img_discount);

        txtName = (TextView) headView.findViewById(R.id.txt_name);
        txtPhone = (TextView) headView.findViewById(R.id.txt_phone);
        txtPrice = (TextView) headView.findViewById(R.id.txt_price);
        txtSlogan = (TextView) headView.findViewById(R.id.txt_slogan);
        txtAddress = (TextView) headView.findViewById(R.id.txt_address);
        txtAdvText = (TextView) headView.findViewById(R.id.txt_advText);
        txtDiscount = (TextView) headView.findViewById(R.id.txt_discount);
        txtDescription = (TextView) headView.findViewById(R.id.txt_description);
        txtDiscountTime = (TextView) headView.findViewById(R.id.txt_discountTime);

        layoutCall = (LinearLayout) headView.findViewById(R.id.layout_call);
        layoutGood = (LinearLayout) headView.findViewById(layout_good);
        layoutAddress = (LinearLayout) headView.findViewById(R.id.layout_address);
        layoutDiscount = (RelativeLayout) headView.findViewById(R.id.layout_discount);
        layoutDiscountVisible = (LinearLayout) headView.findViewById(R.id.layout_discountVisible);
    }

    private void initEvent() {
        imgShopPic.setOnClickListener(new HeaderViewOnClickListener());
        layoutCall.setOnClickListener(new HeaderViewOnClickListener());
        layoutGood.setOnClickListener(new HeaderViewOnClickListener());
        layoutAddress.setOnClickListener(new HeaderViewOnClickListener());
        layoutDiscount.setOnClickListener(new HeaderViewOnClickListener());
    }

    private void initListView() {
        listShop.addHeaderView(headView);
        listShop.setAdapter(adapter);
    }


    /**
     * 获取商家详情
     */
    private void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("fromUser", "");
            object.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(CODE_808218, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<ShopDetailsModel>() {
                    }.getType());


                    String[] pic = model.getPic().split("\\|\\|");
                    for (int i = 0; i < pic.length; i++) {
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

        if (model.getType().equals("G01")){
            txtBuy.setText("购买礼品券");
        }else {
            txtBuy.setText("购买联盟券");
        }

        txtTitle.setText(model.getName());
        txtName.setText(model.getName());
        txtAddress.setText(model.getProvince() + model.getCity() + model.getArea() + model.getAddress());
        txtAdvText.setText(model.getSlogan());
        txtPhone.setText(model.getBookMobile());

        ImageUtil.glide(model.getAdvPic(), imgShopPic, this);

        txtDescription.setText(model.getDescription());
    }

    @OnClick({R.id.layout_back, R.id.layout_share, R.id.txt_pay, R.id.txt_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_share:
                showShare(view);
                break;

            case R.id.txt_pay:
                if (userInfoSp.getString("userId", null) == null) {
                    Toast.makeText(ShopDetailsActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ShopDetailsActivity.this, LoginActivity.class));
                } else {
                    if (model.getType().equals("G01")){
                        startActivity(new Intent(ShopDetailsActivity.this, GiftPayActivity.class)
                                .putExtra("code", code));
                    }else {
                        startActivity(new Intent(ShopDetailsActivity.this, ShopPayActivity.class)
                                .putExtra("currency", model.getPayCurrency())
                                .putExtra("code", code));
                    }

                }
                break;

            case R.id.txt_buy:
                if (userInfoSp.getString("userId", null) == null) {
                    Toast.makeText(ShopDetailsActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ShopDetailsActivity.this, LoginActivity.class));
                } else {
                    if (model.getType().equals("G01")){
                        startActivity(new Intent(ShopDetailsActivity.this, GiftBuyActivity.class)
                                .putExtra("type", "gift")
                                .putExtra("code", code));
                    }else {
                        startActivity(new Intent(ShopDetailsActivity.this, GiftBuyActivity.class)
                                .putExtra("type", "lm")
                                .putExtra("code", code));
                    }

                }
                break;
        }
    }

    private class HeaderViewOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_shopPic:
//                    startActivity(new Intent(ShopDetailsActivity.this,WebActivity.class).putExtra("webURL","www.baidu.com"));
                    break;

                case R.id.layout_call:
                    Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + model.getBookMobile()));
                    startActivity(phoneIntent);
                    break;

                case R.id.layout_address:
                    startActivity(new Intent(ShopDetailsActivity.this, MapActivity.class)
                            .putExtra("latitude", model.getLatitude())
                            .putExtra("longitude", model.getLongitude()));
                    break;

                case R.id.layout_discount:
                    startActivity(new Intent(ShopDetailsActivity.this, DiscountStoreActivity.class)
                            .putExtra("code", code)
                            .putExtra("name", model.getName())
                            .putExtra("phone", model.getBookMobile()));
                    break;

                case R.id.layout_good:
                    startActivity(new Intent(ShopDetailsActivity.this, ShopGoodActivity.class)
                            .putExtra("owner",model.getOwner()));
                    break;
            }
        }
    }

    private void showShare(View view) {

        // 一个自定义的布局，作为显示的内容
        View mview = LayoutInflater.from(this).inflate(R.layout.popup_share, null);

        ImageView qrCode = (ImageView) mview.findViewById(R.id.img_QRCode);
        ImageView wx = (ImageView) mview.findViewById(R.id.img_wx);
        ImageView pyq = (ImageView) mview.findViewById(R.id.img_pyq);
        TextView qx = (TextView) mview.findViewById(R.id.txt_cancel);

        final PopupWindow popupWindow = new PopupWindow(mview,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        qrCode.setVisibility(View.GONE);

        wx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                WxUtil.shareToWX(ShopDetailsActivity.this, shareURL, model.getName(), model.getSlogan());
                popupWindow.dismiss();
            }
        });

        pyq.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                WxUtil.shareToPYQ(ShopDetailsActivity.this, shareURL, model.getName(), model.getSlogan());
                popupWindow.dismiss();

            }
        });
        qx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_layout));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    private void showGift(View view) {

        // 一个自定义的布局，作为显示的内容
        View mview = LayoutInflater.from(this).inflate(R.layout.popup_shop, null);

        TextView txtPay = (TextView) mview.findViewById(R.id.txt_pay);
        TextView txtBuy = (TextView) mview.findViewById(R.id.txt_buy);

        final PopupWindow popupWindow = new PopupWindow(mview,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                popupWindow.dismiss();
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        txtPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopDetailsActivity.this, GiftPayActivity.class)
                        .putExtra("code", code));
                popupWindow.dismiss();
            }
        });

        txtBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopDetailsActivity.this, GiftBuyActivity.class)
                        .putExtra("code", code));
                popupWindow.dismiss();
            }
        });


        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_layout));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

}
