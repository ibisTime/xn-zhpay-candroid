package com.zhenghui.zhqb.zhenghuiqianbaomember.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.RichTextActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.activity.TreeActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.GiveAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.GiveModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.PersonalModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.WxUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_805056;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808456;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_808476;

public class GiveFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.list_give)
    ListView listGivet;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;
    @InjectView(R.id.txt_buy)
    TextView txtBuy;
    @InjectView(R.id.layout_buy)
    RelativeLayout layoutBuy;
    @InjectView(R.id.layout_my)
    LinearLayout layoutMy;
    @InjectView(R.id.txt_introduce)
    TextView txtIntroduce;

    // Fragment主视图
    private View view;

    private View headView;

    private SharedPreferences userInfoSp;
    private SharedPreferences appConfigSp;
    private SharedPreferences wxShareSp;

    private boolean isBuy;
    private String treeDetail;

    private GiveAdapter adapter;
    private List<GiveModel> list;

    private int page = 1;
    private int pageSize = 50;

    private String code = "";
    private String shareURL;

    private PersonalModel model;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_give, null);
        ButterKnife.inject(this, view);

        inits();
        initRefreshLayout();
        initListView();

        getMyTree();
        getDatas();

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            getMyTree();
            getData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyTree();
        getData();
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new GiveAdapter(getActivity(), list);

        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getActivity().getSharedPreferences("appConfig", Context.MODE_PRIVATE);
        wxShareSp = getActivity().getSharedPreferences("wxShare", Context.MODE_PRIVATE);

    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

//        swipeContainer.setOnLoadListener(this);
        swipeContainer.setOnRefreshListener(this);
    }

    private void initListView() {
        listGivet.setAdapter(adapter);
        listGivet.setOnItemClickListener(this);
    }

    private void getData() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式

        JSONObject object = new JSONObject();
        try {
            object.put("owner", userInfoSp.getString("userId", null));
            object.put("receiver", "");
            object.put("status", "");
            object.put("createDatetimeStart", df.format(new Date()));
            object.put("createDatetimeEnd", df.format(new Date()));
            object.put("receiveDatetimeStart", "");
            object.put("receiveDatetimeEnd", "");
            object.put("orderDir", "");
            object.put("orderColumn", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808476, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonObject = new JSONArray(result);

                    Gson gson = new Gson();
                    ArrayList<GiveModel> lists = gson.fromJson(jsonObject.toString(), new TypeToken<ArrayList<GiveModel>>() {
                    }.getType());

                    if(page == 1){
                        list.clear();
                    }
                    list.addAll(lists);
                    adapter.notifyDataSetChanged();

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

    public void getMyTree() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_808456, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("{}")) {
                    isBuy = false;
                    layoutMy.setVisibility(View.GONE);
                    layoutBuy.setVisibility(View.VISIBLE);
                } else {
                    isBuy = true;
                    layoutBuy.setVisibility(View.GONE);
                    layoutMy.setVisibility(View.VISIBLE);
                    getData();
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

    @OnClick({R.id.txt_introduce, R.id.txt_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_introduce:
                startActivity(new Intent(getActivity(), RichTextActivity.class).putExtra("cKey","fyf_rule"));
                break;

            case R.id.txt_buy:
                startActivity(new Intent(getActivity(), TreeActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (list.get(i).getStatus().equals("0")) { // 待发送
            code = list.get(i).getCode();
            shareURL = Xutil.SHARE_URL + Xutil.SHARE_PORT + "/share/share-receive.html?code=" + code + "&userReferee=" + userInfoSp.getString("mobile", null);

            tip1(view);
        } else if (list.get(i).getStatus().equals("1")) { // 已发送,待领取
            tip2();
        } else if (list.get(i).getStatus().equals("2")) { // 已领取
            tip3(i);
        }
    }

    private void tip1(View view) {

        // 一个自定义的布局，作为显示的内容
        View mview = LayoutInflater.from(getActivity()).inflate(R.layout.popup_give, null);

        final LinearLayout wx = (LinearLayout) mview.findViewById(R.id.layout_wx);
        final LinearLayout pyq = (LinearLayout) mview.findViewById(R.id.layout_pyq);
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

        wx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (WxUtil.check(getActivity())) {
                    WxUtil.shareToWX(getActivity(), shareURL,
                            "花米宝邀您领红包",
                            "千万红包免费领，快来快来快快来");

                    SharedPreferences.Editor editor = wxShareSp.edit();
                    editor.putString("shareWay", "give");
                    editor.putString("giveCode", code);
                    editor.commit();
                }

                System.out.println("shareURL=" + shareURL);

                popupWindow.dismiss();
            }
        });

        pyq.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (WxUtil.check(getActivity())) {
                    WxUtil.shareToPYQ(getActivity(), shareURL,
                            "花米宝邀您领红包",
                            "千万红包免费领，快来快来快快来");

                    SharedPreferences.Editor editor = wxShareSp.edit();
                    editor.putString("shareWay", "give");
                    editor.putString("giveCode", code);
                    editor.commit();
                }

                System.out.println("shareURL=" + shareURL);

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

    private void tip2() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_give);
        TextView txtTitle = (TextView) window.findViewById(R.id.txt_title);
        TextView txtContent3 = (TextView) window.findViewById(R.id.txt_content3);
        TextView txtOk = (TextView) window.findViewById(R.id.txt_ok);

        txtTitle.setText("该红包还未被领取");
        txtContent3.setText("快通知小伙伴领取红包吧，不然要过期了...");

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void tip3(int position) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_give);
        TextView txtTitle = (TextView) window.findViewById(R.id.txt_title);
        TextView txtContent1 = (TextView) window.findViewById(R.id.txt_content1);
        TextView txtContent2 = (TextView) window.findViewById(R.id.txt_content2);
        TextView txtContent3 = (TextView) window.findViewById(R.id.txt_content3);
        TextView txtOk = (TextView) window.findViewById(R.id.txt_ok);

        txtContent1.setVisibility(View.VISIBLE);
        txtContent2.setVisibility(View.VISIBLE);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(list.get(position).getReceiveDatetime());

        txtTitle.setText("该红包已经被领取");
        txtContent1.setText("领取人：" + list.get(position).getReceiverUser().getMobile());
        txtContent2.setText("领取时间：" + format.format(date));
        txtContent3.setText("您的红包业绩已经到账，请注意查收");

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    /**
     * 获取用户详情
     */
    private void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("userId", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Xutil().post(CODE_805056, object.toString(), new Xutil.XUtils3CallBackPost() {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                page = 1;
                getData();

            }
        }, 1500);
    }


//    @Override
//    public void onLoad() {
//        swipeContainer.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                swipeContainer.setLoading(false);
//                page = page + 1;
//                getData();
//
//            }
//        }, 1500);
//    }
}
