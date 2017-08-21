package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.GiveAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.GiveModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.WxUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.R.id.txt_history;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_615118;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_615135;

public class GiveActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.list_give)
    ListView listGive;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;
    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(txt_history)
    TextView txtHistory;
    @InjectView(R.id.txt_buy)
    TextView txtBuy;
    @InjectView(R.id.layout_buy)
    RelativeLayout layoutBuy;
    @InjectView(R.id.layout_my)
    LinearLayout layoutMy;

    private GiveAdapter adapter;
    private List<GiveModel> list;

    private int page = 1;
    private int pageSize = 5;

    private String code = "";
    private String shareURL;

    private boolean isBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give);
        ButterKnife.inject(this);

        inits();
        initRefreshLayout();
        initListView();


    }

    @Override
    protected void onResume() {
        super.onResume();

        getMyTree();
        getData();
    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new GiveAdapter(this, list);

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
        wxShareSp = getSharedPreferences("wxShare", Context.MODE_PRIVATE);
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
    }

    private void initListView() {
        listGive.setAdapter(adapter);
        listGive.setOnItemClickListener(this);
    }

    private void getData() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式

        JSONObject object = new JSONObject();
        try {
            object.put("hzbCode", "");
            object.put("owner", userInfoSp.getString("userId", null));
            object.put("token", userInfoSp.getString("token", null));
            object.put("receiver", "");
            object.put("status", "");
            object.put("createDatetimeStart", df.format(new Date()));
            object.put("createDatetimeEnd", df.format(new Date()));
            object.put("receiveDatetimeStart", "");
            object.put("receiveDatetimeEnd", "");
            object.put("start","1");
            object.put("limit","5");
            object.put("orderDir", "");
            object.put("orderColumn", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

            new Xutil().post(CODE_615135, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<GiveModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<GiveModel>>() {
                    }.getType());

                    list.clear();
                    list.addAll(lists);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(GiveActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(GiveActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
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

        new Xutil().post(CODE_615118, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("[]")) {
                    isBuy = false;
                    layoutMy.setVisibility(View.GONE);
                    txtHistory.setVisibility(View.GONE);
                    layoutBuy.setVisibility(View.VISIBLE);
                } else {
                    isBuy = true;
                    layoutBuy.setVisibility(View.GONE);
                    layoutMy.setVisibility(View.VISIBLE);
                    txtHistory.setVisibility(View.VISIBLE);
                    getData();
                }
            }

            @Override
            public void onTip(String tip) {

                Toast.makeText(GiveActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(GiveActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (list.get(i).getStatus().equals("0")) { // 待发送
            code = list.get(i).getCode();
            shareURL = Xutil.SHARE_URL + Xutil.SHARE_PORT + "/share/share-receive.html?code=" + code + "&userReferee=" + userInfoSp.getString("mobile", null);

            tip1(view, list.get(i).getCode());
        } else if (list.get(i).getStatus().equals("1")) { // 已发送,待领取
            tip2();
        } else if (list.get(i).getStatus().equals("2")) { // 已领取
            tip3(i);
        }
    }

    private void tip1(View view, final String code) {

        // 一个自定义的布局，作为显示的内容
        View mview = LayoutInflater.from(GiveActivity.this).inflate(R.layout.popup_give, null);

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

                if (WxUtil.check(GiveActivity.this)) {
                    WxUtil.shareToWX(GiveActivity.this, shareURL,
                            "正汇钱包邀您领红包",
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

                if (WxUtil.check(GiveActivity.this)) {
                    WxUtil.shareToPYQ(GiveActivity.this, shareURL,
                            "正汇钱包邀您领红包",
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
        final AlertDialog alertDialog = new AlertDialog.Builder(GiveActivity.this).create();
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
        final AlertDialog alertDialog = new AlertDialog.Builder(GiveActivity.this).create();
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

    @OnClick({R.id.layout_back, txt_history})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case txt_history:
                startActivity(new Intent(GiveActivity.this, GiveHistoryActivity.class));
                break;
        }
    }

    @OnClick(R.id.txt_buy)
    public void onClick() {
        startActivity(new Intent(GiveActivity.this, TreeActivity.class));
    }
}
