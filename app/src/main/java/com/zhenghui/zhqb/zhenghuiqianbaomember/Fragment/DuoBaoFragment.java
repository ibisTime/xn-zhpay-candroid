package com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.DuoBaoActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.ScheduleActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Loader.BannerImageLoader;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.DuoBaoModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by dell1 on 2016/12/23.
 */


public class DuoBaoFragment extends Fragment {


    @InjectView(R.id.banner)
    Banner banner;
    @InjectView(R.id.txt_name)
    TextView txtName;
    @InjectView(R.id.txt_info)
    TextView txtInfo;
    @InjectView(R.id.bar_schedule)
    ProgressBar barSchedule;
    @InjectView(R.id.txt_schedule)
    TextView txtSchedule;
    @InjectView(R.id.txt_menber)
    TextView txtMenber;
    @InjectView(R.id.txt_rmb)
    TextView txtRmb;
    @InjectView(R.id.txt_gwb)
    TextView txtGwb;
    @InjectView(R.id.txt_qbb)
    TextView txtQbb;
    @InjectView(R.id.txt_day)
    TextView txtDay;
    @InjectView(R.id.layout_schedule)
    LinearLayout layoutSchedule;
    @InjectView(R.id.txt_subtract)
    TextView txtSubtract;
    @InjectView(R.id.txt_number)
    TextView txtNumber;
    @InjectView(R.id.txt_add)
    TextView txtAdd;
    @InjectView(R.id.txt_all)
    TextView txtAll;
    @InjectView(R.id.layout_rmb)
    LinearLayout layoutRmb;
    @InjectView(R.id.layout_gwb)
    LinearLayout layoutGwb;
    @InjectView(R.id.layout_qbb)
    LinearLayout layoutQbb;
    private View view;

    private DuoBaoModel model;

    private List<String> images;

    private DuoBaoActivity activity;


    public static DuoBaoFragment newInstance(DuoBaoModel model) {
        DuoBaoFragment fragment = new DuoBaoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_duobao, null);
        ButterKnife.inject(this, view);

        Bundle args = getArguments();
        if (args != null) {
            model = (DuoBaoModel) args.getSerializable("model");
        }

        inits();
        initBanner();
        activity.setTotalPrice();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDatas();
    }

    private void inits() {
        activity = (DuoBaoActivity) getActivity();
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

        if(model.getPrice1() == 0){
            layoutRmb.setVisibility(View.INVISIBLE);
        }
        if(model.getPrice2() == 0){
            layoutGwb.setVisibility(View.INVISIBLE);
        }
        if(model.getPrice3() == 0){
            layoutQbb.setVisibility(View.INVISIBLE);
        }


        txtRmb.setText(MoneyUtil.moneyFormatDouble(model.getPrice1()));
        txtGwb.setText(MoneyUtil.moneyFormatDouble(model.getPrice2()));
        txtQbb.setText(MoneyUtil.moneyFormatDouble(model.getPrice3()));

        txtDay.setText(model.getRaiseDays() + "天");
        txtMenber.setText(model.getInvestNum() + "人");
        int percent = (model.getInvestNum() * 100 / model.getTotalNum());
        txtSchedule.setText(percent + "%");

        barSchedule.setMax((int) (model.getTotalNum()));
        barSchedule.setProgress(model.getInvestNum());

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


    @OnClick({R.id.txt_subtract, R.id.txt_add, R.id.layout_schedule, R.id.txt_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_subtract:
                if (activity.number > 1) {
                    activity.number--;
                    txtNumber.setText(activity.number + "");
                }
                activity.setTotalPrice();
                break;

            case R.id.txt_add:
                activity.number++;
                txtNumber.setText(activity.number + "");
                activity.setTotalPrice();
                break;

            case R.id.layout_schedule:
                startActivity(new Intent(getActivity(), ScheduleActivity.class)
                        .putExtra("model", model));
                break;

            case R.id.txt_all:
                activity.number = model.getTotalNum() - model.getInvestNum();
                txtNumber.setText(activity.number + "");
                activity.setTotalPrice();
                break;

        }
    }

    /**
     * 获取宝贝详情
     */
    public void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", model.getCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808312", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<DuoBaoModel>() {
                    }.getType());

                    setView();

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
