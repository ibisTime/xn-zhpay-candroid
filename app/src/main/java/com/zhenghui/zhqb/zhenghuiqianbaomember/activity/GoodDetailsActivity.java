package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.PagerAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.ParameterAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.fragment.CommodityFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.fragment.DetailFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.fragment.EvaluateFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.GoodsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.ProductModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.LoginUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.WxUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808026;

public class GoodDetailsActivity extends MyBaseActivity {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.layout_share)
    LinearLayout layoutShare;
    @InjectView(R.id.txt_commodity)
    TextView txtCommodity;
    @InjectView(R.id.line_commodity)
    View lineCommodity;
    @InjectView(R.id.txt_detail)
    TextView txtDetail;
    @InjectView(R.id.line_detail)
    View lineDetail;
    @InjectView(R.id.txt_evaluate)
    TextView txtEvaluate;
    @InjectView(R.id.line_evaluate)
    View lineEvaluate;
    @InjectView(R.id.layout_content)
    FrameLayout layoutContent;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.layout_buyNow)
    LinearLayout layoutBuyNow;

    // 店铺类别
    private List<Fragment> fragments;
    private PagerAdapter pageAdapter;

    // 内容fragment
    private Fragment commodityFragment;
    private Fragment detailFragment;
    private Fragment evaluateFragment;

    private String shareURL;

    private String code;
    private GoodsModel model;

    // 商品数量
    public int number = 1;
    public int index = 0;

    //
    private ParameterAdapter adapter;
    private List<GoodsModel.ProductSpecsListBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_details);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        initViewPager();
        initFragment();
        getDatas();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        code = getIntent().getStringExtra("code");

        list = new ArrayList<>();
        adapter = new ParameterAdapter(this,list);

        fragments = new ArrayList<>();
        //初始化pageAdapter
        pageAdapter = new PagerAdapter(this.getSupportFragmentManager(),
                fragments);

        shareURL = Xutil.SHARE_URL + Xutil.SHARE_PORT + "/share/share-product.html?code=" + code;
    }

    private void initViewPager() {
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetImgs();
                switch (position) {
                    case 0:
                        txtCommodity.setTextColor(getResources().getColor(R.color.fontColor_orange));
                        lineCommodity.setVisibility(View.VISIBLE);
                        break;

                    case 1:
                        txtDetail.setTextColor(getResources().getColor(R.color.fontColor_orange));
                        lineDetail.setVisibility(View.VISIBLE);
                        break;

                    case 2:
                        txtEvaluate.setTextColor(getResources().getColor(R.color.fontColor_orange));
                        lineEvaluate.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        viewpager.setAdapter(pageAdapter);
    }

    private void initFragment() {
        commodityFragment = CommodityFragment.newInstance(model);
        detailFragment = DetailFragment.newInstance(model);
        evaluateFragment = EvaluateFragment.newInstance(model);
    }

    /**
     * 选择Fragment
     *
     * @param i
     */
    private void setSelect(int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        // 把图片设置为亮的
        // 设置内容区域
        switch (i) {
            case 0:
                if (commodityFragment == null) {
                    commodityFragment = CommodityFragment.newInstance(model);
//                    commodityFragment = new CommodityFragment();
                    transaction.add(R.id.layout_content, commodityFragment);
                } else {
                    transaction.show(commodityFragment);
                }
                txtCommodity.setTextColor(getResources().getColor(R.color.fontColor_orange));
                lineCommodity.setVisibility(View.VISIBLE);
                break;

            case 1:
                if (detailFragment == null) {
                    detailFragment = DetailFragment.newInstance(model);
                    transaction.add(R.id.layout_content, detailFragment);
                } else {
                    transaction.show(detailFragment);

                }
                txtDetail.setTextColor(getResources().getColor(R.color.fontColor_orange));
                lineDetail.setVisibility(View.VISIBLE);
                break;

            case 2:
                if (evaluateFragment == null) {
                    evaluateFragment = EvaluateFragment.newInstance(model);
                    transaction.add(R.id.layout_content, evaluateFragment);
                } else {
                    transaction.show(evaluateFragment);
                }
                txtEvaluate.setTextColor(getResources().getColor(R.color.fontColor_orange));
                lineEvaluate.setVisibility(View.VISIBLE);
                break;


        }

        transaction.commit();
    }


    private void hideFragment(FragmentTransaction transaction) {
        if (commodityFragment != null) {
            transaction.hide(commodityFragment);
        }
        if (detailFragment != null) {
            transaction.hide(detailFragment);
        }
        if (evaluateFragment != null) {
            transaction.hide(evaluateFragment);
        }
    }

    /**
     * 切换图片至暗色
     */
    private void resetImgs() {
        txtDetail.setTextColor(getResources().getColor(R.color.fontColor_gray));
        txtEvaluate.setTextColor(getResources().getColor(R.color.fontColor_gray));
        txtCommodity.setTextColor(getResources().getColor(R.color.fontColor_gray));

        lineDetail.setVisibility(View.INVISIBLE);
        lineEvaluate.setVisibility(View.INVISIBLE);
        lineCommodity.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.layout_back, R.id.layout_share,R.id.layout_buyNow, R.id.txt_commodity, R.id.txt_detail, R.id.txt_evaluate})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_share:
                showShare(view);

                break;

            case R.id.layout_buyNow:

                if (userInfoSp.getString("userId", null) != null) {
                    pay(view);
                } else {
                    LoginUtil.toLogin(GoodDetailsActivity.this);
                }


                break;


            case R.id.txt_commodity:
                resetImgs();
                txtCommodity.setTextColor(getResources().getColor(R.color.fontColor_orange));
                lineCommodity.setVisibility(View.VISIBLE);

                viewpager.setCurrentItem(0);
                break;

            case R.id.txt_detail:
                resetImgs();
                txtDetail.setTextColor(getResources().getColor(R.color.fontColor_orange));
                lineDetail.setVisibility(View.VISIBLE);

                viewpager.setCurrentItem(1);
                break;

            case R.id.txt_evaluate:
                resetImgs();
                txtEvaluate.setTextColor(getResources().getColor(R.color.fontColor_orange));
                lineEvaluate.setVisibility(View.VISIBLE);

                viewpager.setCurrentItem(2);
                break;

        }
    }

    /**
     * 获取商品详情
     */
    public void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808026, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<GoodsModel>() {
                    }.getType());

                    commodityFragment = CommodityFragment.newInstance(model);
                    detailFragment = DetailFragment.newInstance(model);
                    evaluateFragment = EvaluateFragment.newInstance(model);

                    if (null == model.getProductSpecsList()) {
                        fragments.clear();
                        fragments.add(commodityFragment);
                        fragments.add(detailFragment);
                        fragments.add(evaluateFragment);
                    } else {
                        if (model.getProductSpecsList().size() == 0) {
                            fragments.clear();
                            fragments.add(commodityFragment);
                            fragments.add(detailFragment);
                            fragments.add(evaluateFragment);
                        } else {
                            fragments.clear();
                            fragments.add(commodityFragment);
                            fragments.add(detailFragment);
                            fragments.add(evaluateFragment);
                        }

                    }

                    pageAdapter.notifyDataSetChanged();
