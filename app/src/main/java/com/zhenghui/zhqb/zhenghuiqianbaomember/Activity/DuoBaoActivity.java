package com.zhenghui.zhqb.zhenghuiqianbaomember.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Application.MyApplication;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment.DetailFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment.DuoBaoFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment.EvaluateFragment;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.DuoBaoModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.LoginUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.MoneyUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.WxUtil;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class DuoBaoActivity extends MyBaseActivity implements EMMessageListener {


    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_commodity)
    TextView txtCommodity;
    @InjectView(R.id.line_commodity)
    View lineCommodity;
    @InjectView(R.id.txt_detail)
    TextView txtDetail;
    @InjectView(R.id.line_detail)
    View lineDetail;
    @InjectView(R.id.txt_evaluate)
    TextView txtEvaluate;
    @InjectView(R.id.line_evaluate)
    View lineEvaluate;
    @InjectView(R.id.layout_share)
    LinearLayout layoutShare;
    @InjectView(R.id.layout_content)
    FrameLayout layoutContent;
    @InjectView(R.id.layout_service)
    LinearLayout layoutService;
    @InjectView(R.id.txt_rmb)
    TextView txtRmb;
    @InjectView(R.id.txt_gwb)
    TextView txtGwb;
    @InjectView(R.id.txt_qbb)
    TextView txtQbb;
    @InjectView(R.id.txt_buyNow)
    TextView txtBuyNow;
    @InjectView(R.id.layout_rmb)
    LinearLayout layoutRmb;
    @InjectView(R.id.layout_gwb)
    LinearLayout layoutGwb;
    @InjectView(R.id.layout_qbb)
    LinearLayout layoutQbb;
    @InjectView(R.id.txt_point)
    TextView txtPoint;

    // 内容fragment
    private Fragment duoBaoFragment;
    private Fragment detailFragment;
    private Fragment evaluateFragment;

    private SharedPreferences appConfigSp;
    private SharedPreferences userInfoSp;


    private String code;
    private DuoBaoModel model;

    // 商品数量
    public int number = 1;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what){
                case 1:
                    txtPoint.setVisibility(View.VISIBLE);
                    break;

                case 2:
                    txtPoint.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duo_bao);
        ButterKnife.inject(this);
        MyApplication.getInstance().addActivity(this);

        inits();
        getDatas();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        number = 1;
