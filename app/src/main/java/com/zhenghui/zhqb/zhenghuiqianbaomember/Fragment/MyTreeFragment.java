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

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.GiveActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.RichTextActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.TreeActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.TreeBillActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dell1 on 2016/12/19.
 */

public class MyTreeFragment extends Fragment {


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
    @InjectView(R.id.txt_state)
    TextView txtState;
    @InjectView(R.id.linearLayout)
    LinearLayout linearLayout;
    @InjectView(R.id.layout_activate)
    LinearLayout layoutActivate;
    @InjectView(R.id.txt_popedom)
    TextView txtPopedom;
    @InjectView(R.id.txt_shakeNum_history)
    TextView txtShakeNumHistory;
    @InjectView(R.id.layout_shake)
    LinearLayout layoutShake;
    @InjectView(R.id.txt_shakeNum_today)
    TextView txtShakeNumToday;
    @InjectView(R.id.txt_hbyj)
    TextView txtHbyj;
    @InjectView(R.id.txt_giveNum_history)
    TextView txtGiveNumHistory;
    @InjectView(R.id.layout_give)
    LinearLayout layoutGive;
    @InjectView(R.id.txt_giveNum_today)
    TextView txtGiveNumToday;
    @InjectView(R.id.txt_gxz)
    TextView txtGxz;
    @InjectView(R.id.txt_give)
    TextView txtGive;
    private View view;
    private String tree;

    private TreeActivity activity;
    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    public static MyTreeFragment newInstance(String tree) {
        MyTreeFragment fragment = new MyTreeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tree", tree);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tree_my, null);
        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        Bundle args = getArguments();
        if (args != null) {
            tree = args.getString("tree");
            inits();
            getData();
        }
    }

    private void inits() {

        activity = (TreeActivity) getActivity();
        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getActivity().getSharedPreferences("appConfig", Context.MODE_PRIVATE);


        txtName.setText(userInfoSp.getString("realName", null));
        txtPopedom.setText(userInfoSp.getString("address", null));

        try {
            JSONObject jsonObject = new JSONObject(tree);

            txtName.setText(userInfoSp.getString("realName", null));
            txtPopedom.setText(userInfoSp.getString("address", null));

            if (jsonObject.getString("status").equals("0")) {
                txtState.setText("待支付");
            } else if (jsonObject.getString("status").equals("1")) {
                txtState.setText("已激活");
            } else if (jsonObject.getString("status").equals("2")) {
                txtState.setText("已冻结");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.layout_back, R.id.txt_introduce, R.id.layout_activate, R.id.layout_shake, R.id.layout_give, R.id.txt_give})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                getActivity().finish();
                break;

            case R.id.txt_introduce:
                startActivity(new Intent(getActivity(), RichTextActivity.class).putExtra("cKey", "yyy_rule"));
                break;

            case R.id.layout_activate:
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, 100);
                break;

            case R.id.layout_shake:
                startActivity(new Intent(getActivity(), TreeBillActivity.class)
                        .putExtra("bizType", "39")
                        .putExtra("currency", "HBYJ"));
                break;

            case R.id.layout_give:
                startActivity(new Intent(getActivity(), TreeBillActivity.class)
                        .putExtra("bizType", "60")
                        .putExtra("currency", "HBYJ"));
                break;

            case R.id.txt_give:
                startActivity(new Intent(getActivity(), GiveActivity.class));

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 100) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
//                    Toast.makeText(getActivity(), "解析结果:" + result, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        activateTree(jsonObject.getString("userId"), jsonObject.getString("code"));

                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "请扫描正确的二维码", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void activateTree(String userId, String code) {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userId);
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(code, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                activity.getMyTree();
                txtState.setText("已激活");
                layoutActivate.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "激活成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(getActivity(), "无法连接服务器，请检查网络", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getData() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808802", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    txtHbyj.setText(MoneyUtil.moneyFormatDouble(Double.parseDouble(jsonObject.getString("yyTotalAmount"))));
                    txtShakeNumToday.setText(jsonObject.getString("todayYyTimes") + "次");
                    txtShakeNumHistory.setText(jsonObject.getString("historyYyTimes") + "次");

                    txtGxz.setText(MoneyUtil.moneyFormatDouble(Double.parseDouble(jsonObject.getString("ffTotalHbAmount"))));
                    txtGiveNumToday.setText(jsonObject.getString("todayHbTimes") + "次");
                    txtGiveNumHistory.setText(jsonObject.getString("historyHbTimes") + "次");

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
