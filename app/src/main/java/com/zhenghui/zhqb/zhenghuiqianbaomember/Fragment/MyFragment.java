package com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.AboutActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.DiscountActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.MyDuoBaoActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.MyShopActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.OrderListActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.PersonalActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.StockActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.TreeActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.WalletActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.PayResult;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.PersonalModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dell1 on 2016/12/12.
 */

public class MyFragment extends Fragment {

    @InjectView(R.id.img_photo)
    CircleImageView imgPhoto;
    @InjectView(R.id.txt_person)
    TextView txtPerson;
    @InjectView(R.id.layout_stock)
    LinearLayout layoutStock;
    @InjectView(R.id.layout_moneyTree)
    LinearLayout layoutMoneyTree;
    @InjectView(R.id.layout_wallet)
    LinearLayout layoutWallet;
    @InjectView(R.id.layout_discount)
    LinearLayout layoutDiscount;
    @InjectView(R.id.layout_myshop)
    LinearLayout layoutMyshop;
    @InjectView(R.id.layout_shoppinglist)
    LinearLayout layoutShoppinglist;
    @InjectView(R.id.layout_duobaojilu)
    LinearLayout layoutDuobaojilu;
    @InjectView(R.id.layout_about)
    LinearLayout layoutAbout;
    @InjectView(R.id.txt_name)
    TextView txtName;
    @InjectView(R.id.layout_person)
    LinearLayout layoutPerson;

    // Fragment主视图
    private View view;

    private SharedPreferences userInfoSp;

    private String userInfo;
    private PersonalModel model;


    private myFragmentInteraction listterner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, null);
        ButterKnife.inject(this, view);

        inis();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void inis() {
        model = new PersonalModel();
        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.layout_person, R.id.layout_stock, R.id.layout_moneyTree, R.id.layout_wallet, R.id.layout_discount, R.id.layout_myshop, R.id.layout_shoppinglist, R.id.layout_duobaojilu, R.id.layout_about})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_person:
                Intent intent = new Intent(getActivity(), PersonalActivity.class);
                intent.putExtra("model", model);
                getActivity().startActivity(intent);
                break;

            case R.id.layout_stock:
                startActivity(new Intent(getActivity(), StockActivity.class));
                break;

            case R.id.layout_moneyTree:
                startActivity(new Intent(getActivity(), TreeActivity.class));
                break;

            case R.id.layout_wallet:
                startActivity(new Intent(getActivity(), WalletActivity.class));
                break;

            case R.id.layout_discount:
                startActivity(new Intent(getActivity(), DiscountActivity.class));
                break;

            case R.id.layout_myshop:
                startActivity(new Intent(getActivity(), MyShopActivity.class));
                break;

            case R.id.layout_shoppinglist:
                startActivity(new Intent(getActivity(), OrderListActivity.class));
                break;

            case R.id.layout_duobaojilu:
                startActivity(new Intent(getActivity(), MyDuoBaoActivity.class));
                break;

            case R.id.layout_about:

                startActivity(new Intent(getActivity(), AboutActivity.class));

//                final String payInfo ="app_id=2015052600090779&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22seller_id%22%3A%22%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%220.02%22%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%22314VYGIAGG7ZOYY%22%7D&charset=utf-8&method=alipay.trade.app.pay&sign_type=RSA2&timestamp=2016-08-15%2012%3A12%3A15&version=1.0&sign=MsbylYkCzlfYLy9PeRwUUIg9nZPeN9SfXPNavUCroGKR5Kqvx0nEnd3eRmKxJuthNUx4ERCXe552EV9PfwexqW%2B1wbKOdYtDIb4%2B7PL3Pc94RZL0zKaWcaY3tSL89%2FuAVUsQuFqEJdhIukuKygrXucvejOUgTCfoUdwTi7z%2BZzQ%3D";
//
////                EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
//
//                Runnable payRunnable = new Runnable() {
//
//                    @Override
//
//                    public void run() {
//
//                        // 构造PayTask 对象
//                        PayTask alipay = new PayTask(getActivity());
//                        // 调用支付接口，获取支付结果
//
//                        String result = alipay.pay(payInfo, true);
//
//                        Message msg = new Message();
//
//                        msg.what = 1;
//
//                        msg.obj = result;
//
//                        mHandler.sendMessage(msg);
//
//                    }
//
//                };
//
//                Thread payThread = new Thread(payRunnable);
//
//                payThread.start();
                break;
        }
    }

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 1:


                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签

                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    System.out.println("resultInfo="+resultInfo);
                    System.out.println("resultStatus="+resultStatus);


                    Toast.makeText(getActivity(), payResult.getResult(),
                            Toast.LENGTH_LONG).show();
                    break;

            }
        };

    };


    /**
     * 获取用户详情
     */
    private void getData() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post("805056", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                userInfo = result;
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<PersonalModel>() {
                    }.getType());

                    SharedPreferences.Editor editor = userInfoSp.edit();
                    editor.putString("realName",model.getRealName());
                    editor.putString("nickName",model.getNickname());
                    editor.putString("identityFlag",model.getIdentityFlag());
                    editor.putString("tradepwdFlag",model.getTradepwdFlag());
                    if(null != model.getUserExt().getPhoto()){
                        editor.putString("photo",model.getUserExt().getPhoto());
                    }
                    String address = model.getUserExt().getProvince()+model.getUserExt().getCity()+model.getUserExt().getArea();
                    editor.putString("address",address);

                    editor.commit();

                    listterner.getUserInfo(model);
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

    private void setView() {
        txtName.setText(model.getNickname());
        if(null == model.getUserExt().getPhoto()){
            Picasso.with(getActivity()).load(R.mipmap.photo_default).into(imgPhoto);
        }else {
            ImageUtil.photo(model.getUserExt().getPhoto(),imgPhoto,getActivity());
        }
    }

    /**
     * 当Fragmen被加载到activity的时候会被回调
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof myFragmentInteraction) {
            listterner = (myFragmentInteraction) activity;
        } else {
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }

    }


    public interface myFragmentInteraction {
        void getUserInfo(PersonalModel model);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listterner = null;
    }

}