//        getDatas();
        addMessageListener();
        getServiceUnReade();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().removeActivity(this);
    }

    private void inits() {
        code = getIntent().getStringExtra("code");

        userInfoSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        appConfigSp = getSharedPreferences("appConfig", Context.MODE_PRIVATE);
    }


    /**
     * 选择Fragment
     *
     * @param i
     */
    private void setSelect(int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        // 把图片设置为亮的
        // 设置内容区域
        switch (i) {
            case 0:
                if (duoBaoFragment == null) {
                    duoBaoFragment = DuoBaoFragment.newInstance(model);
                    transaction.add(R.id.layout_content, duoBaoFragment);
                } else {
                    transaction.show(duoBaoFragment);
                }
                txtCommodity.setTextColor(getResources().getColor(R.color.fontColor_orange));
                lineCommodity.setVisibility(View.VISIBLE);
                break;

            case 1:
                if (detailFragment == null) {
                    detailFragment = DetailFragment.newInstance(model.getDescriptionText(), model.getDescriptionPic());
                    transaction.add(R.id.layout_content, detailFragment);
                } else {
                    transaction.show(detailFragment);

                }
                txtDetail.setTextColor(getResources().getColor(R.color.fontColor_orange));
                lineDetail.setVisibility(View.VISIBLE);
                break;

            case 2:
                if (evaluateFragment == null) {
                    evaluateFragment = EvaluateFragment.newInstance(model.getCode());
//                    evaluateFragment = new EvaluateFragment();
                    transaction.add(R.id.layout_content, evaluateFragment);
                } else {
                    transaction.show(evaluateFragment);
                }
                txtEvaluate.setTextColor(getResources().getColor(R.color.fontColor_orange));
                lineEvaluate.setVisibility(View.VISIBLE);
                break;


        }

        transaction.commit();
    }


    private void hideFragment(FragmentTransaction transaction) {
        if (duoBaoFragment != null) {
            transaction.hide(duoBaoFragment);
        }
        if (detailFragment != null) {
            transaction.hide(detailFragment);
        }
        if (evaluateFragment != null) {
            transaction.hide(evaluateFragment);
        }
    }

    /**
     * 切换图片至暗色
     */
    private void resetImgs() {
        txtDetail.setTextColor(getResources().getColor(R.color.fontColor_gray));
        txtEvaluate.setTextColor(getResources().getColor(R.color.fontColor_gray));
        txtCommodity.setTextColor(getResources().getColor(R.color.fontColor_gray));

        lineDetail.setVisibility(View.INVISIBLE);
        lineEvaluate.setVisibility(View.INVISIBLE);
        lineCommodity.setVisibility(View.INVISIBLE);
    }


    /**
     * 获取宝贝详情
     */
    public void getDatas() {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
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

                    setSelect(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(DuoBaoActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(DuoBaoActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @OnClick({R.id.layout_back, R.id.txt_commodity, R.id.txt_detail, R.id.txt_evaluate, R.id.layout_share, R.id.layout_service, R.id.txt_buyNow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_commodity:
                resetImgs();
                setSelect(0);
                break;

            case R.id.txt_detail:
                resetImgs();
                setSelect(1);
                break;

            case R.id.txt_evaluate:
                resetImgs();
                setSelect(2);
                break;

            case R.id.layout_share:
                if (WxUtil.check(this)) {
                    WxUtil.share(this, "http://www.baidu.com", "一元夺宝", "描述");
                }

                break;

            case R.id.layout_service:
                if (userInfoSp.getString("userId", null) != null) {
                    Intent intent = new Intent(DuoBaoActivity.this, ChatActivity.class);
                    intent.putExtra("nickName", "客服");
                    intent.putExtra("myPhoto", userInfoSp.getString("photo",""));
                    intent.putExtra("otherPhoto", "");
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, "androidkefu");
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                    startActivity(intent);
                } else {
                    LoginUtil.toLogin(DuoBaoActivity.this);
                }

                break;

            case R.id.txt_buyNow:
                if (userInfoSp.getString("userId", null) != null) {
                    if (txtBuyNow.getText().toString().equals("参与夺宝")) {
                        startActivity(new Intent(DuoBaoActivity.this, DuoBaoPayActivity.class).putExtra("code", model.getCode()).putExtra("number", number));
                    }
                } else {
                    LoginUtil.toLogin(DuoBaoActivity.this);
                }

                break;
        }
    }

    public void setTotalPrice() {
        if (model.getPrice1() == 0) {
            layoutRmb.setVisibility(View.INVISIBLE);
        }
        if (model.getPrice2() == 0) {
            layoutGwb.setVisibility(View.INVISIBLE);
        }
        if (model.getPrice3() == 0) {
            layoutQbb.setVisibility(View.INVISIBLE);
        }

        txtRmb.setText(MoneyUtil.moneyFormatDouble(model.getPrice1() * number));
        txtGwb.setText(MoneyUtil.moneyFormatDouble(model.getPrice2() * number));
        txtQbb.setText(MoneyUtil.moneyFormatDouble(model.getPrice3() * number));
    }

    private void addMessageListener(){
        EaseUI.getInstance().pushActivity(this);
        EMClient.getInstance().chatManager().addMessageListener(this);
    }

    @Override
    public void onMessageReceived(List<EMMessage> list) {
        for (EMMessage message : list) {
            EaseUI.getInstance().getNotifier().onNewMsg(message);
            getServiceUnReade();
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    private void getServiceUnReade() {
        try {
            Message msg = new Message();
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(SERVICE_ID);
            if(conversation != null){
                if (conversation.getUnreadMsgCount() > 0) {
                    msg.what = 1;
                }else{
                    msg.what = 2;
                }
                handler.sendMessage(msg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
