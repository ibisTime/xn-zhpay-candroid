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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.AuthenticateActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.TreeActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.TreePayActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.WebActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.TreeModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tree_buy, null);
        ButterKnife.inject(this, view);

        inits();
        getTree();

        return view;
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
                startActivity(new Intent(getActivity(), WebActivity.class).putExtra("webURL","www.baidu.com"));
                break;

            case R.id.txt_buy:
//                buy();
                if(userInfoSp.getString("realName",null) != null){
                    startActivity(new Intent(getActivity(), TreePayActivity.class).putExtra("price",list.get(0).getPrice()).putExtra("code",list.get(0).getCode()));
                }else{
                    Toast.makeText(getActivity(), "请先实名认证", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), AuthenticateActivity.class));
                }


                break;
        }
    }

    public void buy(){
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("hzbCode", "HZB001");
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808452", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                activity.getMyTree();
                Toast.makeText(getActivity(), "购买成功", Toast.LENGTH_SHORT).show();
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

}
