package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.JewelAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.loader.BannerImageLoader;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.JewelRecordModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.TargetModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.ImageUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.LoginUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.WxUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;
import com.zzhoujay.richtext.RichText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.zhenghui.zhqb.zhenghuiqianbaomember.R.id.txt_number;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_615016;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_615025;
import static com.zhenghui.zhqb.zhenghuiqianbaomember.util.Constants.CODE_807717;

public class JewelActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.layout_share)
    LinearLayout layoutShare;
    @InjectView(R.id.list_jewel)
    ListView listJewel;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;
    @InjectView(R.id.txt_rmb)
    TextView txtRmb;
    @InjectView(R.id.layout_rmb)
    LinearLayout layoutRmb;
    @InjectView(R.id.txt_gwb)
    TextView txtGwb;
    @InjectView(R.id.layout_gwb)
    LinearLayout layoutGwb;
    @InjectView(R.id.txt_qbb)
    TextView txtQbb;
    @InjectView(R.id.layout_qbb)
    LinearLayout layoutQbb;
    @InjectView(R.id.txt_buyNow)
    TextView txtBuyNow;
    @InjectView(R.id.txt_price)
    TextView txtPrice;

    private String code;

    private View headView;

    // 轮播图
    Banner banner;
    // 轮播图数据
    private List<String> images;

    private ImageView imgAdvPic;

    private TextView txtSum;
    private TextView txtAdd;
    private TextView txtMax;
    private TextView txtInfo;
    private TextView txtName;
    private TextView txtIssue;
    private TextView txtNumber;
    private TextView txtSurplus;
    private TextView txtSubtract;
    private TextView txtStartTime;
    private ProgressBar barSchedule;

    private LinearLayout layoutHistory;
    private LinearLayout layoutIntroduce;

    private TargetModel model;




    private int page = 1;
    private int pageSize = 10;

    private JewelAdapter adapter;
    private List<JewelRecordModel> list;

    private int number = 1;

    private String shareURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jewel);
        ButterKnife.inject(this);

        inits();
        initHeaderView();
        initEvent();
        initRefreshLayout();
        initListView();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // 初始化number
        number = 1;

        getData();
        getJewelRecord();
    }

    private void inits() {
        code = getIntent().getStringExtra("code");

        list = new ArrayList<>();
        images = new ArrayList<>();
        adapter= new JewelAdapter(this,list);

        shareURL = Xutil.SHARE_URL + Xutil.SHARE_PORT + "/share/share-db.html?code="+code;
    }

    private void initHeaderView() {
        headView = LayoutInflater.from(this).inflate(R.layout.head_jewel, null);

        banner = (Banner) headView.findViewById(R.id.banner);

        imgAdvPic = (ImageView) headView.findViewById(R.id.img_advPic);

        txtSum = (TextView) headView.findViewById(R.id.txt_sum);
        txtAdd = (TextView) headView.findViewById(R.id.txt_add);
        txtMax = (TextView) headView.findViewById(R.id.txt_max);
        txtInfo = (TextView) headView.findViewById(R.id.txt_info);
        txtName = (TextView) headView.findViewById(R.id.txt_name);
        txtIssue = (TextView) headView.findViewById(R.id.txt_issue);
        txtNumber = (TextView) headView.findViewById(txt_number);
        txtSurplus = (TextView) headView.findViewById(R.id.txt_surplus);
        txtSubtract = (TextView) headView.findViewById(R.id.txt_subtract);
        txtStartTime = (TextView) headView.findViewById(R.id.txt_startTime);
        barSchedule = (ProgressBar) headView.findViewById(R.id.bar_schedule);

        layoutHistory = (LinearLayout) headView.findViewById(R.id.layout_history);
        layoutIntroduce = (LinearLayout) headView.findViewById(R.id.layout_introduce);

    }

    private void initEvent() {
        txtMax.setOnClickListener(new HeaderViewOnClickListener());
        txtAdd.setOnClickListener(new HeaderViewOnClickListener());
        txtSubtract.setOnClickListener(new HeaderViewOnClickListener());
        layoutHistory.setOnClickListener(new HeaderViewOnClickListener());
        layoutIntroduce.setOnClickListener(new HeaderViewOnClickListener());
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setOnLoadListener(this);
    }

    private void initListView() {
        listJewel.addHeaderView(headView);
        listJewel.setAdapter(adapter);
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
        //设置banner点击事件
//        banner.setOnBannerClickListener(this);
        // 设置在操作Banner时listView事件不触发
        banner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    listJewel.requestDisallowInterceptTouchEvent(false);
                } else {
                    listJewel.requestDisallowInterceptTouchEvent(true);
                }
                return true;
            }
        });
        //banner设置方法全部调用完毕时最后调用
        banner.start();

    }

    @OnClick({R.id.layout_back, R.id.layout_share, R.id.txt_buyNow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.layout_share:
                SharedPreferences.Editor editor = wxShareSp.edit();
                editor.putString("shareWay", "jewel");
                editor.commit();

                showShare(view);
                break;

            case R.id.txt_buyNow:
                if(number > (model.getTotalNum()-model.getInvestNum())){
                    Toast.makeText(this, "参与数量不能大于剩余数量哦", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userInfoSp.getString("userId", null) != null) {
                    if (txtBuyNow.getText().toString().equals("参与")) {
                        statement(view);
                    }
                } else {
                    LoginUtil.toLogin(this);
                }

                break;
        }
    }

    private void showShare(View view) {

        // 一个自定义的布局，作为显示的内容
        View mview = LayoutInflater.from(this).inflate(R.layout.popup_share, null);

        ImageView qrCode = (ImageView) mview.findViewById(R.id.img_QRCode);
        ImageView wx = (ImageView) mview.findViewById(R.id.img_wx);
        ImageView pyq = (ImageView) mview.findViewById(R.id.img_pyq);
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

        qrCode.setVisibility(View.GONE);

        wx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                WxUtil.shareToWX(JewelActivity.this,shareURL,"小目标大玩法","花米宝邀您来玩小目标");
                popupWindow.dismiss();
            }
        });

        pyq.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                WxUtil.shareToPYQ(JewelActivity.this,shareURL,"小目标大玩法","花米宝邀您来玩小目标");
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

    private void getData() {

        JSONObject object = new JSONObject();
        try {
            object.put("code", code);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_615016, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    model = gson.fromJson(jsonObject.toString(), new TypeToken<TargetModel>() {}.getType());

                    setHeadView();
                    setTotalPrice();

                    images.clear();
                    String[] pic =  model.getAdvPic().split("\\|\\|");
                    for(String picName : pic){
                        System.out.println("picName="+picName);
                        images.add(picName);
                    }

                    ImageUtil.glide(pic[0],imgAdvPic,JewelActivity.this);

                    initBanner();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(JewelActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(JewelActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setHeadView() {
        txtInfo.setText(model.getSlogan());
        txtNumber.setText(number + "");
        txtName.setText((model.getToAmount()/1000)+getCurrency(model.getToCurrency()));

        txtIssue.setText("期号:"+model.getPeriods());
        txtSum.setText("总需 "+model.getTotalNum()+" 人次");
        txtSurplus.setText(Html.fromHtml("剩余 <html><font color=\"#2570fd\">"+(model.getTotalNum()-model.getInvestNum())+"</font></html>"));

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d5 = new Date(model.getStartDatetime());
        txtStartTime.setText(s.format(d5));

        barSchedule.setMax(model.getTotalNum());
        barSchedule.setProgress(model.getInvestNum());
    }

    private void getJewelRecord() {

        JSONObject object = new JSONObject();
        try {
            object.put("userId", "");
            object.put("jewelCode", code);
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("status","123");
            object.put("orderDir", "");
            object.put("orderColumn", "");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("companyCode", appConfigSp.getString("systemCode", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_615025, object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    List<JewelRecordModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<JewelRecordModel>>() {
                    }.getType());

                    if (page == 1) {
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
                Toast.makeText(JewelActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(JewelActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getStatement() {
        JSONObject object = new JSONObject();
        try {
            object.put("ckey", "treasure_statement");
            object.put("systemCode", appConfigSp.getString("systemCode", null));
            object.put("token", userInfoSp.getString("token", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post(CODE_807717, object.toString(), new Xutil.XUtils3CallBackPost() {
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
                Toast.makeText(JewelActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(JewelActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class HeaderViewOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.txt_subtract:
                    if (number > 1) {
                        number--;
                        txtNumber.setText(number + "");
                    }
                    setTotalPrice();
                    break;

                case R.id.txt_add:
                    if(number < (model.getTotalNum()-model.getInvestNum())){
                        number++;
                        txtNumber.setText(number + "");
                        setTotalPrice();
                    }
                    break;

                case R.id.txt_max:
                    if(model.getMaxNum() > (model.getTotalNum()-model.getInvestNum())){
                        number = (model.getTotalNum()-model.getInvestNum());
                    }else {
                        number = model.getMaxNum();
                    }
                    txtNumber.setText(number + "");
                    setTotalPrice();
                    break;

                case R.id.layout_history:
                    startActivity(new Intent(JewelActivity.this, JewelHistoryActivity.class).putExtra("templateCode",model.getTemplateCode()));
                    break;

                case R.id.layout_introduce:
                    startActivity(new Intent(JewelActivity.this, RichTextActivity.class).putExtra("cKey","treasure_rule"));
                    break;
            }
        }
    }

    public void setTotalPrice() {
        txtPrice.setText(MoneyUtil.moneyFormatDouble(model.getFromAmount() * number)+getCurrency(model.getFromCurrency()));
    }

    private TextView content;

    private void statement(View view) {

        // 一个自定义的布局，作为显示的内容
        View mview = LayoutInflater.from(this).inflate(R.layout.popup_statement, null);

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
                startActivity(new Intent(JewelActivity.this, JewelPayActivity.class)
                        .putExtra("price",MoneyUtil.moneyFormatDouble(model.getFromAmount() * number))
                        .putExtra("number",number+"")
                        .putExtra("currency",model.getFromCurrency())
                        .putExtra("code",model.getCode()));
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

    private String getCurrency(String currency){
        String type = "";

        switch (currency){
            case "FRB":
                type = "分润";
                break;

            case "GXJL":
                type = "贡献奖励";
                break;

            case "GWB":
                type = "购物币";
                break;

            case "QBB":
                type = "钱包币";
                break;

            case "HBB":
                type = "红包";
                break;

            case "HBYJ":
                type = "红包业绩";
                break;

            case "CNY":
                type = "人民币";
                break;
        }
        return type;
    }

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                page = 1;
                getData();
                getJewelRecord();

            }
        }, 1500);
    }

    @Override
    public void onLoad() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setLoading(false);
                page = page + 1;
                getData();
                getJewelRecord();
            }
        }, 1500);
    }
}
