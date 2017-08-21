package com.zhenghui.zhqb.zhenghuiqianbaomember.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.GoodDetailsActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.ShopDetailsActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.loader.BannerImageLoader;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.GoodsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CommodityFragment extends Fragment {


    @InjectView(R.id.banner)
    Banner banner;
    @InjectView(R.id.txt_name)
    TextView txtName;
    @InjectView(R.id.txt_info)
    TextView txtInfo;
    @InjectView(R.id.txt_currency)
    TextView txtCurrency;
    @InjectView(R.id.txt_price)
    TextView txtPrice;
    @InjectView(R.id.layout_price)
    LinearLayout layoutPrice;
    @InjectView(R.id.txt_yunfei)
    TextView txtYunfei;
    @InjectView(R.id.img_shopPic)
    ImageView imgShopPic;
    @InjectView(R.id.txt_shopName)
    TextView txtShopName;
    @InjectView(R.id.txt_slogan)
    TextView txtSlogan;
    @InjectView(R.id.txt_type)
    TextView txtType;
    @InjectView(R.id.layout_store)
    LinearLayout layoutStore;
    @InjectView(R.id.txt_subtract)
    TextView txtSubtract;
    @InjectView(R.id.txt_number)
    TextView txtNumber;
    @InjectView(R.id.txt_add)
    TextView txtAdd;

    private View view;
    private GoodsModel model;
    private List<String> images;

    private GoodDetailsActivity activity;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;


    public static CommodityFragment newInstance(GoodsModel model) {
        CommodityFragment fragment = new CommodityFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_commodity, null);
        ButterKnife.inject(this, view);

        Bundle args = getArguments();
        if (args != null) {
            model = (GoodsModel) args.getSerializable("model");
        }

        inits();
        setView();
//        getYunfei();
        initBanner();


        return view;
    }

    private void inits() {
        activity = (GoodDetailsActivity) getActivity();
        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getActivity().getSharedPreferences("appConfig", Context.MODE_PRIVATE);

        images = new ArrayList<>();

        String[] pic = model.getAdvPic().split("\\|\\|");

        System.out.println("pic.length=" + pic.length);

        for (String str : pic) {
            System.out.print("url=" + str);
            images.add(str);
        }

    }

    private void setView() {
        txtNumber.setText(activity.number + "");
        txtName.setText(model.getName());
        txtInfo.setText(model.getSlogan());

        switch (model.getStore().getType()) {

            case "G01":
                txtCurrency.setText("礼品券");
                break;

            default:
                txtCurrency.setText("¥");
                break;

        }
        txtPrice.setText(MoneyUtil.moneyFormatDouble(model.getProductSpecsList().get(0).getPrice1()));

        ImageUtil.glide(model.getStore().getAdvPic(), imgShopPic, getActivity());
        txtShopName.setText(model.getStore().getName());
        txtSlogan.setText(model.getStore().getSlogan());
        if (model.getType().equals("G01")) {
            txtType.setText("礼品商");
        } else {
            txtType.setText("普通商家");
        }
    }

    private void initBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new BannerImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(Arrays.asList(titles));
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.txt_subtract, R.id.txt_add, R.id.layout_store})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_subtract:
                if (activity.number > 1) {
                    activity.number--;
                    txtNumber.setText(activity.number + "");
                }

                break;

            case R.id.txt_add:
                activity.number++;
                txtNumber.setText(activity.number + "");

                break;

            case R.id.layout_store:
                startActivity(new Intent(getActivity(), ShopDetailsActivity.class).putExtra("code", model.getStore().getCode()));
                break;

        }
    }
}