//                    setSelect(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(GoodDetailsActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(GoodDetailsActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

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

                WxUtil.shareToWX(GoodDetailsActivity.this, shareURL, model.getName(), model.getSlogan());
                popupWindow.dismiss();
            }
        });

        pyq.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                WxUtil.shareToPYQ(GoodDetailsActivity.this, shareURL, model.getName(), model.getSlogan());
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

    private void pay(View view) {

        // 一个自定义的布局，作为显示的内容
        View mview = LayoutInflater.from(this).inflate(R.layout.popup_parameter, null);

        ImageView imgPhoto = (ImageView) mview.findViewById(R.id.img_photo);
        final ListView listParameter = (ListView) mview.findViewById(R.id.list_parameter);
        LinearLayout layoutCancel = (LinearLayout) mview.findViewById(R.id.layout_cancel);
        final TextView txtBuy = (TextView) mview.findViewById(R.id.txt_buy);
        TextView txtAdd = (TextView) mview.findViewById(R.id.txt_add);
        final TextView txtNumber = (TextView) mview.findViewById(R.id.txt_number);
        TextView txtSubtract = (TextView) mview.findViewById(R.id.txt_subtract);
        final TextView txtName = (TextView) mview.findViewById(R.id.txt_name);
        final TextView txtPrice = (TextView) mview.findViewById(R.id.txt_price);
        final TextView txtCurrency = (TextView) mview.findViewById(R.id.txt_currency);
        final TextView txtQuantity = (TextView) mview.findViewById(R.id.txt_quantity);

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

        txtNumber.setText(number+"");

        ImageUtil.glide(model.getAdvPic(),imgPhoto,GoodDetailsActivity.this);

        // 初始化数据
        number = 1;
        index = 0;
        if(model.getProductSpecsList() != null){
            if(model.getProductSpecsList().size() > 0){
                list.clear();
                list.addAll(model.getProductSpecsList());
                for (GoodsModel.ProductSpecsListBean bean : list){
                    bean.setSelect(false);
                }
                list.get(0).setSelect(true);

                txtName.setText(list.get(0).getName());
                txtQuantity.setText("库存"+list.get(0).getQuantity()+"件");

                switch (model.getStore().getType()) {
                    case "G01":
                        txtCurrency.setText("礼品券");
                        break;

                    default:
                        txtCurrency.setText("¥");
                        break;
                }
                txtPrice.setText(MoneyUtil.moneyFormatDouble(list.get(0).getPrice1()));
            }
        }
        listParameter.setAdapter(adapter);
        listParameter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;
                number = 1;
                txtNumber.setText(number+"");
                txtName.setText(list.get(i).getName());
                txtQuantity.setText("库存"+list.get(i).getQuantity()+"件");

                switch (model.getStore().getType()) {
                    case "G01":
                        txtCurrency.setText("礼品券");
                        break;

                    default:
                        txtCurrency.setText("¥");
                        break;
                }
                txtPrice.setText(MoneyUtil.moneyFormatDouble(list.get(i).getPrice1()));


                for (GoodsModel.ProductSpecsListBean bean : list){
                    bean.setSelect(false);
                }
                list.get(i).setSelect(true);
                adapter.notifyDataSetChanged();
            }
        });

        layoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        txtSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number > 1) {
                    number--;
                    txtNumber.setText(number + "");
                }
            }
        });

        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number++;
                txtNumber.setText(number + "");
            }
        });

        txtBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProductModel productModel = new ProductModel();
                productModel.setProductNumber(number);
                productModel.setProductCode(model.getCode());
                productModel.setProductName(model.getName());
                productModel.setProductImage(model.getAdvPic());
                productModel.setType(model.getStore().getType());
                productModel.setStartPoin(list.get(index).getProvince());
                productModel.setPrice1(model.getProductSpecsList().get(index).getPrice1());
                productModel.setPrice2(model.getProductSpecsList().get(index).getPrice2());
                productModel.setPrice3(model.getProductSpecsList().get(index).getPrice3());
                productModel.setProductSpecsCode(model.getProductSpecsList().get(index).getCode());
                productModel.setProductSpecsName(model.getProductSpecsList().get(index).getName());
                startActivity(new Intent(GoodDetailsActivity.this, CommitOrderActivity.class)
                        .putExtra("orderType", "now")
                        .putExtra("currency", model.getPayCurrency())
                        .putExtra("productModel", productModel));


                popupWindow.dismiss();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_layout));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

}