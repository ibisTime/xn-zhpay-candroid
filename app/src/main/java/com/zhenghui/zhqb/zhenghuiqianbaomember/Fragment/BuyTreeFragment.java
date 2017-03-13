package com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.AuthenticateActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.RichTextActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.TreeActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.TreePayActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.PersonalModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.TreeModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.LoginUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;
import com.zzhoujay.richtext.RichText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by dell1 on 2016/12/19.
 */

public class BuyTreeFragment extends Fragment {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_introduce)
    TextView txtIntroduce;
    @InjectView(R.id.imageView3)
    ImageView imageView3;
    @InjectView(R.id.txt_buy)
    TextView txtBuy;

    private View view;

    private TreeActivity activity;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;

    private List<TreeModel> list;
    private PersonalModel model;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tree_buy, null);
        ButterKnife.inject(this, view);

        inits();
        getTree();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void inits() {
        list = new ArrayList<>();

        activity = (TreeActivity) getActivity();
        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getActivity().getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.layout_back, R.id.txt_introduce, R.id.txt_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                getActivity().finish();
                break;

            case R.id.txt_introduce:
                startActivity(new Intent(getActivity(), RichTextActivity.class).putExtra("cKey", "fyf_rule"));
                break;

            case R.id.txt_buy:
                if (userInfoSp.getString("userId", null) != null) {

                    if(userInfoSp.getString("realName",null) != null){

                        statement(view);
                    }else{
                        Toast.makeText(getActivity(), "请先实名认证", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), AuthenticateActivity.class)
                                .putExtra("price",list.get(0).getPrice())
                                .putExtra("code",list.get(0).getCode()));
                    }
                } else {
                    LoginUtil.toLogin(getActivity());
                }



                break;
        }
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

                    SharedPreferences.Editor editor = userInfoSp.edit();
                    editor.putString("mobile", model.getMobile());
                    editor.putString("realName", model.getRealName());
                    editor.putString("nickName", model.getNickname());
                    editor.putString("identityFlag", model.getIdentityFlag());
                    editor.putString("tradepwdFlag", model.getTradepwdFlag());
                    editor.putString("userRefereeName", model.getUserRefereeName());
                    if (null != model.getUserExt().getPhoto()) {
                        editor.putString("photo", model.getUserExt().getPhoto());
                    }
                    String address = model.getUserExt().getProvince() + model.getUserExt().getCity() + model.getUserExt().getArea();
                    editor.putString("address", address);
                    editor.commit();

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

    public void getTree(){
        JSONObject object = new JSONObject();
        try {
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808451", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONArray jsonObject = new JSONArray(result);

                    Gson gson = new Gson();
                    ArrayList<TreeModel> lists = gson.fromJson(jsonObject.toString(), new TypeToken<ArrayList<TreeModel>>() {
                    }.getType());

                    list.addAll(lists);

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

    private TextView content;

    private void statement(View view) {

        // 一个自定义的布局，作为显示的内容
        View mview = LayoutInflater.from(getActivity()).inflate(R.layout.popup_statement, null);

        content = (TextView) mview.findViewById(R.id.txt_content);
        TextView ok = (TextView) mview.findViewById(R.id.txt_ok);
        LinearLayout layoutStatement = (LinearLayout) mview.findViewById(R.id.layout_statement);

        getStatement();

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

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
                startActivity(new Intent(getActivity(), TreePayActivity.class)
                        .putExtra("price",list.get(0).getPrice())
                        .putExtra("code",list.get(0).getCode()));
            }
        });

        layoutStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_layout));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    public void getStatement() {
        JSONObject object = new JSONObject();
        try {
            object.put("ckey", "fyf_statement");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("807717", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    RichText.from(jsonObject.getString("note")).into(content);


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
