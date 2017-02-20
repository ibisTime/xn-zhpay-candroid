package com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.WebActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.PersonalModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dell1 on 2016/12/19.
 */

public class MyStockFragment extends Fragment {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_introduce)
    TextView txtIntroduce;
    @InjectView(R.id.img_photo)
    CircleImageView imgPhoto;
    @InjectView(R.id.layout_text)
    LinearLayout layoutText;
    @InjectView(R.id.txt_name)
    TextView txtName;
    @InjectView(R.id.txt_status)
    TextView txtStatus;
    @InjectView(R.id.txt_popedom)
    TextView txtPopedom;
    @InjectView(R.id.txt_principal)
    TextView txtPrincipal;
    @InjectView(R.id.txt_award)
    TextView txtAward;
    @InjectView(R.id.txt_awardDate)
    TextView txtAwardDate;
    @InjectView(R.id.txt_confirm)
    TextView txtConfirm;
    @InjectView(R.id.txt_awardTitle)
    TextView txtAwardTitle;

    private View view;
    private String code;

    private SharedPreferences userInfoSp;
    private PersonalModel model;

    public static MyStockFragment newInstance(String code) {
        MyStockFragment fragment = new MyStockFragment();
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stock_my, null);
        ButterKnife.inject(this, view);

        inits();
        getDatas();
        getData();

        return view;

    }

    private void inits() {
        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        Bundle args = getArguments();
        if (args != null) {
            code = args.getString("code");
        }
    }

    @OnClick({R.id.layout_back, R.id.txt_introduce, R.id.txt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                getActivity().finish();
                break;

            case R.id.txt_introduce:
                startActivity(new Intent(getActivity(), WebActivity.class).putExtra("webURL", "www.baidu.com"));
                break;

            case R.id.txt_confirm:
                signIn();
                break;
        }
    }

    /**
     * 获取我的股份详情
     */
    public void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808406", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    if (jsonObject.getString("status").equals("2")) { // 已清算
                        txtStatus.setText("已清算");
                        txtAwardDate.setVisibility(View.GONE);
                        txtAwardTitle.setVisibility(View.GONE);
                        txtConfirm.setVisibility(View.GONE);
                    } else if (jsonObject.getString("status").equals("1")) { // 未清算
                        txtStatus.setText("未清算");
                        txtAwardDate.setVisibility(View.VISIBLE);
                        txtAwardTitle.setVisibility(View.VISIBLE);
                    }

                    txtAward.setText(MoneyUtil.moneyFormatDouble(jsonObject.getDouble("backWelfare1")) + "贡献奖励+" + MoneyUtil.moneyFormatDouble(jsonObject.getDouble("backWelfare2")) + "购物币");

                    txtPrincipal.setText(jsonObject.getJSONObject("stock").getDouble("capital") + "");

                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                    Date d5 = new Date(jsonObject.getString("nextBack"));
                    txtAwardDate.setText(s.format(d5));


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


    /**
     * 签到领奖
     */
    private void signIn() {
        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("userId", userInfoSp.getString("userId", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808404", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(getActivity(), "签到成功", Toast.LENGTH_SHORT).show();
                getDatas();
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


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
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<PersonalModel>() {
                    }.getType());

                    txtName.setText(model.getNickname());
                    txtPopedom.setText(model.getUserExt().getProvince() + model.getUserExt().getCity() + model.getUserExt().getArea());
                    ImageUtil.photo(model.getUserExt().getPhoto(), imgPhoto, getActivity());


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
