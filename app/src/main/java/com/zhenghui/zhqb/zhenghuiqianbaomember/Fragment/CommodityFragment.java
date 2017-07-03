package com.zhenghui.zhqb.zhenghuiqianbaomember.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.GoodDetailsActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.loader.BannerImageLoader;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.GoodsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CommodityFragment extends Fragment {

    @InjectView(R.id.txt_name)
    TextView txtName;
    @InjectView(R.id.txt_info)
    TextView txtInfo;
    @InjectView(R.id.banner)
    Banner banner;
    @InjectView(R.id.txt_subtract)
    TextView txtSubtract;
    @InjectView(R.id.txt_number)
    TextView txtNumber;
    @InjectView(R.id.txt_add)
    TextView txtAdd;
    @InjectView(R.id.txt_gwb1)
    TextView txtGwb1;
    @InjectView(R.id.txt_gwb2)
    TextView txtGwb2;
    @InjectView(R.id.txt_rmb1)
    TextView txtRmb1;
    @InjectView(R.id.txt_rmb2)
    TextView txtRmb2;
    @InjectView(R.id.txt_qbb1)
    TextView txtQbb1;
    @InjectView(R.id.txt_qbb2)
    TextView txtQbb2;
    @InjectView(R.id.txt_yunfei)
    TextView txtYunfei;
    @InjectView(R.id.img_shopPic)
    ImageView imgShopPic;
    @InjectView(R.id.txt_shopName)
    TextView txtShopName;
    @InjectView(R.id.txt_mobile)
    TextView txtMobile;
    @InjectView(R.id.txt_slogan)
    TextView txtSlogan;

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
        getYunfei();
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


        if (model.getProductSpecsList().get(0).getPrice1() == 0) {
            txtRmb1.setVisibility(View.GONE);
            txtRmb2.setVisibility(View.GONE);
        } else {
            txtRmb1.setVisibility(View.VISIBLE);
            txtRmb2.setVisibility(View.VISIBLE);
            txtRmb1.setText("¥" + MoneyUtil.moneyFormatDouble(model.getProductSpecsList().get(0).getPrice1()));
            if (model.getProductSpecsList().get(0).getPrice2() == 0 && model.getProductSpecsList().get(0).getPrice3() == 0) {
                txtRmb2.setText("");
            }
        }

        if (model.getProductSpecsList().get(0).getPrice2() == 0) {
            txtGwb1.setVisibility(View.GONE);
            txtGwb2.setVisibility(View.GONE);
        } else {
            txtGwb1.setVisibility(View.VISIBLE);
            txtGwb2.setVisibility(View.VISIBLE);
            txtGwb1.setText(MoneyUtil.moneyFormatDouble(model.getProductSpecsList().get(0).getPrice2()));
            if (model.getProductSpecsList().get(0).getPrice3() == 0) {
                txtGwb2.setText("购物币");
            }
        }

        if (model.getProductSpecsList().get(0).getPrice3() == 0) {
            txtQbb1.setVisibility(View.GONE);
            txtQbb2.setVisibility(View.GONE);
        } else {
            txtQbb1.setVisibility(View.VISIBLE);
            txtQbb2.setVisibility(View.VISIBLE);
            txtQbb1.setText(MoneyUtil.moneyFormatDouble(model.getProductSpecsList().get(0).getPrice3()));
        }

        if (model.getProductSpecsList().get(0).getPrice1() == 0
                && model.getProductSpecsList().get(0).getPrice2() == 0
                && model.getProductSpecsList().get(0).getPrice3() == 0) {
            txtRmb1.setText("0");
            txtRmb1.setVisibility(View.VISIBLE);
        }

        ImageUtil.glide(model.getStore().getAdvPic(), imgShopPic, getActivity());
        txtShopName.setText(model.getStore().getName());
        txtMobile.setText("联系电话:" + model.getStore().getBookMobile());
        txtSlogan.setText(model.getStore().getSlogan());
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

    @OnClick({R.id.txt_subtract, R.id.txt_add})
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

        }
    }

    public void getYunfei() {
        JSONObject object = new JSONObject();
        try {
            object.put("key", "SP_YUNFEI");
            object.put("token", userInfoSp.getString("token", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808917", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    txtYunfei.setText("运费: " + jsonObject.getString("cvalue") + "元");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(getActivity(), "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
